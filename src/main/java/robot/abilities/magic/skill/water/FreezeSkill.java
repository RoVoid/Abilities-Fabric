package robot.abilities.magic.skill.water;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import robot.abilities.AbilitiesMod;
import robot.abilities.entity.IcicleEntity;
import robot.abilities.magic.property.Property;
import robot.abilities.magic.skill.Skill;
import robot.abilities.util.Utils;

public class FreezeSkill extends Skill {
    public FreezeSkill() {
        super(AbilitiesMod.ID, "freeze", Type.ATTACK, Rarity.UNCOMMON, Property.of(1.0, 0.02), Property.of(2, 2));
        add("damage", Property.of(1, 0.05));
        add("explode", Property.of(0.2, 0.05));
        add("level", Property.of(1, 0.05));
        add("time", Property.of(1, 0.05));

    }

    public boolean use(LivingEntity user, int level) {
        if (user.getWorld().isClient) return false;
        Vec3d look = user.getRotationVec(1.0f);
        float speed = 1.5f;
        IcicleEntity icicle = new IcicleEntity(user.getWorld(), user, look.x * speed, look.y * speed, look.z * speed, get("explode", level));
        if (user instanceof PlayerEntity) icicle.setOwner(user);
        icicle.updatePosition(user.getX() + look.x * 1.2, user.getY() + look.y + user.getEyeHeight(user.getPose()), user.getZ() + look.z * 1.2);
        user.getWorld().spawnEntity(icicle);
        return true;
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip",
                Text.literal(Utils.decimal("#.#", get("damage", level))).formatted(Formatting.GOLD),
                Text.literal(Utils.decimal("#.#", get("mp", level))).formatted(Formatting.GOLD));
    }
}
