package robot.abilities;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import robot.abilities.callback.CallbackManager;
import robot.abilities.item.ItemsInit;


public class AbilitiesMod implements ModInitializer {
	public static final String ID = "abilities";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	
	@Override
	public void onInitialize() {
		ItemsInit.register();
		CallbackManager.register();
	}


}