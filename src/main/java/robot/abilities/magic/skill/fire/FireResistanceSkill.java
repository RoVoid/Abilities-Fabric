package robot.abilities.magic.skill.fire;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.text.DecimalFormat;

public class FireResistanceSkill extends Skill {
    public FireResistanceSkill() {
        super(AbilitiesMod.ID + ".fire_resistance", Type.DEFEND, new Property(3, 0.01), new Property(1), new Property(1, 2));
        add("time", new Property(40, 20));
    }

    public boolean use(LivingEntity user, int level) {
        if (user.getWorld().isClient) return false;
        StatusEffectInstance customEffect = new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, (int) get("time", level), 0);
        user.addStatusEffect(customEffect);
        return true;
    }

    @Override
    public void usePlayer(PlayerEntity player, int level) {
        if(!canUse(player, level) || player.getWorld().isClient) return;
        if (!use(player, level)) return;
        double mp = get("mp", level);
        IPlayerMixin cap = (IPlayerMixin) player;
        cap.add(DataKeys.POINTS, 5);
        SkillHelper.addExperience(cap, this, 5);
        cap.add(DataKeys.MANA, -mp);
        cap.sync(false);
    }

    @Override
    public boolean canUse(PlayerEntity player, int level) {
        return super.canUse(player, level) && ((IPlayerMixin) player).get(DataKeys.MANA) >= get("mp", level);
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip", Text.literal(new DecimalFormat("#.#").format(get("time", level) / 20)).formatted(Formatting.GOLD), Text.literal(new DecimalFormat("#.#").format(get("mp", level))).formatted(Formatting.GOLD));
    }
}
