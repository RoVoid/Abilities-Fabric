package robot.abilities.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import robot.abilities.block.blockentity.CubesAltarBlockEntity;
import robot.abilities.item.ModItems;
import robot.abilities.item.cubes.ICube;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

public class CubesAltarBlock extends BlockWithEntity implements Waterloggable {

    public static final IntProperty TYPE = IntProperty.of("type", 0, 4);
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final MapCodec<CubesAltarBlock> CODEC = CubesAltarBlock.createCodec(CubesAltarBlock::new);

    private static long lastUse = 0;

    protected CubesAltarBlock(Settings settings) {
        super(settings.nonOpaque().notSolid().luminance(getLuminance()));
        this.setDefaultState(this.stateManager.getDefaultState().with(WATERLOGGED, false).with(TYPE, 0));
    }

    protected static ToIntFunction<BlockState> getLuminance()
    {
        return (state) -> state.get(TYPE) == 0 ? 15 : 0;
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
        if (!(item.getItem() instanceof ICube) && !item.isEmpty()) return ActionResult.PASS;
        if (item.getItem().equals(ModItems.FIRE_CUBE)) {
            world.setBlockState(pos, state.with(TYPE, 0));
        } else if (item.getItem().equals(ModItems.WATER_CUBE)) {
            world.setBlockState(pos, state.with(TYPE, 1));
        } else if (item.getItem().equals(ModItems.EARTH_CUBE)) {
            world.setBlockState(pos, state.with(TYPE, 2));
        } else if (item.getItem().equals(ModItems.AIR_CUBE)) {
            world.setBlockState(pos, state.with(TYPE, 3));
        }
        player.setStackInHand(hand, entity.getRenderItem());
        entity.setRenderItem(item);
        return ActionResult.SUCCESS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, TYPE);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.get(WATERLOGGED)) {
            return Fluids.WATER.getStill(false);
        }
        return super.getFluidState(state);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(WATERLOGGED, ctx.getWorld().getBlockState(ctx.getBlockPos()).getBlock() == Blocks.WATER);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0, 0, 0, 1, 0.9, 1);
    }
}
