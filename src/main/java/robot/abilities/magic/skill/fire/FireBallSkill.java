package robot.abilities.magic.skill.fire;

import net.minecraft.entity.LivingEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import robot.abilities.AbilitiesMod;
import robot.abilities.entity.FireBallEntity;
import robot.abilities.magic.property.Property;
import robot.abilities.magic.skill.Skill;
import robot.abilities.util.Utils;

public class FireBallSkill extends Skill {
    public FireBallSkill() {
        super(AbilitiesMod.ID, "fireball", Type.ATTACK, Rarity.COMMON, Property.of(1.0, 0.02), Property.of(2, 2));
        add("explode", Property.of(0.1, 0.05));
        add("damage", Property.of(1.0, 0.05));
        icon();
    }

    public boolean use(LivingEntity user, int level) {
        if (user.getWorld().isClient) return false;
        Vec3d look = user.getRotationVec(1.0f);
        float speed = 1.75f;
        FireBallEntity fireball = new FireBallEntity(user.getWorld(), user, look.x * speed, look.y * speed, look.z * speed, get("explode", level));
        fireball.setDamage(get("damage", level));
        user.getWorld().spawnEntity(fireball);
        return true;
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip",
                Text.literal(Utils.decimal(get("damage", level))).formatted(Formatting.GOLD),
                Text.literal(Utils.decimal(get("explode", level))).formatted(Formatting.GOLD),
                Text.literal(Utils.decimal(get("mp", level))).formatted(Formatting.GOLD));
    }
}
