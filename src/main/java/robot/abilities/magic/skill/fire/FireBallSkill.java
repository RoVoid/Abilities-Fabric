package robot.abilities.magic.skill.fire;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import robot.abilities.AbilitiesMod;
import robot.abilities.entity.FireBallEntity;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.SkillEnchantment;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;
import robot.abilities.util.Utils;

public class FireBallSkill extends Skill {
    public FireBallSkill() {
        super(AbilitiesMod.ID, "fireball", Type.ATTACK, Property.of(1, 0.02), Property.of(1), Property.of(2, 0.1));
        add("explode", Property.of(0.1, 0.05));
        add("damage", Property.of(1, 0.05));
        enchantment(SkillEnchantment.builder(getNamespace(), getName()).levels(1, 100).onUsed(this::useItem).build());
    }

    public boolean useItem(LivingEntity user, int level) {
        return use(user, level / 10 + 1);
    }

    public boolean use(LivingEntity user, int level) {
        if (user.getWorld().isClient) return false;
        Vec3d look = user.getRotationVec(1.0f);
        float speed = 1.5f;
        FireBallEntity fireball = new FireBallEntity(user.getWorld(), user, look.x * speed, look.y * speed, look.z * speed, get("explode", level));
        fireball.setDamage(get("damage", level));
        fireball.setPos(user.getX() + look.x * 1.2, user.getY() + look.y + user.getEyeHeight(user.getPose()), user.getZ() + look.z * 1.2);
        user.getWorld().spawnEntity(fireball);
        return true;
    }

    public void usePlayer(PlayerEntity player, int level) {
        if (!canUse(player, level) || player.getWorld().isClient) return;
        if (!use(player, level)) return;
        double mp = get("mp", level);
        IPlayerMixin cap = (IPlayerMixin) player;
        cap.add(DataKeys.POINTS, 5);
        SkillHelper.addExperience(cap, this, 1);
        cap.add(DataKeys.MANA, -mp);
        cap.sync(false);
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip",
                Text.literal(Utils.decimal(get("damage", level))).formatted(Formatting.GOLD),
                Text.literal(Utils.decimal(get("explode", level))).formatted(Formatting.GOLD),
                Text.literal(Utils.decimal(get("mp", level))).formatted(Formatting.GOLD));
    }

    @Override
    public MutableText getTooltipTextWithDelta(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip",
                Text.literal("%s > %s".formatted(Utils.decimal(get("damage", level)), Utils.decimal(get("damage", level + 1)))).formatted(Formatting.GREEN),
                Text.literal("%s > %s".formatted(Utils.decimal(get("explode", level)), Utils.decimal(get("explode", level + 1)))).formatted(Formatting.GREEN),
                Text.literal("%s > %s".formatted(Utils.decimal(get("mp", level)), Utils.decimal(get("mp", level + 1)))).formatted(Formatting.GREEN));
    }
}
