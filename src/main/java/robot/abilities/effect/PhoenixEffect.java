package robot.abilities.effect;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import robot.abilities.item.ModItems;
import robot.abilities.network.ModMessages;

import java.util.Map;

public class PhoenixEffect extends StatusEffect {
    public PhoenixEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0xaa3333);
    }

    @Override
    public void onApplied(AttributeContainer attributeContainer, int amplifier) {
        double healthModifierValue = 4 * (amplifier + 1);
        double attackSpeedModifierValue = 2 * (amplifier + 1);
        double movementSpeedModifierValue = Math.min(0.02 * (amplifier + 1), 0.2);
        double attackDamageModifierValue = 1.2 * (amplifier + 1);
        addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, "8afecd58-1a95-453f-8c8e-99404ab08480", healthModifierValue, EntityAttributeModifier.Operation.ADDITION);
        addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "8afecd58-1a95-453f-8c8e-99404ab08481", attackSpeedModifierValue, EntityAttributeModifier.Operation.ADDITION);
        addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "8afecd58-1a95-453f-8c8e-99404ab08482", movementSpeedModifierValue, EntityAttributeModifier.Operation.ADDITION);
        addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "8afecd58-1a95-453f-8c8e-99404ab08484", attackDamageModifierValue, EntityAttributeModifier.Operation.ADDITION);
        for (Map.Entry<EntityAttribute, AttributeModifierCreator> entry : this.getAttributeModifiers().entrySet()) {
            EntityAttributeInstance entityAttributeInstance = attributeContainer.getCustomInstance(entry.getKey());
            if (entityAttributeInstance == null) continue;
            entityAttributeInstance.removeModifier(entry.getValue().getUuid());
            entityAttributeInstance.addPersistentModifier(entry.getValue().createAttributeModifier(0));
        }
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20, 14));
        entity.setHealth((float) entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).getValue());
        if (entity instanceof PlayerEntity player) {
            player.getHungerManager().setFoodLevel(20);
            if (!player.getWorld().isClient) {
                ServerPlayNetworking.send((ServerPlayerEntity) player, ModMessages.RENDER_FLOATING_ITEM, PacketByteBufs.create().writeItemStack(new ItemStack(ModItems.DISTORTED_BERRIES)));
            }
        }
    }
}
