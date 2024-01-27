package robot.abilities;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import robot.abilities.block.ModBlockEntities;
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
        KetInputHandler.register();
        ModBlocks.registerRender();
        ModBlockEntities.registerRender();
        ModEntities.registerRender();
        ModEvents.registerClient();
        ModMessages.registerS2CPackets();
        ModScreens.register();
    }
}