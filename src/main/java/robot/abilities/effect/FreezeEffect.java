package robot.abilities.effect;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.Vec3d;
import robot.abilities.AbilitiesMod;

public class FreezeEffect extends StatusEffect {
    public FreezeEffect() {
        super(StatusEffectCategory.HARMFUL, 0xaaaaaa);
        AbilitiesMod.LOGGER.info("apply1");
        addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", -1.0, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF3", -1, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF4", -1, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(EntityAttributes.GENERIC_FOLLOW_RANGE, "AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF4", -1, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        AbilitiesMod.LOGGER.info("apply2");
        entity.slowMovement(Blocks.POWDER_SNOW.getDefaultState(), new Vec3d(0.9f, 1.5, 0.9f));
        entity.setInPowderSnow(true);
    }
}
