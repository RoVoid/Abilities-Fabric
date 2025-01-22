package robot.abilities.magic.skill.fire;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.property.Property;
import robot.abilities.magic.skill.Skill;
import robot.abilities.util.Utils;

import java.util.List;

public class FireRingSkill extends Skill {
    public FireRingSkill() {
        super(AbilitiesMod.ID, "fire_ring", Type.ATTACK, Rarity.UNCOMMON, Property.of(1.0, 0.02), Property.of(2, 2));
        add("damage", Property.of(0.1, 0.05));
        add("burn_time", Property.of(5, 3));
        add("distance", Property.of(1.5, 0.05));
    }

    public static boolean shouldDamageAttacker(int level, Random random) {
        return random.nextFloat() < 0.015f * (float) level && level > 0;
    }

    public boolean use(LivingEntity user, int level) {
        if (user.getWorld().isClient) return false;
        double distance = get("distance", level);
        int fire_time = getInt("burn_time", level);
        double damage = get("damage", level);
        Vec3d pos = user.getPos();
        Box searchBox = new Box(
                pos.add(-distance, -0.5, -distance),
                pos.add(distance, user.getHeight() + 0.5, distance)
        );
        List<LivingEntity> entities = user.getWorld().getOtherEntities(user, searchBox).stream().filter((e) -> e instanceof LivingEntity).map(e -> (LivingEntity) e).toList();
        for (LivingEntity entity : entities) {
            entity.setFireTicks(fire_time);
            entity.setOnFire(true);
            entity.damage(user.getDamageSources().inFire(), (float) damage);
        }
        for (int i = 0; i < 24; i++) {
            double x = Math.cos(2 * Math.PI / 24 * i) * 2;
            double z = Math.sin(2 * Math.PI / 24 * i) * 2;
            Utils.addParticles((ServerWorld) user.getWorld(), ParticleTypes.FLAME, false, user.getX() + x, user.getY() + user.getHeight() / 2, user.getZ() + z, x / 20, 0, z / 20, 0.05f, 1);
        }
        return true;
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip",
                Text.literal(Utils.decimal("#", get("distance", level))).formatted(Formatting.GOLD),
                Text.literal(Utils.decimal(get("damage", level))).formatted(Formatting.GOLD),
                Text.literal(Utils.decimal("#.##", getInt("burn_time", level) / 20.0)).formatted(Formatting.GOLD),
                Text.literal(Utils.decimal(get("mp", level))).formatted(Formatting.GOLD));
    }

    @Override
    public Identifier getIcon() {
        return new Identifier(getNamespace(), "textures/gui/skills/fireball.png");
    }
}
