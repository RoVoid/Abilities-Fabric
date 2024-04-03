package robot.abilities.block.blockentity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
import robot.abilities.AbilitiesMod;
import robot.abilities.block.MagicCircleBlock;
import robot.abilities.block.ModBlockEntities;
import robot.abilities.item.ManaInfusedItem;
import robot.abilities.network.ModMessages;
import robot.abilities.recipe.CircleRecipe;
import robot.abilities.recipe.ModCircleRecipes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class MagicCircleBlockEntity extends BlockEntity {
    private static final List<ItemPositions> itemPositions = new ArrayList<>();
    private static final double p = 0.5, q = 0.7;

    private String lastRecipeName = "";
    private int creationTime = 0;

    static {
        itemPositions.add(new ItemPositions("1122\n1002\n3004\n3344", new Vec3d(0, 0, 0), new Vec3d(-0.3, 0, -0.3), new Vec3d(0.3, 0, -0.3), new Vec3d(-0.3, 0, 0.3), new Vec3d(0.3, 0, 0.3)));
    }

    private final List<ItemStack> items = new ArrayList<>();

    public MagicCircleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        if (state.get(MagicCircleBlock.CIRCLE_LEVEL) == 0) {
            for (int i = 0; i < 5; i++) items.add(ItemStack.EMPTY);
        } else if (state.get(MagicCircleBlock.CIRCLE_LEVEL) == 1) {
            for (int i = 0; i < 9; i++) items.add(ItemStack.EMPTY);
        }
    }

    public MagicCircleBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.MAGIC_CIRCLE_BLOCK_ENTITY, pos, state);
    }


    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        items.clear();
        for (int i = 0; i < nbt.getInt("size"); i++) {
            items.add(nbt.contains("" + i) ? ItemStack.fromNbt(nbt.getCompound("" + i)) : ItemStack.EMPTY);
        }
        lastRecipeName = nbt.getString("recipe");
        if (hasWorld()) updateRecipe(world.getBlockState(pos));
        creationTime = nbt.getInt("time");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("size", items.size());
        for (int i = 0; i < items.size(); i++) {
            nbt.put("" + i, items.get(i).writeNbt(new NbtCompound()));
        }
        nbt.putString("recipe", lastRecipeName);
        nbt.putInt("time", creationTime);
        super.writeNbt(nbt);
    }

    public static void tick(World world, BlockPos pos, BlockState state, MagicCircleBlockEntity blockEntity) {
        if (blockEntity.lastRecipeName.isEmpty()) return;
        blockEntity.creationTime++;
        CircleRecipe recipe = ModCircleRecipes.getRecipe(blockEntity.lastRecipeName);
        if (recipe == null) return;
        if (blockEntity.creationTime > recipe.getCreationTime()) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, recipe.getResult()));
            int c, ci, cj = 0;
            ItemStack manaStack = blockEntity.getStack(0);
            while (((ManaInfusedItem) manaStack.getItem()).getManaAmount() * cj < recipe.getManaAmount()) {
                cj++;
            }
            manaStack.setCount(manaStack.getCount() - cj);
            blockEntity.setStack(0, manaStack);
            if (recipe.isShaped()) {
                for (int i = 0; i < recipe.getIngredients().size(); i++) {
                    ItemStack stack = recipe.getIngredients().get(i);
                    c = blockEntity.getStack(i).getCount();
                    if (c - stack.getCount() > 0) {
                        blockEntity.setStack(i, new ItemStack(blockEntity.getStack(i).getItem(), c - stack.getCount()));
                        continue;
                    }
                    blockEntity.setStack(i, ItemStack.EMPTY);
                }
            } else {
                List<ItemStack> list = new LinkedList<>(blockEntity.items);
                List<ItemStack> list1 = new LinkedList<>(recipe.getIngredients());
                list.remove(0);
                list.removeIf(ItemStack::isEmpty);
                list.sort(Comparator.comparingInt(ItemStack::getCount));
                list1.removeIf(ItemStack::isEmpty);
                list1.sort(Comparator.comparingInt(ItemStack::getCount));
                for (ItemStack stack : list) {
                    for (int j = 0; j < list1.size(); j++)
                        if (stack.getItem() == list1.get(j).getItem() && stack.getCount() >= list1.get(j).getCount()) {
                            ci = blockEntity.items.indexOf(stack);
                            c = blockEntity.getStack(ci).getCount();
                            blockEntity.setStack(ci, c - list1.get(j).getCount() > 0 ? new ItemStack(stack.getItem(), c - list1.get(j).getCount()) : ItemStack.EMPTY);
                            list1.remove(j);
                            break;
                        }
                }
            }
            blockEntity.markDirty();
            blockEntity.updateRecipe(state);
        }
    }

    public String updateRecipe(BlockState state) {
        if (!(state.getBlock() instanceof MagicCircleBlock)) return "";
        creationTime = 0;
        List<ItemStack> list = new LinkedList<>(items);
        if (getManaAmount() <= 0 || items.size() <= 1) {
            lastRecipeName = "";
            return "";
        }
        list.remove(0);
        list.removeIf(ItemStack::isEmpty);
        list.sort(Comparator.comparingInt(ItemStack::getCount));
        AbilitiesMod.LOGGER.info("LIST Inventory " + list);
        for (String name : ModCircleRecipes.getRecipes().keySet()) {
            CircleRecipe recipe = ModCircleRecipes.getRecipe(name);
            if (recipe == null) continue;
            if (!recipe.getSupportedLevels().contains(state.get(MagicCircleBlock.CIRCLE_LEVEL))) continue;
            if (recipe.getManaAmount() > getManaAmount()) continue;
            boolean e = false;
            if (recipe.isShaped()) {
                for (int i = 0; i < recipe.getIngredients().size(); i++) {
                    ItemStack stack = recipe.getIngredients().get(i);
                    if (getStack(i).getItem() != stack.getItem() || getStack(i).getCount() < stack.getCount()) {
                        e = true;
                        break;
                    }
                }
            } else {
                List<ItemStack> list1 = new LinkedList<>(recipe.getIngredients());
                list1.removeIf(ItemStack::isEmpty);
                list1.sort(Comparator.comparingInt(ItemStack::getCount));
                AbilitiesMod.LOGGER.info("LIST Recipe " + list1);
                if (list.size() != list1.size()) break;
                for (ItemStack itemStack : list) {
                    e = true;
                    for (int j = 0; j < list1.size(); j++)
                        if (itemStack.getItem() == list1.get(j).getItem() && itemStack.getCount() >= list1.get(j).getCount()) {
                            e = false;
                            AbilitiesMod.LOGGER.info(itemStack.getItem() + " equals " + list1.get(j).getItem());
                            list1.remove(j);
                            break;
                        }
                    if (e) break;
                }
                e = !list1.isEmpty();
            }
            if (!e) {
                lastRecipeName = name;
                return name;
            }
        }
        lastRecipeName = "";
        return "";
    }

    public double getManaAmount() {
        if (!(items.get(0).getItem() instanceof ManaInfusedItem)) return 0;
        return ((ManaInfusedItem) items.get(0).getItem()).getManaAmount() * items.get(0).getCount();
    }

    public void setStack(int index, ItemStack item) {
        items.set(Math.max(0, Math.min(index, items.size() - 1)), item.copy());
    }

    public ItemStack getStack(int index) {
        return index < 0 || items.size() <= index ? ItemStack.EMPTY : items.get(index);
    }

    public int getItemsSize() {
        return items.size();
    }

    public ItemPositions getItemsPosition(BlockState state) {
        return itemPositions.get(state.get(MagicCircleBlock.CIRCLE_LEVEL));
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public void markDirty() {
        if (world != null) if (!world.isClient()) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBlockPos(getPos());
            buf.writeInt(items.size());
            items.forEach(buf::writeItemStack);
            for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
                ServerPlayNetworking.send(player, ModMessages.MAGIC_CIRCLE_SYNC, buf);
            }
        }
        super.markDirty();
    }

    public static class Render implements BlockEntityRenderer<MagicCircleBlockEntity> {

        public Render(BlockEntityRendererFactory.Context context) {
        }

        @Override
        public void render(MagicCircleBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
            if (entity == null || entity.world == null || !(entity.world.getBlockState(entity.pos).getBlock() instanceof MagicCircleBlock))
                return;
            for (int i = 0; i < entity.getItemsSize(); i++) {
                ItemStack stack = entity.getStack(i);
                Vec3d pos = entity.getItemsPosition(entity.world.getBlockState(entity.pos)).offsets().get(i);
                if (!stack.isEmpty()) {
                    int c = stack.getCount() / 8 + 1;
                    if (stack.getCount() > 1) c += 1;
                    for (int k = 0; k < c; k++) {
                        matrices.push();
                        matrices.translate(pos.x + 0.5 + k % 3 * 0.015, pos.y + 0.01 + k * 0.02, pos.z + 0.5 + k % 3 * 0.015);
                        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
                        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(44 * k * k));
                        matrices.scale(0.3f, 0.3f, 0.3f);
                        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.FIXED, 0xFFFFFF, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
                        matrices.pop();
                    }
                }
            }
        }
    }

    public static class ItemPositions {
        private final List<Vec3d> _offsets;
        private final String _map;
        private final int countX, countY;

        public ItemPositions(String map, List<Vec3d> offsets) {
            this._offsets = offsets;
            this._map = map;
            this.countX = map.indexOf("\n");
            int i = -1, c = 1;
            while (map.indexOf("\n", i + 1) >= 0) {
                i = map.indexOf("\n", i + 1);
                c++;
            }
            this.countY = c;
        }

        public ItemPositions(String map, Vec3d... offsets) {
            this(map, List.of(offsets));
        }

        public List<Vec3d> offsets() {
            return _offsets;
        }

        public String map() {
            return _map;
        }

        public Vector2i size() {
            return new Vector2i(countX, countY);
        }
    }
}
