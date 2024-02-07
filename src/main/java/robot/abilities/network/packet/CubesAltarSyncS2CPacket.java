package robot.abilities.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import robot.abilities.block.CubesAltarBlock;
import robot.abilities.block.blockentity.CubesAltarBlockEntity;

public class CubesAltarSyncS2CPacket {
    public static void item(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        //Only Client
        ItemStack item = buf.readItemStack();
        BlockPos pos = buf.readBlockPos();
        if (client.player.getWorld().getBlockEntity(pos) instanceof CubesAltarBlockEntity entity) {
            entity.setRenderItem(item);
        }
    }

    public static void particle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        //Only Client
        BlockPos pos = buf.readBlockPos();
        if (client.player.getWorld().getBlockState(pos).getBlock() instanceof CubesAltarBlock block) {
            block.onClient(client.player.getWorld(), pos, client.player);
        }
    }
}
