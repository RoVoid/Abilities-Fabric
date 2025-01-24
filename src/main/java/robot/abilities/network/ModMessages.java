package robot.abilities.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;

public class ModMessages {
    public static final Identifier DATA_SYNC = new Identifier(AbilitiesMod.ID, "player_data_sync");
    public static final Identifier SKILL_USE = new Identifier(AbilitiesMod.ID, "skill_use");
    public static final Identifier SKILL_USE_ON_CLIENT = new Identifier(AbilitiesMod.ID, "skill_use_on_client");
    public static final Identifier SKILL_CHANGE = new Identifier(AbilitiesMod.ID, "skill_change");
    public static final Identifier SKILL_MANAGER = new Identifier(AbilitiesMod.ID, "skill_manager");
    public static final Identifier SKILL_MANAGER_CHANGE = new Identifier(AbilitiesMod.ID, "skill_manager_change");
    public static final Identifier SKILL_MANAGER_UP = new Identifier(AbilitiesMod.ID, "skill_manager_up");
    public static final Identifier BEACON = new Identifier(AbilitiesMod.ID, "beacon");
    public static final Identifier MAGIC_CIRCLE_SYNC = new Identifier(AbilitiesMod.ID, "magic_circle_sync");
    public static final Identifier ALTAR_SYNC = new Identifier(AbilitiesMod.ID, "altar_sync");
    public static final Identifier ALTAR_PARTICLE = new Identifier(AbilitiesMod.ID, "altar_particle");
    public static final Identifier RENDER_FLOATING_ITEM = new Identifier(AbilitiesMod.ID, "render_floating_item");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(DATA_SYNC, DataSyncS2CPacket::request);
        ServerPlayNetworking.registerGlobalReceiver(SKILL_USE, SkillUseC2SPacket::use);
        ServerPlayNetworking.registerGlobalReceiver(SKILL_CHANGE, SkillUseC2SPacket::change);
        ServerPlayNetworking.registerGlobalReceiver(SKILL_MANAGER, SkillManagerC2SPackets::open);
        ServerPlayNetworking.registerGlobalReceiver(SKILL_MANAGER_CHANGE, SkillManagerC2SPackets::change);
        ServerPlayNetworking.registerGlobalReceiver(SKILL_MANAGER_UP, SkillManagerC2SPackets::levelUp);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(DATA_SYNC, DataSyncS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(SKILL_USE_ON_CLIENT, SkillUseS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(MAGIC_CIRCLE_SYNC, MagicCircleSyncS2CPacket::item);
        ClientPlayNetworking.registerGlobalReceiver(ALTAR_SYNC, CubesAltarSyncS2CPacket::item);
        ClientPlayNetworking.registerGlobalReceiver(ALTAR_PARTICLE, CubesAltarSyncS2CPacket::particle);
        ClientPlayNetworking.registerGlobalReceiver(RENDER_FLOATING_ITEM, RenderFloatingItemS2CPacket::receive);
    }
}
