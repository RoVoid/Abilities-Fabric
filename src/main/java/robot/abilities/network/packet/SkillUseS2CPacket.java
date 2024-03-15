package robot.abilities.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.SkillHelper;

public class SkillUseS2CPacket {
    public static void use(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        //Only Client
        boolean isPlayer = buf.readBoolean();
        LivingEntity entity = isPlayer ? client.world.getPlayerByUuid(buf.readUuid()) : (LivingEntity) client.world.getEntityById(buf.readInt());
        String skillName = buf.readString();
        int level = buf.readInt();
        if (entity == null || skillName.isEmpty()) return;
        Skill skill = SkillHelper.get(skillName);
        if (skill != null) {
            skill.onClient(entity, level);
        }
    }
}
