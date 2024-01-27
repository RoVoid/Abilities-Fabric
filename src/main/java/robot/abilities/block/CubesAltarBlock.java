package robot.abilities.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import robot.abilities.block.blockentity.CubesAltarBlockEntity;
import robot.abilities.item.cubes.ICube;

public class CubesAltarBlock extends BlockWithEntity {
    public static final MapCodec<CubesAltarBlock> CODEC = CubesAltarBlock.createCodec(CubesAltarBlock::new);
    private static long lastUse = 0;

    protected CubesAltarBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CubesAltarBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUse < 100 || world.isClient) return ActionResult.CONSUME;
        lastUse = currentTime;
        CubesAltarBlockEntity entity = (CubesAltarBlockEntity) world.getBlockEntity(pos);
        ItemStack item = player.getStackInHand(hand);
        if (!(item.getItem() instanceof ICube) && !item.isEmpty()) return ActionResult.CONSUME;
        player.setStackInHand(hand, entity.getRenderItem());
        entity.setRenderItem(item);
        return ActionResult.SUCCESS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

}
