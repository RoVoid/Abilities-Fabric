package robot.abilities;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import robot.abilities.event.ModEvents;
import robot.abilities.item.ModItems;
import robot.abilities.network.ModMessages;


public class AbilitiesMod implements ModInitializer {
    public static final String ID = "abilities";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    @Override
    public void onInitialize() {
        ModItems.register();
        ModEvents.register();
        ModMessages.registerS2CPackets();
    }


}