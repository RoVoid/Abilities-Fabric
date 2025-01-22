package robot.abilities.magic.skill.air;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.property.Property;
import robot.abilities.magic.skill.Skill;
import robot.abilities.util.Utils;

import java.util.Map;

public class DashSkill extends Skill {
    public DashSkill() {
        super(AbilitiesMod.ID, "dash", Type.SUPPORT, Rarity.COMMON, Property.of(1.0), Property.of(10));
        add("dash", Property.of(1.2, 0.01));
    }

    public static boolean shouldDamageAttacker(int level, Random random) {
        if (level <= 0) {
            return false;
        }
        return random.nextFloat() < 0.015f * (float) level;
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
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip",
                Text.literal(Utils.decimal(get("dash", level))).formatted(Formatting.GOLD),
                Text.literal(Utils.decimal(get("mp", level))).formatted(Formatting.GOLD));
    }
}
