package robot.abilities.block;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import robot.abilities.block.blockentity.CubesAltarBlockEntity;
import robot.abilities.item.ModItems;
import robot.abilities.item.cubes.MagicCubeItem;
import robot.abilities.network.ModMessages;
import robot.abilities.particle.ModParticles;

import java.util.function.ToIntFunction;

public class CubesAltarBlock extends BlockWithEntity implements Waterloggable {

    public static final IntProperty TYPE = IntProperty.of("type", 0, 4);
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final MapCodec<CubesAltarBlock> CODEC = CubesAltarBlock.createCodec(CubesAltarBlock::new);

    protected CubesAltarBlock(Settings settings) {
        super(settings.nonOpaque().luminance(getLuminance()));
        this.setDefaultState(this.stateManager.getDefaultState().with(WATERLOGGED, false).with(TYPE, 0));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    private static ToIntFunction<BlockState> getLuminance() {
        return state -> state.get(TYPE) == 1 ? 15 : 0;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CubesAltarBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, TYPE);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return this.getDefaultState().with(WATERLOGGED, fluidState.isOf(Fluids.WATER));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0, 0, 0, 1, 0.9, 1);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return state.get(TYPE) != 0;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(TYPE) == 0 ? 0 : 15;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;

        CubesAltarBlockEntity entity = world.getBlockEntity(pos) instanceof CubesAltarBlockEntity altarEntity ? altarEntity : null;
        if (entity == null) return ActionResult.PASS;

        ItemStack item = player.getStackInHand(hand);
        if (!(item.getItem() instanceof MagicCubeItem) && !item.isEmpty()) return ActionResult.PASS;

        // Изменение типа блока на основе предмета
        if (item.getItem().equals(ModItems.FIRE_CUBE)) {
            world.setBlockState(pos, state.with(TYPE, 1));
        } else if (item.getItem().equals(ModItems.WATER_CUBE)) {
            world.setBlockState(pos, state.with(TYPE, 2));
        } else if (item.getItem().equals(ModItems.EARTH_CUBE)) {
            world.setBlockState(pos, state.with(TYPE, 3));
        } else if (item.getItem().equals(ModItems.AIR_CUBE)) {
            world.setBlockState(pos, state.with(TYPE, 4));
        } else world.setBlockState(pos, state.with(TYPE, 0));

        // Обновление предмета в руке
        player.setStackInHand(hand, entity.getRenderItem());
        entity.setRenderItem(item);

        // Отправка частицы всем игрокам
        for (ServerPlayerEntity serverPlayer : player.getServer().getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(serverPlayer, ModMessages.ALTAR_PARTICLE, PacketByteBufs.create().writeBlockPos(pos));
        }

        return ActionResult.SUCCESS;
    }

    public void onClient(World world, BlockPos pos) {
        for (int i = 0; i < 12; i++) {
            double angle = 2 * Math.PI / 12 * i;
            double x = Math.cos(angle) * 0.5;
            double z = Math.sin(angle) * 0.5;
            world.addParticle(ModParticles.RED_FLAME, pos.getX() + 0.5 + x, pos.getY() + 0.7 + 0.05 * i, pos.getZ() + 0.5 + z, 0, 0.01, 0);
        }
    }
}
