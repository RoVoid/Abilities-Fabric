package robot.abilities.event;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import robot.abilities.client.ManaBarOverlay;

public class ModEvents {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(new PlayerEvents());
        ServerLivingEntityEvents.AFTER_DEATH.register(new EntityEvents());
        ServerMessageEvents.CHAT_MESSAGE.register(new ChatEvents());
        UseItemCallback.EVENT.register(new ItemEvents());
    }

    public static void registerClient() {
        HudRenderCallback.EVENT.register(new ManaBarOverlay());
    }
}
