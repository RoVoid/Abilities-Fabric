package robot.abilities.magic.skill.water;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.property.DoubleProperty;
import robot.abilities.magic.property.IntProperty;
import robot.abilities.magic.skill.Skill;
import robot.abilities.util.Utils;

import java.text.DecimalFormat;

public class FertilitySkill extends Skill {
    public final IntProperty RADIUS = new IntProperty(0, 0.05);

    public FertilitySkill() {
        super(AbilitiesMod.ID, "fertility", Type.SUPPORT, Rarity.COMMON, new DoubleProperty(0.1, 0.2), new IntProperty(1));
    }

    @Override
    public boolean use(LivingEntity user, int level) {
        World world = user.getWorld();
        if (world.isClient) return false;
        int radius = RADIUS.get(level);
        BlockPos pos = user.getBlockPos();
        Fertilizable fertilizable;
        boolean used = false;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos1 = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    BlockState state = world.getBlockState(pos1);
                    Block block = state.getBlock();
                    if (block instanceof Fertilizable && (fertilizable = (Fertilizable) block).isFertilizable(world, pos1, state)) {
                        if (fertilizable.canGrow(world, world.random, pos1, state)) {
                            Utils.addParticles((ServerWorld) world, ParticleTypes.HAPPY_VILLAGER, false, pos1.getX(), pos1.getY(), pos1.getZ(), 1, 1, 1, 0.5f, 10);
                            fertilizable.grow((ServerWorld) world, world.random, pos1, state);
                            if (!used) used = true;
                        }
                    }
                }
            }
        }
        return used;
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip",
                Text.literal(Utils.decimal("#.#", RADIUS.get(level) + 1)).formatted(Formatting.GOLD),
                Text.literal(new DecimalFormat("#.#").format(MP.get(level))).formatted(Formatting.GOLD));
    }
}
