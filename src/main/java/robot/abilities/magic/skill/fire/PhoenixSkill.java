package robot.abilities.magic.skill.fire;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import robot.abilities.AbilitiesMod;
import robot.abilities.effect.DormantPhoenixEffect;
import robot.abilities.effect.ModEffects;
import robot.abilities.magic.property.DoubleProperty;
import robot.abilities.magic.property.IntProperty;
import robot.abilities.magic.skill.Skill;
import robot.abilities.util.Utils;

public class PhoenixSkill extends Skill {
    public final IntProperty TIME = new IntProperty(200, 50);
    public final IntProperty AMPLIFIER = new IntProperty(0, 0.5);

    public PhoenixSkill() {
        super(AbilitiesMod.ID, "phoenix", Type.DEFEND, Rarity.EPIC, new DoubleProperty(20.0, 0.01), new IntProperty(10, 2));
    }

    public boolean use(LivingEntity user, int level) {
        if (user.getWorld().isClient) return false;
        StatusEffectInstance customEffect = new StatusEffectInstance(ModEffects.DORMANT_PHOENIX, TIME.get(level), AMPLIFIER.get(level));
        user.addStatusEffect(customEffect);
        return true;
    }

    @Override
    public void applyEventsHandler() {
        ServerLivingEntityEvents.ALLOW_DEATH.register(((entity, damageSource, damageAmount) -> {
            if (entity.hasStatusEffect(ModEffects.DORMANT_PHOENIX)) {
                DormantPhoenixEffect.applyEffect(entity, entity.getStatusEffect(ModEffects.DORMANT_PHOENIX).getAmplifier());
                entity.setHealth(entity.getMaxHealth());
                return false;
            }
            return true;
        }));
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip",
                Text.literal(Utils.decimal("#.#", TIME.get(level) / 20.0)).formatted(Formatting.GOLD),
                Text.literal(Utils.decimal(AMPLIFIER.get(level))).formatted(Formatting.GOLD),
                Text.literal(Utils.decimal(MP.get(level))).formatted(Formatting.GOLD));
    }
}
