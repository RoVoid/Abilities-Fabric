package robot.abilities.magic.skill.fire;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import robot.abilities.AbilitiesMod;
import robot.abilities.event.PlayerTakeDamageCallback;
import robot.abilities.magic.property.Property;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;
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
    public void applyEventsHandler() {
        PlayerTakeDamageCallback.EVENT.register((player, source, amount) -> {
            if (player.getWorld().isClient || !source.isOf(DamageTypes.IN_FIRE)) return true;
            IPlayerMixin cap = (IPlayerMixin) player;
            if (SkillHelper.hasSkill(cap, id()) || !SkillHelper.includesSkill(cap, id())) return true;
            cap.add(DataKeys.ARGS, id() + ":in_fire", amount);
            if (cap.get(DataKeys.ARGS).getFloat(id() + ":in_fire") > 10) {
                cap.get(DataKeys.ARGS).remove(id() + ":in_fire");
                SkillHelper.upLevel(cap, id(), 1);
                player.sendMessage(getDisplayName().append(" is received"));
                player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1, 1);
            }
            cap.sync();
            return true;
        });
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip", Text.literal(Utils.decimal("#.#", getInt("time", level) / 20)).formatted(Formatting.GOLD), Text.literal(Utils.decimal("#.#", getDouble("mp", level))).formatted(Formatting.GOLD));
    }
}
