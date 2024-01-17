package robot.abilities.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import robot.abilities.magic.ModMagics;
import robot.abilities.magic.skill.AbstractSkill;
import robot.abilities.util.IPlayerMixin;
import robot.abilities.util.DataKeys;

public class SkillUseC2SPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        //Only Server
        IPlayerMixin cap = (IPlayerMixin) player;
        if(cap.get(DataKeys.COOLDOWN) > 0) return;
        AbstractSkill skill = ModMagics.getSkill(cap.get(DataKeys.MAGIC), cap.get(DataKeys.SKILL));
        if (skill != null) {
            skill.use(player, cap.get(DataKeys.SKILLS).getInt(cap.get(DataKeys.SKILL)));
            cap.add(DataKeys.SCORE, 1);
            cap.put(DataKeys.COOLDOWN, 5);
            cap.sync(DataKeys.SCORE, DataKeys.COOLDOWN);
        }
    }
}
