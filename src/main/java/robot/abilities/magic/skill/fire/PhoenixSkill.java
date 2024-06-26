package robot.abilities.magic.skill.fire;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.property.Property;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;
import robot.abilities.util.Utils;

public class PhoenixSkill extends Skill {
    public PhoenixSkill() {
        super(AbilitiesMod.ID, "phoenix", Type.DEFEND, Rarity.EPIC, Property.of(20.0, 0.01), Property.of(10, 2));
        add("time", Property.of(200, 50));
        add("amplifier", Property.of(0, 0.5));
    }

    public boolean use(LivingEntity user, int level) {
        if (user.getWorld().isClient) return false;
        StatusEffectInstance customEffect = new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, (int) get("time", level), (int) get("amplifier", level));
        user.addStatusEffect(customEffect);
        return true;
    }

    @Override
    public void usePlayer(PlayerEntity player, int level) {
        if (!canPlayerUse(player, level) || player.getWorld().isClient) return;
        if (!use(player, level)) return;
        double mp = get("mp", level);
        IPlayerMixin cap = (IPlayerMixin) player;
        SkillHelper.addExperience(cap, this, 5);
        cap.add(DataKeys.MANA, -mp);
        cap.sync(false);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player, int level) {
        return super.canPlayerUse(player, level) && ((IPlayerMixin) player).get(DataKeys.MANA) >= get("mp", level);
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip", Text.literal(Utils.decimal("#", get("time", level) / 20)).formatted(Formatting.GOLD), Text.literal(Utils.decimal(get("time", level) / 20)).formatted(Formatting.GOLD), Text.literal(Utils.decimal(get("mp", level))).formatted(Formatting.GOLD));
    }
}
