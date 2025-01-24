package robot.abilities.event;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import robot.abilities.AbilitiesMod;
import robot.abilities.effect.ModEffects;
import robot.abilities.item.ModArmors;
import robot.abilities.network.ModMessages;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;
import robot.abilities.util.Utils;

import java.awt.event.ItemEvent;

public class PlayerEvents implements ServerTickEvents.EndTick, PlayerBlockBreakEvents.Before, ServerPlayerEvents.AfterRespawn, ServerPlayerEvents.CopyFrom, ServerPlayConnectionEvents.Join, ClientPlayConnectionEvents.Join {
    private static final EntityAttributeModifier walkWithMithril = new EntityAttributeModifier("mithril_walk_speed", 0.05, EntityAttributeModifier.Operation.ADDITION);
    private static final EntityAttributeModifier walkWithDoreel = new EntityAttributeModifier("doreel_walk_speed", -0.025, EntityAttributeModifier.Operation.ADDITION);

    @Override
    public void onEndTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            changeMovementSpeed(player);
            IPlayerMixin cap = (IPlayerMixin) player;
            double speed = cap.get(DataKeys.MANA) < cap.get(DataKeys.MAX_MANA) ? 0.01 : 0.001;
            cap.add(DataKeys.MANA, speed);
            if (cap.get(DataKeys.COOLDOWN) > 0) cap.add(DataKeys.COOLDOWN, -1);
            cap.sync();
        }

    }

    void changeMovementSpeed(PlayerEntity player) {
        EntityAttributeInstance attributeInstance = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (attributeInstance == null) return;
        boolean flag = Utils.isTakeFullArmor(player, ModArmors.MITHRIL_ARMOR);
        boolean flag1 = Utils.isTakeFullArmor(player, ModArmors.DOREEL_ARMOR);
        if (flag && !attributeInstance.hasModifier(walkWithMithril)) {
            attributeInstance.addTemporaryModifier(walkWithMithril);
            player.sendAbilitiesUpdate();
        } else if (!flag && attributeInstance.hasModifier(walkWithMithril)) {
            attributeInstance.removeModifier(walkWithMithril.getId());
            player.sendAbilitiesUpdate();
        }
        if (flag1 && !attributeInstance.hasModifier(walkWithDoreel)) {
            attributeInstance.addTemporaryModifier(walkWithDoreel);
            player.sendAbilitiesUpdate();
        } else if (!flag1 && attributeInstance.hasModifier(walkWithDoreel)) {
            attributeInstance.removeModifier(walkWithDoreel.getId());
            player.sendAbilitiesUpdate();
        }
    }

    @Override
    public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        if (!player.hasStatusEffect(ModEffects.STRONG_FIST)) return true;
        int level = player.getStatusEffect(ModEffects.STRONG_FIST).getAmplifier();
        boolean d = state.isIn(BlockTags.NEEDS_DIAMOND_TOOL), i = state.isIn(BlockTags.NEEDS_IRON_TOOL), s = state.isIn(BlockTags.NEEDS_STONE_TOOL);
        boolean drop = d ? level > 2 : i ? level > 1 : !s || level > 0;
        AbilitiesMod.LOGGER.info(String.valueOf(drop));
        world.breakBlock(pos, drop);
        return false;
    }

    @Override
    public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        IPlayerMixin cap = (IPlayerMixin) newPlayer;
        cap.setPersistentData(((IPlayerMixin) oldPlayer).getPersistentData());
        if (!alive) cap.put(DataKeys.MANA, 0d);
        cap.fullSync();
    }

    @Override
    public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
    }

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
    }

    @Override
    public void onPlayReady(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
        ClientPlayNetworking.send(ModMessages.DATA_SYNC, PacketByteBufs.create());
    }
}
