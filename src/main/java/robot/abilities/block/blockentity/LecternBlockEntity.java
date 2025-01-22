package robot.abilities.block.blockentity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import robot.abilities.block.ModBlockEntities;

public class LecternBlockEntity extends BlockEntity {
    private ItemStack book = ItemStack.EMPTY;

    public LecternBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public LecternBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.LECTERN_BLOCK_ENTITY, pos, state);
    }

    public void setBook(ItemStack book) {
        this.book = book == null ? ItemStack.EMPTY : book;
    }

    public ItemStack getBook() {
        return book;
    }

    public boolean hasBook() {
        return !book.isEmpty();
    }

    private Long lastUseTime;

    public Long getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(Long time) {
        this.lastUseTime = time;
    }


    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        book = ItemStack.fromNbt(nbt.getCompound("book"));
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.put("book", book.writeNbt(new NbtCompound()));
        super.writeNbt(nbt);
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

    @Environment(value = EnvType.CLIENT)
    public static class Render implements BlockEntityRenderer<LecternBlockEntity> {

        public Render(BlockEntityRendererFactory.Context context) {
        }

        @Override
        public void render(LecternBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        }
    }
}
