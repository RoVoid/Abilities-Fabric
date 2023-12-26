package robot.abilities.callback;

import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import robot.abilities.item.armor.ArmorsInit;

public class CallbackManager {
    public static void tickCallback(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            boolean flag = player.getInventory().getArmorStack(3).getItem() == ArmorsInit.MITHRIL_ARMOR.HELMET && player.getInventory().getArmorStack(2).getItem() == ArmorsInit.MITHRIL_ARMOR.CHESTPLATE && player.getInventory().getArmorStack(1).getItem() == ArmorsInit.MITHRIL_ARMOR.LEGGINGS && player.getInventory().getArmorStack(0).getItem() == ArmorsInit.MITHRIL_ARMOR.BOOTS;
            if (flag) {
                PlayerAbilitiesManager.increaseWalkSpeed(player);
            } else {
                PlayerAbilitiesManager.resetWalkSpeed(player);
            }
        }

    }

    public static void register() {
        ServerTickCallback.EVENT.register(CallbackManager::tickCallback);
    }
}
