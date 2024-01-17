package robot.abilities.event;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import robot.abilities.client.ManaBarOverlay;

public class ModEvents {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(new PlayerEvents());
        ServerPlayConnectionEvents.INIT.register(new PlayerEvents());
        ServerLivingEntityEvents.AFTER_DEATH.register(new EntityEvents());
        ServerMessageEvents.CHAT_MESSAGE.register(new ChatEvents());
    }

    public static void registerClient() {
        HudRenderCallback.EVENT.register(new ManaBarOverlay());
    }
}
