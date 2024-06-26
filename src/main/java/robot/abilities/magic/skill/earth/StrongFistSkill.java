package robot.abilities.magic.skill.earth;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import robot.abilities.AbilitiesMod;
import robot.abilities.effect.ModEffects;
import robot.abilities.magic.property.Property;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.text.DecimalFormat;

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
        user.addStatusEffect(new StatusEffectInstance(ModEffects.STRONG_FIST, (int)Math.floor(get("duration", level)), (int)Math.floor(get("amplifier", level))));
        return true;
    }

    @Override
    public void usePlayer(PlayerEntity player, int level) {
        if (!canPlayerUse(player, level) || player.getWorld().isClient) return;
        if (!use(player, level)) return;
        double mp = get("mp", level);
        IPlayerMixin cap = (IPlayerMixin) player;
        cap.add(DataKeys.MANA, -mp);
        cap.add(DataKeys.POINTS, 5);
        SkillHelper.addExperience(cap, this, 5);
        cap.sync(false);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player, int level) {
        return super.canPlayerUse(player, level) && ((IPlayerMixin) player).get(DataKeys.MANA) >= get("mp", level);
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip", Text.literal(new DecimalFormat("#.#").format(get("golem", level))).formatted(Formatting.GOLD), Text.literal(new DecimalFormat("#.#").format(get("mp", level))).formatted(Formatting.GOLD));
    }
}