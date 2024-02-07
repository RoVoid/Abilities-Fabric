package robot.abilities.magic.skill.air;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class PushSkill extends Skill {
    public PushSkill() {
        super(AbilitiesMod.ID + ".push", Type.ATTACK, new Property(1), new Property(5), new Property(10));
        add("push", new Property(1.2));
        // applyEnchantment(SkillEnchantment.builder().target(EnchantmentTarget.ARMOR).slotTypes(new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}).levels(1, 50).onUserDamaged(this::useItem).build());
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

    public static boolean shouldDamageAttacker(int level, Random random) {
        if (level <= 0) {
            return false;
        }
        return random.nextFloat() < 0.015f * (float) level;
    }

    @Override
    public boolean use(LivingEntity user, int level) {
        if (user.getWorld().isClient) return false;
        Vec3d pos = user.getPos();
        Vec3d look = user.getRotationVector().normalize();
        Box searchBox = new Box(
                pos.add(-5, -5, -5),
                pos.add(5, 5, 5)
        );
        double push = get("push", level);
        List<LivingEntity> entities = user.getWorld().getOtherEntities(user, searchBox).stream().filter((e) -> e instanceof LivingEntity).map((e) -> (LivingEntity) e).filter((e) -> isEntityInFront(user, e)).toList();
        for (LivingEntity entity : entities) {
            Vec3d p = entity.getPos().add(pos.multiply(-1)).normalize();
            Vec3d entityPos = entity.getPos();
            Vec3d toEntity = entityPos.subtract(pos).normalize();
            double angle = look.dotProduct(toEntity);
            double distance = pos.distanceTo(entityPos);
            double forceStrength = push * angle / distance;
            entity.velocityModified = true;
            entity.addVelocity(toEntity.multiply(forceStrength));
        }
        return true;
    }

    private static boolean isEntityInFront(LivingEntity user, Entity entity) {
        Vec3d playerLook = user.getRotationVector();
        Vec3d entityPos = entity.getPos().subtract(user.getPos());
        return playerLook.dotProduct(entityPos.normalize()) > 0.8;
    }

    @Override
    public void usePlayer(PlayerEntity player, int level) {
        if (!canUse(player, level) || player.getWorld().isClient) return;
        if (!use(player, level)) return;
        double mp = get("mp", level);
        IPlayerMixin cap = (IPlayerMixin) player;
        cap.add(DataKeys.MP, -mp);
        SkillHelper.addPoints(cap, 5);
        cap.sync(false);
    }

    @Override
    public boolean canUse(PlayerEntity player, int level) {
        return super.canUse(player, level) && ((IPlayerMixin) player).get(DataKeys.MP) >= get("mp", level);
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip",
                Text.literal(new DecimalFormat("#.#").format(get("push", level))).formatted(Formatting.GOLD),
                Text.literal(new DecimalFormat("#.#").format(get("mp", level))).formatted(Formatting.GOLD));
    }
}
