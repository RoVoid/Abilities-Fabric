package robot.abilities.event;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import robot.abilities.client.ManaBarOverlay;
import robot.abilities.command.ModCommands;

public class ModEvents {
    public static void register() {
        ServerMessageEvents.CHAT_MESSAGE.register(new ChatEvents());
        ServerTickEvents.END_SERVER_TICK.register(new PlayerEvents());
        ServerPlayerEvents.AFTER_RESPAWN.register(new PlayerEvents());
        ServerPlayerEvents.COPY_FROM.register(new PlayerEvents());
        ServerPlayConnectionEvents.JOIN.register(new PlayerEvents());
        ClientPlayConnectionEvents.JOIN.register(new PlayerEvents());
        PlayerBlockBreakEvents.BEFORE.register(new PlayerEvents());
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ModCommands.register(dispatcher));
    }

    public static void registerClient() {
        HudRenderCallback.EVENT.register(new ManaBarOverlay());
    }
}
