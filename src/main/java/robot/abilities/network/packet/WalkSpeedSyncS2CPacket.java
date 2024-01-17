package robot.abilities.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import robot.abilities.AbilitiesMod;
import robot.abilities.util.IPlayerMixin;

public class WalkSpeedSyncS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        //Only Client
        float speed = buf.readFloat();
        if (client.player == null) return;
        AbilitiesMod.LOGGER.info("Client " + speed);
        client.player.getAbilities().setWalkSpeed(client.player.getAbilities().getWalkSpeed() + speed);
        client.player.sendAbilitiesUpdate();
        ((IPlayerMixin) client.player).getPersistentData().putFloat("walk_speed", Math.max(0, speed));
    }
}
