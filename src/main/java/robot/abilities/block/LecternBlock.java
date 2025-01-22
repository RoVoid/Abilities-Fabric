package robot.abilities.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import robot.abilities.block.blockentity.LecternBlockEntity;
import robot.abilities.item.SkillBook;

public class LecternBlock extends BlockWithEntity {
    public static final BooleanProperty HAS_BOOK = BooleanProperty.of("book");
    public static final IntProperty BOOK_LEVEL = IntProperty.of("level", 0, 4);

    public static final MapCodec<LecternBlock> CODEC = LecternBlock.createCodec(LecternBlock::new);

    public LecternBlock(Settings settings) {
        super(settings.nonOpaque());
        this.setDefaultState(super.getDefaultState().with(HAS_BOOK, false).with(BOOK_LEVEL, 0));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HAS_BOOK, BOOK_LEVEL);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!(world.getBlockState(pos).getBlock() instanceof LecternBlock) || world.isClient) {
            return ActionResult.FAIL;
        }
        if (world.getBlockEntity(pos) instanceof LecternBlockEntity entity) {
            long currentTime = world.getTime();
            if (entity.getLastUseTime() != null && currentTime - entity.getLastUseTime() < 5) {
                return ActionResult.PASS;
            }
            entity.setLastUseTime(currentTime);

            ItemStack stack = player.getStackInHand(hand).copy();
            ItemStack book = entity.getBook().copy();
            if ((stack.isEmpty() && book.isEmpty()) || (!(stack.getItem() instanceof SkillBook) && !stack.isEmpty())) {
                return ActionResult.PASS;
            }
            player.setStackInHand(hand, book);
            entity.setBook(stack);
            entity.markDirty();
            state = state.with(HAS_BOOK, entity.hasBook());
            state = state.with(BOOK_LEVEL, entity.hasBook() ? SkillBook.getSkillRarity(entity.getBook()) : 0);
            world.setBlockState(pos, state, 3);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, state));
        }
        return ActionResult.SUCCESS;
    }


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(0, 0, 0, 16, 16, 16);
    }


    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        if (world.getBlockEntity(pos) instanceof LecternBlockEntity entity) {
            if (entity.hasBook()) {
                ItemEntity itemEntity = new ItemEntity((World) world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, entity.getBook().copy());
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
                entity.setBook(null);
            }
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LecternBlockEntity(pos, state);
    }
}
