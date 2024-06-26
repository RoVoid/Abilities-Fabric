package robot.abilities.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.Iterator;
import java.util.Map;

public class DormantPhoenixEffect extends StatusEffect {
    public DormantPhoenixEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0xaa3333);
        addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", -0.5, EntityAttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public void onApplied(AttributeContainer attributeContainer, int amplifier) {
        for (Map.Entry<EntityAttribute, AttributeModifierCreator> entry : this.getAttributeModifiers().entrySet()) {
            EntityAttributeInstance entityAttributeInstance = attributeContainer.getCustomInstance(entry.getKey());
            if (entityAttributeInstance == null) continue;
            entityAttributeInstance.removeModifier(entry.getValue().getUuid());
            entityAttributeInstance.addPersistentModifier(entry.getValue().createAttributeModifier(0));
        }
    }

    public static void applyEffect(LivingEntity entity, int amplifier) {
        for (Iterator<StatusEffectInstance> iterator = entity.getStatusEffects().iterator(); iterator.hasNext();) {
            StatusEffectInstance effectInstance = iterator.next();
            StatusEffect effect = effectInstance.getEffectType();
            if (effect.getCategory() == StatusEffectCategory.HARMFUL) {
                iterator.remove();
            }
        }
        entity.removeStatusEffect(ModEffects.DORMANT_PHOENIX);
        entity.addStatusEffect(new StatusEffectInstance(ModEffects.PHOENIX, 200, amplifier));
    }
}
