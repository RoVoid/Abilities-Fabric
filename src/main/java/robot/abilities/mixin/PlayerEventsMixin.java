package robot.abilities.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robot.abilities.event.PlayerChangeDimensionCallback;

@Mixin(Entity.class)
public class PlayerEventsMixin{
    @Inject(method = "moveToWorld(Lnet/minecraft/server/world/ServerWorld;)Lnet/minecraft/entity/Entity;", at = @At(value = "TAIL"))
    private void onPlayerChangeDimension(ServerWorld world, CallbackInfoReturnable<Entity> cir) {
        if(cir.getReturnValue() instanceof PlayerEntity player){
            PlayerChangeDimensionCallback.EVENT.invoker().changeDimension(player, world);
        }
    }
}
