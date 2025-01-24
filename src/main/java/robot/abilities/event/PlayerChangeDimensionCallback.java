package robot.abilities.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface PlayerChangeDimensionCallback {
    Event<PlayerChangeDimensionCallback> EVENT = EventFactory.createArrayBacked(PlayerChangeDimensionCallback.class,
            (listeners) -> (player, world) -> {
                for (PlayerChangeDimensionCallback listener : listeners) {
                    listener.changeDimension(player, world);
                }
            });
    void changeDimension(PlayerEntity player, World world);
}
