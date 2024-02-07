package robot.abilities.block;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class CrystalBudBlock extends Block implements Waterloggable {
    public static final DirectionProperty FACING = FacingBlock.FACING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final BooleanProperty SMALL = BooleanProperty.of("small");

    public CrystalBudBlock(Settings settings) {
        super(settings.nonOpaque().luminance((state) -> 10));
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.UP).with(SMALL, false).with(WATERLOGGED, false));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getSide()).with(WATERLOGGED, ctx.getWorld().getBlockState(ctx.getBlockPos()).getBlock() == Blocks.WATER);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = switch (state.get(FACING)) {
            case UP -> pos.down();
            case DOWN -> pos.up();
            case NORTH -> pos.south();
            case SOUTH -> pos.north();
            case EAST -> pos.west();
            case WEST -> pos.east();
        };
        return world.getBlockState(blockPos).getBlock() == ModBlocks.CRYSTAL_BLOCK;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, SMALL, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.get(WATERLOGGED)) {
            return Fluids.WATER.getStill(false);
        }
        return super.getFluidState(state);
    }
}
