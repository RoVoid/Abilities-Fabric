package robot.abilities.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

public interface PlayerTakeDamageCallback {
    Event<PlayerTakeDamageCallback> EVENT = EventFactory.createArrayBacked(PlayerTakeDamageCallback.class,
            (listeners) -> (player, source, amount) -> {
                for (PlayerTakeDamageCallback listener : listeners) {
                    if (!listener.takeDamage(player, source, amount)) return false;
                }
                return true;
            });

    boolean takeDamage(PlayerEntity player, DamageSource source, float amount);
}
