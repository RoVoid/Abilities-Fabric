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
import org.jetbrains.annotations.Nullable;
import robot.abilities.block.ModBlockEntities;
import robot.abilities.network.ModMessages;

import java.util.ArrayList;
import java.util.List;

public class MagicCircleBlockEntity extends BlockEntity {
    private static final List<Vec3d> itemsPos = new ArrayList<>();
    private static final double p = 0.5, q = 0.7;

    static {
        itemsPos.add(new Vec3d(-p, 0, -p));
        itemsPos.add(new Vec3d(-q, 0, 0));
        itemsPos.add(new Vec3d(-p, 0, p));
        itemsPos.add(new Vec3d(0, 0, -q));
        itemsPos.add(new Vec3d(0, 0, 0));
        itemsPos.add(new Vec3d(0, 0, q));
        itemsPos.add(new Vec3d(p, 0, -p));
        itemsPos.add(new Vec3d(q, 0, 0));
        itemsPos.add(new Vec3d(p, 0, p));
    }

    private final List<ItemStack> items = new ArrayList<>();

    public MagicCircleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public MagicCircleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MAGIC_CIRCLE_BLOCK_ENTITY, pos, state);
        for (int i = 0; i < 9; i++) items.add(ItemStack.EMPTY);
    }


    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        for (int i = 0; i < 9; i++) {
            if (nbt.contains("" + i)) {
                items.set(i, ItemStack.fromNbt(nbt.getCompound("" + i)));
            }
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        for (int i = 0; i < 9; i++) {
            nbt.put("" + i, items.get(i).writeNbt(new NbtCompound()));
        }
        super.writeNbt(nbt);
    }

    public void setItem(int index, ItemStack item) {
        items.set(index, item.copy());
        markDirty();
    }

    public ItemStack getItem(int index) {
        return items.get(index).copy();
    }

    public List<Vec3d> getItemsPos() {
        return itemsPos;
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
        if (world != null)
            if (!world.isClient()) {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBlockPos(getPos());
                for (int i = 0; i < 9; i++) {
                    buf.writeItemStack(items.get(i));
                }
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
            for (int i = 0; i < 9; i++) {
                ItemStack stack = entity.getItem(i);
                Vec3d pos = entity.getItemsPos().get(i);
                if (!stack.isEmpty()) {
                    matrices.push();
                    matrices.translate(pos.x + 0.5, pos.y + 0.01, pos.z + 0.5);
                    matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
                    matrices.scale(0.3f, 0.3f, 0.3f);
                    MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.FIXED, 0xFFFFFF, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
                    matrices.pop();
                }
            }
        }
    }
}