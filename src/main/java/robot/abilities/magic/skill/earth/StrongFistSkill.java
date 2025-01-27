package robot.abilities.magic.skill.earth;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import robot.abilities.AbilitiesMod;
import robot.abilities.effect.ModEffects;
import robot.abilities.magic.property.DoubleProperty;
import robot.abilities.magic.property.IntProperty;
import robot.abilities.magic.skill.Skill;
import robot.abilities.util.Utils;

public class StrongFistSkill extends Skill {
    public final IntProperty DURATION = new IntProperty(2, 5);
    public final IntProperty AMPLIFIER = new IntProperty(2, 1);

    public StrongFistSkill() {
        super(AbilitiesMod.ID, "strong_fist", Type.SUPPORT, Rarity.COMMON, new DoubleProperty(1.0), new IntProperty(1));
        icon();
    }

    @Override
    public boolean use(LivingEntity user, int level) {
        if (user.getWorld().isClient) return false;
        user.addStatusEffect(new StatusEffectInstance(ModEffects.STRONG_FIST, DURATION.get(level), AMPLIFIER.get(level)));
        return true;
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip",
                Text.literal(Utils.decimal("#.#", DURATION.get(level))).formatted(Formatting.GOLD),
                Text.literal(Utils.decimal("#.#", MP.get(level))).formatted(Formatting.GOLD)
        );
    }
}