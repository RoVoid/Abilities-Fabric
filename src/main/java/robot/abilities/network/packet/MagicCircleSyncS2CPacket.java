package robot.abilities.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import robot.abilities.block.blockentity.MagicCircleBlockEntity;

public class MagicCircleSyncS2CPacket {
    public static void item(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        //Only Client
        BlockPos pos = buf.readBlockPos();
        if (client.player.getWorld().getBlockEntity(pos) instanceof MagicCircleBlockEntity entity) {
            int size = buf.readInt();
            for (int i = 0; i < size; i++) {
                entity.setStack(i, buf.readItemStack());
            }
        }
    }
}
