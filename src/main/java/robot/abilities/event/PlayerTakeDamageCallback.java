package robot.abilities.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;

public interface PlayerTakeDamageCallback {
    Event<PlayerTakeDamageCallback> EVENT = EventFactory.createArrayBacked(PlayerTakeDamageCallback.class,
            (listeners) -> (player, source, amount, isAllowed) -> {
                for (PlayerTakeDamageCallback listener : listeners) {
                    listener.takeDamage(player, source, amount, isAllowed);
                }
            });

    void takeDamage(PlayerEntity player, DamageSource source, float amount, boolean isAllowed);
}
