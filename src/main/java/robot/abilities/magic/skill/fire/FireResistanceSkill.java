package robot.abilities.magic.skill.fire;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.property.Property;
import robot.abilities.magic.skill.Skill;
import robot.abilities.util.Utils;

public class FireResistanceSkill extends Skill {
    public FireResistanceSkill() {
        super(AbilitiesMod.ID, "fire_resistance", Type.DEFEND, Rarity.COMMON, Property.of(3.0, 0.01), Property.of(1, 2));
        add("time", Property.of(40, 20));
        icon();
    }

    public boolean use(LivingEntity user, int level) {
        if (user.getWorld().isClient) return false;
        StatusEffectInstance customEffect = new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, get("time", level), 0);
        user.addStatusEffect(customEffect);
        return true;
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip", Text.literal(Utils.decimal("#.#", getInt("time", level) / 20)).formatted(Formatting.GOLD), Text.literal(Utils.decimal("#.#", getDouble("mp", level))).formatted(Formatting.GOLD));
    }
}
