package robot.abilities.magic.skill.fire;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;
import robot.abilities.entity.GolemEntity;
import robot.abilities.magic.skill.Skill;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.text.DecimalFormat;

public class DragonSummonSkill extends Skill {
    public DragonSummonSkill() {
        super(AbilitiesMod.ID + ".dragon_summon", Type.SUPPORT, new Property(5, 0.02), new Property(1), new Property(2, 2));
        add("dragon", new Property(1, 0.05));
    }

    @Override
    public boolean use(LivingEntity user, int level) {
        if (user.getWorld().isClient) return false;
        GolemEntity golem = new GolemEntity(user.getWorld(), null);
        if (user instanceof PlayerEntity) golem.setOwner((PlayerEntity) user);
        golem.updatePosition(user.getX(), user.getY(), user.getZ());
        user.getWorld().spawnEntity(golem);
        return true;
    }

    @Override
    public void usePlayer(PlayerEntity player, int level) {
        if (!canUse(player, level) || player.getWorld().isClient) return;
        double mp = get("mp", level);
        IPlayerMixin cap = (IPlayerMixin) player;
        cap.add(DataKeys.MP, -mp).add(DataKeys.POINTS, 5);
        cap.sync(DataKeys.MP, DataKeys.POINTS);
        use(player, level);
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
