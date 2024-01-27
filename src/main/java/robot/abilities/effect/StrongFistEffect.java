package robot.abilities.effect;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class StrongFistEffect extends StatusEffect {
    public StrongFistEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0x33aa33);
        addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 3.0, EntityAttributeModifier.Operation.ADDITION);
        addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF3", 2.7f, EntityAttributeModifier.Operation.ADDITION);
    }
}
