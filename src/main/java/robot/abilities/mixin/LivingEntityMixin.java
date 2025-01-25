package robot.abilities.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robot.abilities.event.LivingEntityTakeDamageCallback;
import robot.abilities.event.PlayerTakeDamageCallback;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "damage", at = @At(value = "TAIL"), cancellable = true)
    private void onTakeDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        cir.setReturnValue(LivingEntityTakeDamageCallback.EVENT.invoker().takeDamage(entity, source, amount, cir.getReturnValue()));
        if (entity instanceof PlayerEntity player) {
            PlayerTakeDamageCallback.EVENT.invoker().takeDamage(player, source, amount, cir.getReturnValue());
        }
    }
}
