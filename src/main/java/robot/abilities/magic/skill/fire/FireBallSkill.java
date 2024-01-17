package robot.abilities.magic.skill.fire;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import robot.abilities.AbilitiesMod;
import robot.abilities.entity.FireBallEntity;
import robot.abilities.magic.skill.AbstractSkill;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.text.DecimalFormat;

public class FireBallSkill extends AbstractSkill {
    public FireBallSkill() {
        super(AbilitiesMod.ID + ".fireball", Type.ATTACK, new Property(1, 0.02), new Property(1));
        add("power", new Property(2));
    }

    public void use(PlayerEntity player, int level) {
        if (!canUse(player, level)) return;
        double mp = get("mp", level);
        IPlayerMixin cap = (IPlayerMixin) player;
        if (player.getWorld().isClient || cap.get(DataKeys.MP) < mp) return;
        cap.add(DataKeys.MP, -mp).add(DataKeys.SCORE, 5);
        cap.sync(DataKeys.MP, DataKeys.SCORE);
        Vec3d look = player.getRotationVec(1.0f);
        float speed = 1.5f;
        FireBallEntity fireball = new FireBallEntity(player.getWorld(), player, look.x * speed, look.y * speed, look.z * speed, get("power", level));
        fireball.setPos(player.getX() + look.x * 1.5, player.getY() + look.y + player.getEyeHeight(player.getPose()), player.getZ() + look.z * 1.5);
        player.getWorld().spawnEntity(fireball);
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getName() + ".tooltip", Text.literal(new DecimalFormat("#.#").format(get("power", level))).formatted(Formatting.GOLD), Text.literal(new DecimalFormat("#.#").format(get("mp", level))).formatted(Formatting.GOLD));
    }
}
