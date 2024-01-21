package robot.abilities.magic.skill.fire;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import robot.abilities.AbilitiesMod;
import robot.abilities.entity.FireBallEntity;
import robot.abilities.magic.skill.AbstractSkill;
import robot.abilities.magic.skill.SkillEnchantment;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.text.DecimalFormat;

public class FireBallSkill extends AbstractSkill {
    public FireBallSkill() {
        super(AbilitiesMod.ID + ".fireball", Type.ATTACK, new Property(1, 0.02), new Property(1), new Property(1));
        add("power", new Property(2));
        applyEnchantment(SkillEnchantment.builder().levels(1, 100).onUsed(this::useItem).build());
    }

    public boolean useItem(LivingEntity user, int level) {
        return use(user, level / 10 + 1);
    }

    public boolean use(LivingEntity user, int level) {
        if (user.getWorld().isClient) return false;
        Vec3d look = user.getRotationVec(1.0f);
        float speed = 1.5f;
        FireBallEntity fireball = new FireBallEntity(user.getWorld(), user, look.x * speed, look.y * speed, look.z * speed, get("power", level));
        fireball.setPos(user.getX() + look.x * 1.2, user.getY() + look.y + user.getEyeHeight(user.getPose()), user.getZ() + look.z * 1.2);
        user.getWorld().spawnEntity(fireball);
        return true;
    }

    @Override
    public void usePlayer(PlayerEntity player, int level) {
        if (!canUse(player, level)) return;
        double mp = get("mp", level);
        IPlayerMixin cap = (IPlayerMixin) player;
        cap.add(DataKeys.MP, -mp).add(DataKeys.SCORE, 5);
        cap.sync(DataKeys.MP, DataKeys.SCORE);
        use(player, level);
    }

    @Override
    public boolean canUse(PlayerEntity player, int level) {
        return super.canUse(player, level) && !player.getWorld().isClient && ((IPlayerMixin) player).get(DataKeys.MP) >= get("mp", level);
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getName() + ".tooltip", Text.literal(new DecimalFormat("#.#").format(get("power", level))).formatted(Formatting.GOLD), Text.literal(new DecimalFormat("#.#").format(get("mp", level))).formatted(Formatting.GOLD));
    }
}
