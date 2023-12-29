package robot.abilities.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import robot.abilities.AbilitiesMod;
import robot.abilities.item.armor.ModArmors;
import robot.abilities.util.IEntityDataSaver;

import java.math.BigDecimal;

public class PlayerTickEvent implements ServerTickEvents.StartTick {
    private static final EntityAttributeModifier walk = new EntityAttributeModifier("custom_walk_speed", 0.05, EntityAttributeModifier.Operation.ADDITION);

    @Override
    public void onStartTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            EntityAttributeInstance attributeInstance = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            if (attributeInstance == null) continue;
            boolean flag = player.getInventory().getArmorStack(3).getItem() == ModArmors.MITHRIL_ARMOR.HELMET && player.getInventory().getArmorStack(2).getItem() == ModArmors.MITHRIL_ARMOR.CHESTPLATE && player.getInventory().getArmorStack(1).getItem() == ModArmors.MITHRIL_ARMOR.LEGGINGS && player.getInventory().getArmorStack(0).getItem() == ModArmors.MITHRIL_ARMOR.BOOTS;
            if (flag && !attributeInstance.hasModifier(walk)) {
                attributeInstance.addTemporaryModifier(walk);
                player.sendAbilitiesUpdate();
            } else if (!flag && attributeInstance.hasModifier(walk)) {
                attributeInstance.removeModifier(walk.getId());
                player.sendAbilitiesUpdate();
            }
            NbtCompound nbt = ((IEntityDataSaver) player).getPersistentData();
            double speed = nbt.getDouble("mp") < nbt.getDouble("mpMax") ? 0.01 : 0.001;
            nbt.putDouble("mp", BigDecimal.valueOf(nbt.getDouble("mp")).add(BigDecimal.valueOf(speed)).doubleValue());
            ((IEntityDataSaver) player).sync(player, "double:mp");
            AbilitiesMod.LOGGER.info("Server: " + ((IEntityDataSaver) player).getPersistentData().getDouble("mp"));
        }
    }
}
