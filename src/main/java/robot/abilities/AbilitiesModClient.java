package robot.abilities;

import net.fabricmc.api.ClientModInitializer;
import robot.abilities.network.ModMessages;

public class AbilitiesModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModMessages.registerC2SPackets();
	}
}