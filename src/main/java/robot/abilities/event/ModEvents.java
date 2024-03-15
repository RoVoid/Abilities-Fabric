package robot.abilities.event;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import robot.abilities.client.ManaBarOverlay;

public class ModEvents {
    public static void register() {
        ServerMessageEvents.CHAT_MESSAGE.register(new ChatEvents());
        ServerTickEvents.END_SERVER_TICK.register(new PlayerEvents());
        ServerPlayerEvents.AFTER_RESPAWN.register(new PlayerEvents());
        ServerPlayerEvents.COPY_FROM.register(new PlayerEvents());
        ServerPlayConnectionEvents.JOIN.register(new PlayerEvents());
        PlayerBlockBreakEvents.BEFORE.register(new PlayerEvents());
        UseItemCallback.EVENT.register(new ItemEvents());
        ItemTooltipCallback.EVENT.register(new ItemEvents());
    }

    public static void registerClient() {
        HudRenderCallback.EVENT.register(new ManaBarOverlay());
        //context.worldRenderer().
        //  WorldRenderEvents.END.register(DesaturationRenderer::renderWorld);
        //  WorldRenderEvents.START.register(DesaturationRenderer::renderWorld);
    }
}
