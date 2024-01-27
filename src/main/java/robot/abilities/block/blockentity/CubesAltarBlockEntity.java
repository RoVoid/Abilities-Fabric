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
import org.jetbrains.annotations.Nullable;
import robot.abilities.block.ModBlockEntities;
import robot.abilities.network.ModMessages;

public class CubesAltarBlockEntity extends BlockEntity {
    private ItemStack renderItem = ItemStack.EMPTY;

    public CubesAltarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public CubesAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ALTAR_BLOCK_ENTITY, pos, state);
    }


    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("item")) {
            renderItem = ItemStack.fromNbt(nbt.getCompound("item"));
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.put("item", renderItem.writeNbt(new NbtCompound()));
        super.writeNbt(nbt);
    }

    public void setRenderItem(ItemStack item) {
        renderItem = item.copy();
        markDirty();
    }

    public ItemStack getRenderItem() {
        return renderItem.copy();
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
                buf.writeItemStack(getRenderItem());
                buf.writeBlockPos(getPos());
                for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
                    ServerPlayNetworking.send(player, ModMessages.ALTAR_SYNC, buf);
                }
            }
        super.markDirty();
    }

    public static class Render implements BlockEntityRenderer<CubesAltarBlockEntity> {

        public Render(BlockEntityRendererFactory.Context context) {
        }

        @Override
        public void render(CubesAltarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
            ItemStack stack = entity.getRenderItem();
            if (!stack.isEmpty()) {
                matrices.push();

                matrices.translate(0.5, 1f, 0.5);
                matrices.scale(1.2f, 1.2f, 1.2f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((entity.getWorld().getTime() + tickDelta) * 4));
                MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, 0xF000F0, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
                matrices.pop();
            }
        }
    }
}