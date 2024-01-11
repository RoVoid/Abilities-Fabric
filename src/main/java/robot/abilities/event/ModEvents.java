package robot.abilities.event;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import robot.abilities.client.ManaBarOverlay;

public class ModEvents {
    public static void register(){
        ServerTickEvents.START_SERVER_TICK.register(new PlayerTickEvent());
    }
    public static void registerClient(){
        HudRenderCallback.EVENT.register(new ManaBarOverlay());
    }
}
