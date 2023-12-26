package robot.abilities.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class ModEvents {
    public static void register(){
        ServerTickEvents.START_SERVER_TICK.register(new PlayerTickEvent());
    }
}
