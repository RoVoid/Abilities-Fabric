package robot.abilities.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import robot.abilities.AbilitiesMod;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.util.Map;

public class DataSyncS2CPacket {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        //Only Client
        if (client.player == null) return;
        NbtCompound nbt = buf.readNbt();
        if (nbt == null) return;
        IPlayerMixin cap = ((IPlayerMixin) client.player);
        Map<String, DataKeys.Key> keys = DataKeys.getAll();
        for (String key : nbt.getKeys()) {
            if (!keys.containsKey(key)) continue;
            //AbilitiesMod.LOGGER.info("{} {}", key, DataKeys.get(nbt, keys.get(key)));
            cap.put(keys.get(key), DataKeys.get(nbt, keys.get(key)));
        }
        AbilitiesMod.LOGGER.info("Updated Data Keys {}", nbt.getKeys());
    }

    public static void request(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        //Only Server
        if (player != null) ((IPlayerMixin) player).fullSync();
    }
}
