package robot.abilities.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import robot.abilities.AbilitiesMod;
import robot.abilities.block.blockentity.MagicCircleBlockEntity;
import robot.abilities.item.ManaInfusedItem;

public class MagicCircleBlock extends BlockWithEntity {
    public static final IntProperty CIRCLE_LEVEL = IntProperty.of("level", 0, 1);
    public static final MapCodec<MagicCircleBlock> CODEC = MagicCircleBlock.createCodec(MagicCircleBlock::new);
    private static long lastUse = 0;

    public MagicCircleBlock(Settings settings) {
        super(settings.nonOpaque());
        this.setDefaultState(super.getDefaultState().with(CIRCLE_LEVEL, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CIRCLE_LEVEL);
    }

    //Что вы делаете в его коде? @kartoshka
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!(world.getBlockState(pos).getBlock() instanceof MagicCircleBlock) || world.isClient()) {
            return ActionResult.FAIL;
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUse < 100) return ActionResult.CONSUME;
        lastUse = currentTime;
        MagicCircleBlockEntity entity = (MagicCircleBlockEntity) world.getBlockEntity(pos);
        MagicCircleBlockEntity.ItemPositions itemPos = entity.getItemsPosition(state);
        Vec3d posHit = hit.getPos();
        int x = (int) ((posHit.x - pos.getX()) * itemPos.size().x);
        int z = (int) ((posHit.z - pos.getZ()) * itemPos.size().y);
        int index = x + z * (itemPos.size().x + 1);
        int slot = Integer.parseInt("" + itemPos.map().charAt(index));
        ItemStack item = player.getStackInHand(hand);
        ItemStack itemInSlot = entity.getStack(slot);
        if ((item.isEmpty() && itemInSlot.isEmpty()) ||
                (slot == 0 && !item.isEmpty() && !(item.getItem() instanceof ManaInfusedItem)))
            return ActionResult.CONSUME;
        if (item.getItem() == itemInSlot.getItem()) {
            int itemCountInSlot = itemInSlot.getCount();
            int itemCountInHand = item.getCount();
            if (itemCountInSlot < itemInSlot.getMaxCount()) {
                int remainingSpace = itemInSlot.getMaxCount() - itemCountInSlot;
                int transferAmount = Math.min(remainingSpace, itemCountInHand);
                itemInSlot.increment(transferAmount);
                item.decrement(transferAmount);
            }
        } else {
            player.setStackInHand(hand, itemInSlot);
            entity.setStack(slot, item);
        }
        entity.markDirty();
        AbilitiesMod.LOGGER.info(entity.updateRecipe(state));
        return ActionResult.SUCCESS;
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
        BlockPos blockPos = pos.down();
        return !world.isAir(blockPos);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : MagicCircleBlock.validateTicker(type, ModBlockEntities.MAGIC_CIRCLE_BLOCK_ENTITY, MagicCircleBlockEntity::tick);
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
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        return Block.createCuboidShape(0, 0, 0, 16, 0.1, 16);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
