package robot.abilities.magic.skill.earth;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import robot.abilities.AbilitiesMod;
import robot.abilities.effect.ModEffects;
import robot.abilities.magic.property.Property;
import robot.abilities.magic.skill.Skill;
import robot.abilities.util.Utils;

public class StrongFistSkill extends Skill {
    public StrongFistSkill() {
        super(AbilitiesMod.ID, "strong_fist", Type.SUPPORT, Rarity.COMMON, Property.of(1.0), Property.of(1));
        add("duration", Property.of(2, 5));
        add("amplifier", Property.of(2, 1));
        icon();
    }

    @Override
    public boolean use(LivingEntity user, int level) {
        if (user.getWorld().isClient) return false;
        user.addStatusEffect(new StatusEffectInstance(ModEffects.STRONG_FIST, (int) Math.floor(get("duration", level)), (int) Math.floor(get("amplifier", level))));
        return true;
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip",
                Text.literal(Utils.decimal("#.#", get("golem", level))).formatted(Formatting.GOLD),
                Text.literal(Utils.decimal("#.#", get("mp", level))).formatted(Formatting.GOLD)
        );
    }
}