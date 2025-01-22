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

public class PhoenixSkill extends Skill {
    public PhoenixSkill() {
        super(AbilitiesMod.ID, "phoenix", Type.DEFEND, Rarity.EPIC, Property.of(20.0, 0.01), Property.of(10, 2));
        add("time", Property.of(200, 50));
        add("amplifier", Property.of(0, 0.5));
    }

    public boolean use(LivingEntity user, int level) {
        if (user.getWorld().isClient) return false;
        StatusEffectInstance customEffect = new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, get("time", level), get("amplifier", level));
        user.addStatusEffect(customEffect);
        return true;
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip",
                Text.literal(Utils.decimal("#", getInt("time", level) / 20.0)).formatted(Formatting.GOLD), Text.literal(Utils.decimal(getInt("amplifier", level))).formatted(Formatting.GOLD), Text.literal(Utils.decimal(get("mp", level))).formatted(Formatting.GOLD));
    }
}
