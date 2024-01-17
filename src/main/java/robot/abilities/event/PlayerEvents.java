package robot.abilities.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import robot.abilities.item.ModArmors;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;
import robot.abilities.util.Utils;

public class PlayerEvents implements ServerTickEvents.EndTick, ServerPlayConnectionEvents.Init {
    private static final EntityAttributeModifier walkWithMithril = new EntityAttributeModifier("mithril_walk_speed", 0.05, EntityAttributeModifier.Operation.ADDITION);
    private static final EntityAttributeModifier walkWithDoreel = new EntityAttributeModifier("doreel_walk_speed", -0.025, EntityAttributeModifier.Operation.ADDITION);

    @Override
    public void onEndTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            IPlayerMixin cap = (IPlayerMixin) player;
            if (!cap.isInit()) {
                cap.sync();
                continue;
            }
            changeMovementSpeed(player);
            double speed = cap.get(DataKeys.MP) < cap.get(DataKeys.MP_MAX) ? 0.01 : 0.001;
            cap.add(DataKeys.MP, speed);
            if (cap.get(DataKeys.COOLDOWN) > 0) cap.add(DataKeys.COOLDOWN, -1);
            cap.sync(DataKeys.MP, DataKeys.COOLDOWN);
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
    public void onPlayInit(ServerPlayNetworkHandler handler, MinecraftServer server) {
        //  ((IPlayerMixin) handler.player).sync();
    }
}
