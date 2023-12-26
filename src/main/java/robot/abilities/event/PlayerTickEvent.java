package robot.abilities.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import robot.abilities.AbilitiesMod;
import robot.abilities.item.armor.ModArmors;

public class PlayerTickEvent implements ServerTickEvents.StartTick {
    private static final float DEFAULT_WALK_SPEED = 0.1f;
    private static final float INCREASED_WALK_SPEED = 1f;
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

    @Override
    public void onStartTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            boolean flag = player.getInventory().getArmorStack(3).getItem() == ModArmors.MITHRIL_ARMOR.HELMET && player.getInventory().getArmorStack(2).getItem() == ModArmors.MITHRIL_ARMOR.CHESTPLATE && player.getInventory().getArmorStack(1).getItem() == ModArmors.MITHRIL_ARMOR.LEGGINGS && player.getInventory().getArmorStack(0).getItem() == ModArmors.MITHRIL_ARMOR.BOOTS;
            if (flag) {
                increaseWalkSpeed(player);
            } else {
                resetWalkSpeed(player);
            }
        }
    }
}
