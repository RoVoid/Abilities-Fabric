package robot.abilities.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.skill.ActiveSkills;
import robot.abilities.magic.skill.Skill;
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
        Skill skill = skillName.isEmpty() ? ActiveSkills.get(cap) : SkillHelper.get(skillName);
        if (skill != null) {
            int level = skill.getUsefulLevel(cap, pressed);
            AbilitiesMod.LOGGER.info(player.getName().getString() + " use " + skill.getName() + "#" + level);
            skill.usePlayer(player, level);
            cap.put(DataKeys.COOLDOWN, 5);
            cap.sync(DataKeys.COOLDOWN);
        }
    }

    public static void change(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        //Only Server
        IPlayerMixin cap = (IPlayerMixin) player;
        if (cap.get(DataKeys.MAGIC).isEmpty() || cap.get(DataKeys.COOLDOWN) > 0) return;
        boolean dir = buf.readBoolean();
        int size = ActiveSkills.getSize(cap);
        if (size <= 0) return;
        int index = cap.get(DataKeys.SKILL);
        do {
            index = (index + (dir ? -1 : 1) + size) % size;
        }
        while (ActiveSkills.get(cap, index) == null);
        if (cap.get(DataKeys.SKILL) != index) ActiveSkills.updateIndex(cap, index);
        cap.put(DataKeys.COOLDOWN, 5);
        player.sendMessage(Text.literal("< %s §r>".formatted(Text.translatable(ActiveSkills.get(cap).getTranslateKey()).getString())), true);
        cap.sync(false);
    }
}
