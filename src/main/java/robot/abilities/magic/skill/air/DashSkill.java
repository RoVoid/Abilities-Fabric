package robot.abilities.magic.skill.air;

import net.minecraft.entity.LivingEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.random.Random;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.property.DoubleProperty;
import robot.abilities.magic.property.IntProperty;
import robot.abilities.magic.skill.Skill;
import robot.abilities.util.Utils;

public class DashSkill extends Skill {
    public final DoubleProperty DASH = new DoubleProperty(1.2, 0.01);

    public DashSkill() {
        super(AbilitiesMod.ID, "dash", Type.SUPPORT, Rarity.COMMON, new DoubleProperty(1.0), new IntProperty(10));
    }

    public static boolean shouldDamageAttacker(int level, Random random) {
        if (level <= 0) return false;
        return random.nextFloat() < 0.015f * (float) level;
    }

    @Override
    public boolean use(LivingEntity user, int level) {
        if (user.getWorld().isClient) return false;
        user.velocityModified = true;
        user.addVelocity(user.getRotationVec(1).multiply(DASH.get(level)));
        return true;
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip",
                Text.literal(Utils.decimal(DASH.get(level))).formatted(Formatting.GOLD),
                Text.literal(Utils.decimal(MP.get(level))).formatted(Formatting.GOLD));
    }
}
