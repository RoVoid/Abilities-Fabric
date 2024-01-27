package robot.abilities.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import robot.abilities.magic.skill.AbstractSkill;
import robot.abilities.magic.skill.MainSkills;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

public class SkillUseC2SPacket {
    public static void use(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        //Only Server
        IPlayerMixin cap = (IPlayerMixin) player;
        if (cap.get(DataKeys.COOLDOWN) > 0) return;
        String skillName = buf.readString();
        int pressed = buf.readInt();
        AbstractSkill skill = skillName.isEmpty() ? MainSkills.get(cap) : SkillHelper.getSkill(skillName);
        if (skill != null) {
            int level = SkillHelper.getData(cap, skill.getID(), SkillHelper.Keys.LEVEL);
            level = player.isCreative() ? level : (int) Math.floor(level * Math.min(1, pressed / skill.get("castTime", level)));
            skill.usePlayer(player, level);
            cap.put(DataKeys.COOLDOWN, 5);
            cap.sync(DataKeys.COOLDOWN);
        }
    }

    public static void change(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        //Only Server
        IPlayerMixin cap = (IPlayerMixin) player;
        if (cap.get(DataKeys.COOLDOWN) > 0) return;
        boolean dir = buf.readBoolean();
        int size = MainSkills.getSkillNames(cap).contains("") ? MainSkills.getSkillNames(cap).indexOf("") : MainSkills.getSkillNames(cap).size();
        int index = (cap.get(DataKeys.SKILL) + (dir ? -1 : 1) + size) % size;
        cap.put(DataKeys.SKILL, index);
        cap.put(DataKeys.COOLDOWN, 5);
        player.sendMessage(Text.literal("< %s §r>".formatted(Text.translatable(MainSkills.get(cap).getTranslateKey()).getString())), true);
        cap.sync(false);
    }
}
