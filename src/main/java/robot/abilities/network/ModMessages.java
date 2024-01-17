package robot.abilities.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;
import robot.abilities.network.packet.*;

public class ModMessages {
    public static final Identifier DATA_SYNC = new Identifier(AbilitiesMod.ID, "player_data_sync");
    public static final Identifier SKILL_USE = new Identifier(AbilitiesMod.ID, "skill_use");
    public static final Identifier SKILL_MANAGER = new Identifier(AbilitiesMod.ID, "skill_manager");
    public static final Identifier SKILL_MANAGER_CHANGE = new Identifier(AbilitiesMod.ID, "skill_manager_change");
    public static final Identifier WALK_SPEED_SYNC = new Identifier(AbilitiesMod.ID, "walk_speed_sync");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(SKILL_USE, SkillUseC2SPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(SKILL_MANAGER, SkillManagerC2SPackets::open);
        ServerPlayNetworking.registerGlobalReceiver(SKILL_MANAGER_CHANGE, SkillManagerC2SPackets::change);
        ServerPlayNetworking.registerGlobalReceiver(WALK_SPEED_SYNC, WalkSpeedSyncC2SPacket::receive);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(DATA_SYNC, DataSyncS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(WALK_SPEED_SYNC, WalkSpeedSyncS2CPacket::receive);
    }
}
