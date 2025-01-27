package robot.abilities.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robot.abilities.event.PlayerTakeDamageCallback;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "damage", at = @At(value = "HEAD"), cancellable = true)
    private void onTakeDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (!PlayerTakeDamageCallback.EVENT.invoker().takeDamage(player, source, amount)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}



