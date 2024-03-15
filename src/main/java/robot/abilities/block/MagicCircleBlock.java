package robot.abilities.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import robot.abilities.block.blockentity.MagicCircleBlockEntity;

public class MagicCircleBlock extends BlockWithEntity {
    private static long lastUse = 0;

    public static final MapCodec<MagicCircleBlock> CODEC = MagicCircleBlock.createCodec(MagicCircleBlock::new);

    public MagicCircleBlock(Settings settings) {
        super(settings.nonOpaque());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUse < 100 || world.isClient) return ActionResult.CONSUME;
        lastUse = currentTime;
        MagicCircleBlockEntity entity = (MagicCircleBlockEntity) world.getBlockEntity(pos);
        Vec3d posHit = hit.getPos();
        int x = (int) Math.max(0, Math.min(2, (posHit.x - pos.getX()) * 3)),
                z = (int) Math.max(0, Math.min(2, (posHit.z - pos.getZ()) * 3));
        int index = x * 3 + z;
        ItemStack item = player.getStackInHand(hand);
        if (item == entity.getItem(index)) return ActionResult.FAIL;
        player.setStackInHand(hand, entity.getItem(index));
        entity.setItem(index, item);
        return ActionResult.SUCCESS;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MagicCircleBlockEntity(pos, state);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return super.canPlaceAt(state, world, pos);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        return Block.createCuboidShape(0, 0, 0, 16, 0.1, 16);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
