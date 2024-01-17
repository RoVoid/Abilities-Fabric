package robot.abilities.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import robot.abilities.network.ModMessages;

public class WalkSpeedData {

    public static void add(IPlayerMixin player, float speed) {
        NbtCompound nbt = player.getPersistentData();
        nbt.putFloat("walkSpeed", speed);
        sync(speed, (ServerPlayerEntity) player);
    }

    public static void sync(float speed, ServerPlayerEntity player) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeFloat(speed);
        ServerPlayNetworking.send(player, ModMessages.WALK_SPEED_SYNC, buffer);
    }
}
