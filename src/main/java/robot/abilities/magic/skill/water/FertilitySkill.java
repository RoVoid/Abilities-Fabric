package robot.abilities.magic.skill.water;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.skill.AbstractSkill;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IEntityDataSaver;

public class FertilitySkill extends AbstractSkill {
    Property radius = new Property(0, 0.5);

    public FertilitySkill() {
        super(AbilitiesMod.ID + ":fertility", new Property(0.1, 0.2), new Property(5));
    }

    @Override
    public void use(LivingEntity entity, int level) {
        World world = entity.getWorld();
        double mp = getMp().get(level);
        IEntityDataSaver cap = (IEntityDataSaver) entity;
        if (world.isClient || cap.get(DataKeys.MP) < mp) return;
        int r = (int) Math.floor(radius.get(level));
        BlockPos pos = new BlockPos((int) Math.floor(entity.getX()), (int) Math.round(entity.getY()), (int) Math.floor(entity.getZ()));
        Fertilizable fertilizable;
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos p = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    BlockState state = world.getBlockState(p);
                    Block block = state.getBlock();
                    if (block instanceof Fertilizable && (fertilizable = (Fertilizable) block).isFertilizable(world, pos, state)) {
                        if (fertilizable.canGrow(world, world.random, pos, state)) {
                            fertilizable.grow((ServerWorld) world, world.random, pos, state);
                        }
                    }
                }
            }
        }
        cap.add(DataKeys.MP, -mp);
        cap.add(DataKeys.SCORE, 5);
        cap.sync(entity, DataKeys.MP, DataKeys.SCORE);
    }
}
