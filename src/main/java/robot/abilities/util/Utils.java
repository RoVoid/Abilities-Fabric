package robot.abilities.util;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import robot.abilities.item.ModArmors;

public class Utils {
    public static boolean isTakeFullArmor(LivingEntity entity, ModArmors.CustomArmor armor) {
        return entity.getEquippedStack(EquipmentSlot.HEAD).getItem() == armor.HELMET && entity.getEquippedStack(EquipmentSlot.CHEST).getItem() == armor.CHESTPLATE && entity.getEquippedStack(EquipmentSlot.LEGS).getItem() == armor.LEGGINGS && entity.getEquippedStack(EquipmentSlot.FEET).getItem() == armor.BOOTS;
    }
}
