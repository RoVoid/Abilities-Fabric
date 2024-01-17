package robot.abilities.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import robot.abilities.client.screen.ModScreens;
import robot.abilities.magic.ModMagics;
import robot.abilities.magic.skill.AbstractSkill;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

public class SkillManagerC2SPackets {
    public static void open(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        //Only Server
        player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> ModScreens.SKILL_MANAGER.create(i, playerInventory), Text.of("My GUI")));
    }

    public static void change(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        //Only Server
        String skillName = buf.readString();
        if (skillName.isEmpty()) return;
        IPlayerMixin cap = (IPlayerMixin) player;
        AbstractSkill skill = ModMagics.getSkill(cap.get(DataKeys.MAGIC), skillName);
        if (skill == null || !skill.canUse(player)) return;
        cap.put(DataKeys.SKILL, skillName);
        cap.sync(DataKeys.SKILL);
        player.sendMessage(Text.literal("Способность установлена"), true);
    }
}
