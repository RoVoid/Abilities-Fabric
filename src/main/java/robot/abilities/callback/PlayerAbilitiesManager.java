package robot.abilities.callback;

import net.minecraft.server.network.ServerPlayerEntity;
import robot.abilities.AbilitiesMod;

public class PlayerAbilitiesManager {
    private static final float DEFAULT_WALK_SPEED = 0.1f;
    private static final float INCREASED_WALK_SPEED = 0.3f;

    private static boolean isWalkSpeedIncreased = false;

    public static void increaseWalkSpeed(ServerPlayerEntity player) {
        if (!isWalkSpeedIncreased) {
            player.getAbilities().setWalkSpeed(INCREASED_WALK_SPEED);
            player.sendAbilitiesUpdate();
            isWalkSpeedIncreased = true;
            AbilitiesMod.LOGGER.info("Walk speed increased: " + INCREASED_WALK_SPEED);
        }
    }

    public static void resetWalkSpeed(ServerPlayerEntity player) {
        if (isWalkSpeedIncreased) {
            player.getAbilities().setWalkSpeed(DEFAULT_WALK_SPEED);
            player.sendAbilitiesUpdate();
            isWalkSpeedIncreased = false;
            AbilitiesMod.LOGGER.info("Walk speed reset to default: " + DEFAULT_WALK_SPEED);
        }
    }

    public static boolean isWalkSpeedIncreased() {
        return isWalkSpeedIncreased;
    }
}
