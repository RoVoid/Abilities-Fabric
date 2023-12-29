package robot.abilities.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import robot.abilities.AbilitiesMod;
import robot.abilities.util.IEntityDataSaver;

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
            for (String key : nbt.getKeys()) {
                String type = key.substring(0, key.indexOf(":")),
                        name = key.substring(key.indexOf(":") + 1);
                switch (type) {
                    case "bool" -> data.putBoolean(name, nbt.getBoolean(key));
                    case "int" -> data.putInt(name, nbt.getInt(key));
                    case "double" -> data.putDouble(name, nbt.getDouble(key));
                    case "string" -> data.putString(name, nbt.getString(key));
                    case "nbt" -> data.put(name, nbt.get(key));
                }
            }
            ((IEntityDataSaver) client.player).setPersistentData(data);
        }
        AbilitiesMod.LOGGER.info("Client Sync: " + ((IEntityDataSaver) client.player).getPersistentData().getDouble("mp"));
    }
}
