package robot.abilities.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public interface LivingEntityTakeDamageCallback {
    Event<LivingEntityTakeDamageCallback> EVENT = EventFactory.createArrayBacked(LivingEntityTakeDamageCallback.class,
            (listeners) -> (entity, source, amount, isAllowed) -> {
                for (LivingEntityTakeDamageCallback listener : listeners) {
                    isAllowed &= listener.takeDamage(entity, source, amount, isAllowed);
                }
                return isAllowed;
            });

    boolean takeDamage(LivingEntity entity, DamageSource source, float amount, boolean isAllowed);
}
