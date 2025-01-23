package robot.abilities.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.NbtCompound;
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
        NbtCompound nbt = buf.readNbt();
        String skillName = buf.readString();
        IPlayerMixin cap = (IPlayerMixin) player;
        ActiveSkills.setSkillsID(cap, nbt);
        if (skillName.isEmpty()) {
            ActiveSkills.updateIndex(cap, skillName);
            cap.sync();
            player.sendMessage(Text.literal(ActiveSkills.get(cap) != null ? "< Способность переустановлена >" : "< Способность не выбрана >"), true);
            return;
        }
        Skill skill = SkillHelper.get(skillName);
        if (skill == null || !skill.canPlayerUse(player)) return;
        ActiveSkills.updateIndex(cap, skillName);
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
