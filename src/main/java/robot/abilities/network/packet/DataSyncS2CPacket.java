package robot.abilities.network.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import robot.abilities.AbilitiesMod;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class DataSyncS2CPacket {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        //Only Client
        if (client.player == null) return;
        NbtCompound nbt = buf.readNbt();
        if (nbt == null) return;
        IPlayerMixin cap = ((IPlayerMixin) client.player);
        Map<String, DataKeys.Key> keys = DataKeys.getAll();
        for (String key : nbt.getKeys()) {
            if (key == null || keys.get(key) == null) continue;
            Object obj = DataKeys.get(nbt, keys.get(key));
            if (obj != null) cap.put(keys.get(key), obj);
        }
    }
}
