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
import robot.abilities.magic.property.Property;
import robot.abilities.magic.skill.Skill;
import robot.abilities.util.Utils;

import java.text.DecimalFormat;

public class FertilitySkill extends Skill {
    public FertilitySkill() {
        super(AbilitiesMod.ID, "fertility", Type.SUPPORT, Rarity.COMMON, Property.of(0.1, 0.2), Property.of(1));
        add("radius", Property.of(0, 0.05));
    }

    @Override
    public boolean use(LivingEntity user, int level) {
        World world = user.getWorld();
        if (world.isClient) return false;
        int r = get("radius", level);
        BlockPos pos = user.getBlockPos();
        Fertilizable fertilizable;
        boolean used = false;
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos p = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    BlockState state = world.getBlockState(p);
                    Block block = state.getBlock();
                    if (block instanceof Fertilizable && (fertilizable = (Fertilizable) block).isFertilizable(world, p, state)) {
                        if (fertilizable.canGrow(world, world.random, p, state)) {
                            Utils.addParticles((ServerWorld) world, ParticleTypes.HAPPY_VILLAGER, false, p.getX(), p.getY(), p.getZ(), 1, 1, 1, 0.5f, 10);
                            fertilizable.grow((ServerWorld) world, world.random, p, state);
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
        return Text.translatable(getTranslateKey() + ".tooltip", Text.literal(Utils.decimal("#.#", getInt("radius", level) + 1)).formatted(Formatting.GOLD), Text.literal(new DecimalFormat("#.#").format(get("mp", level))).formatted(Formatting.GOLD));
    }
}
