package robot.abilities.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import robot.abilities.client.screen.ModScreens;
import robot.abilities.magic.skill.ActiveSkills;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.Constants;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

public class SkillManagerC2SPackets {
    public static void open(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        //Only Server
        player.openHandledScreen(new SimpleNamedScreenHandlerFactory(((syncId, playerInventory, player1) -> ModScreens.PLAYER_SKILLS.create(syncId, playerInventory)), Text.empty()));
    }

    public static void change(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        //Only Server
        String newSkillID = buf.readString();
        String oldSkillID = buf.isReadable() ? buf.readString() : "";
        IPlayerMixin cap = (IPlayerMixin) player;
        if (newSkillID.isEmpty()) return;
        if (!SkillHelper.hasSkill(cap, newSkillID) || (!oldSkillID.isEmpty() && !SkillHelper.hasSkill(cap, oldSkillID)))
            return;
        ActiveSkills.replace(cap, oldSkillID, newSkillID);
        cap.sync();
        player.sendMessage(Text.literal("< Способность установлена >"), true);
    }

    public static void levelUp(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        //Only Server
        String skillName = buf.readString();
        if (skillName.isEmpty()) return;
        IPlayerMixin cap = (IPlayerMixin) player;
        Skill skill = SkillHelper.get(skillName);
        if (skill == null) return;
        int price = Constants.getRarityPrice(skill.getRarity());
        if (cap.get(DataKeys.POINTS) < price) return;
        cap.add(DataKeys.POINTS, -price);
        SkillHelper.upLevel(cap, skillName, 1);
        cap.sync();
    }
}
