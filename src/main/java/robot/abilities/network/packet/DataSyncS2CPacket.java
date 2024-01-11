package robot.abilities.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IEntityDataSaver;

import java.util.HashMap;
import java.util.Map;

public class DataSyncS2CPacket {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        //Only Client
        if (client.player == null) return;
        String func = buf.readString();
        NbtCompound nbt = buf.readNbt();
        if (nbt == null) return;
        if (func.equals("all")) {
            ((IEntityDataSaver) client.player).setPersistentData(nbt);
        } else if (func.equals("replace")) {
            NbtCompound data = ((IEntityDataSaver) client.player).getPersistentData();
            Map<String, DataKeys.Key> keys = new HashMap<>();
            DataKeys.getAll().forEach((key) -> keys.put(key.getName(), key));
            for (String key : nbt.getKeys()) {
                if (key == null) continue;
                DataKeys.put(data, keys.get(key), DataKeys.get(nbt, keys.get(key)));
            }
            ((IEntityDataSaver) client.player).setPersistentData(data);
        }
    }
}
