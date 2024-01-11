package robot.abilities.mixin;

import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robot.abilities.item.ModArmors;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {
    @Inject(method = "canWalkOnPowderSnow", at = @At("RETURN"), cancellable = true)
    private static void canWalkOnPowderSnow(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        Boolean r = cir.getReturnValue();
        if (entity instanceof LivingEntity) {
            cir.setReturnValue(r || ((LivingEntity) entity).getEquippedStack(EquipmentSlot.FEET).isOf(ModArmors.MITHRIL_ARMOR.BOOTS));
        }
    }
}

