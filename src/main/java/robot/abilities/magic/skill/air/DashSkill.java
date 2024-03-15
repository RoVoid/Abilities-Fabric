package robot.abilities.magic.skill.air;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.SkillEnchantment;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.util.Map;

public class DashSkill extends Skill {
    public DashSkill() {
        super(AbilitiesMod.ID + ".dash", Type.SUPPORT, new Property(1), new Property(5), new Property(10));
        add("dash", new Property(1.2));
        enchantment(SkillEnchantment.builder(getNamespace(), getName()).target(EnchantmentTarget.ARMOR).slotTypes(new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}).levels(1, 50).onUserDamaged(this::useItem).build());
    }

    public static boolean shouldDamageAttacker(int level, Random random) {
        if (level <= 0) {
            return false;
        }
        return random.nextFloat() < 0.015f * (float) level;
    }

    public void useItem(LivingEntity user, Entity attacker, int level) {
        Random random = user.getRandom();
        Map.Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.chooseEquipmentWith(getEnchantment(), user);
        if (shouldDamageAttacker(level, random)) {
            if (attacker != null) {
                Vec3d vec3d = attacker.getPos().add(user.getPos().multiply(-1));
                if (vec3d.length() > 5) return;
                user.velocityModified = true;
                attacker.addVelocity(vec3d.multiply((level - 1) / 50f + 1));
            }
            if (entry != null) {
                entry.getValue().damage(2, user, entity -> entity.sendEquipmentBreakStatus(entry.getKey()));
            }
        }
    }

    @Override
    public boolean use(LivingEntity user, int level) {
        if (user.getWorld().isClient) return false;
        double dash = get("dash", level);
        user.velocityModified = true;
        user.addVelocity(user.getRotationVec(1).multiply(dash));
        return true;
    }

    @Override
    public void usePlayer(PlayerEntity player, int level) {
        if (!canUse(player, level) || player.getWorld().isClient) return;
        if (!use(player, level)) return;
        double mp = get("mp", level);
        IPlayerMixin cap = (IPlayerMixin) player;
        cap.add(DataKeys.MANA, -mp);
        cap.add(DataKeys.POINTS, 5);
        SkillHelper.addExperience(cap, this, 5);
        cap.sync(false);
    }
}
