package robot.abilities.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.potion.Potions;
import net.minecraft.predicate.entity.EntityEffectPredicate;
import net.minecraft.world.biome.BiomeEffects;
import robot.abilities.AbilitiesMod;
import robot.abilities.util.IEntityDataSaver;

public class WalkSpeedSyncS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        //Only Client
        float speed = buf.readFloat();
        if (client.player == null) return;
        AbilitiesMod.LOGGER.info("Client " + speed);
        client.player.getAbilities().setWalkSpeed(client.player.getAbilities().getWalkSpeed() + speed);
        client.player.sendAbilitiesUpdate();
        ((IEntityDataSaver) client.player).getPersistentData().putFloat("walk_speed", Math.max(0, speed));
    }
}
