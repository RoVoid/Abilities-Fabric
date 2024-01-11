package robot.abilities;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import robot.abilities.block.ModBlocks;
import robot.abilities.client.screen.ModScreens;
import robot.abilities.entity.ModEntities;
import robot.abilities.event.KetInputHandler;
import robot.abilities.event.ModEvents;
import robot.abilities.network.ModMessages;

@Environment(EnvType.CLIENT)
public class AbilitiesModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModMessages.registerC2SPackets();
        KetInputHandler.register();
        ModBlocks.registerRender();
        ModEntities.registerRender();
        ModEvents.registerClient();
        ModScreens.register();
    }
}