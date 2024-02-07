package robot.abilities.magic.skill.fire;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.SkillEnchantment;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class FireRingSkill extends Skill {
    public FireRingSkill() {
        super(AbilitiesMod.ID + ".fire_ring", Type.ATTACK, new Property(1, 0.02), new Property(1), new Property(2, 2));
        add("damage", new Property(0.1, 0.05));
        add("fire_time", new Property(5, 3));
        add("distance", new Property(1.5, 0.05));
        enchantment(SkillEnchantment.builder(getNamespace(), getName()).target(EnchantmentTarget.ARMOR).slotTypes(new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}).levels(1, 50).onUserDamaged(this::useItem).build());
    }

    public void useItem(LivingEntity user, Entity attacker, int level) {
        Random random = user.getRandom();
        Map.Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.chooseEquipmentWith(getEnchantment(), user);
        if (shouldDamageAttacker(level, random)) {
            use(user, level);
            if (entry != null) {
                entry.getValue().damage(2, user, entity -> entity.sendEquipmentBreakStatus(entry.getKey()));
            }
        }
    }

    public static boolean shouldDamageAttacker(int level, Random random) {
        return random.nextFloat() < 0.015f * (float) level && level > 0;
    }

    public boolean use(LivingEntity user, int level) {
        if (user.getWorld().isClient) return false;
        double distance = get("distance", level);
        int fire_time = (int) Math.floor(get("fire_time", level));
        double damage = get("damage", level);
        Vec3d pos = user.getPos();
        Box searchBox = new Box(
                pos.add(-distance, -0.5, -distance),
                pos.add(distance, user.getHeight() + 0.5, distance)
        );
        List<LivingEntity> entities = user.getWorld().getOtherEntities(user, searchBox).stream().filter((e) -> e instanceof LivingEntity).map(e -> (LivingEntity) e).toList();
        AbilitiesMod.LOGGER.info(entities.toString());
        for (LivingEntity entity : entities) {
            entity.setFireTicks(fire_time);
            entity.setOnFire(true);
            entity.damage(user.getDamageSources().inFire(), (float) damage);
        }
        toClient(user, level);
        return true;
    }

    @Override
    public void usePlayer(PlayerEntity player, int level) {
        if (!canUse(player, level) || player.getWorld().isClient) return;
        if (!use(player, level)) return;
        double mp = get("mp", level);
        IPlayerMixin cap = (IPlayerMixin) player;
        SkillHelper.addPoints(cap, 5);
        cap.add(DataKeys.MP, -mp);
        cap.sync(false);
    }

    @Override
    public void onClient(LivingEntity entity, int level) {
        for (int i = 0; i < 24; i++) {
            double x = Math.cos(2 * Math.PI / 24 * i) * 2;
            double z = Math.sin(2 * Math.PI / 24 * i) * 2;
            entity.getWorld().addParticle(ParticleTypes.FLAME, entity.getX() + x, entity.getY() + entity.getHeight() / 2, entity.getZ() + z, x / 20, 0, z / 20);
        }
    }

    @Override
    public boolean canUse(PlayerEntity player, int level) {
        return super.canUse(player, level) && ((IPlayerMixin) player).get(DataKeys.MP) >= get("mp", level);
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip", Text.literal(new DecimalFormat("#.#").format(get("power", level))).formatted(Formatting.GOLD), Text.literal(new DecimalFormat("#.#").format(get("mp", level))).formatted(Formatting.GOLD));
    }

    @Override
    public Identifier getIcon() {
        return new Identifier(getNamespace(), "textures/gui/skills/fireball.png");
    }
}
