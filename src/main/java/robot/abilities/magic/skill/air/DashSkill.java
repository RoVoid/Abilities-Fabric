package robot.abilities.magic.skill.air;

import net.minecraft.entity.player.PlayerEntity;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.skill.AbstractSkill;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

public class DashSkill extends AbstractSkill {
    public DashSkill() {
        super(AbilitiesMod.ID + ".dash", Type.SUPPORT, new Property(2), new Property(5));
        add("dash", new Property(2));
    }

    @Override
    public void use(PlayerEntity player, int level) {
        if (!canUse(player, level)) return;
        double mp = get("mp", level);
        IPlayerMixin cap = (IPlayerMixin) player;
        if (player.getWorld().isClient || cap.get(DataKeys.MP) < mp) return;
        cap.add(DataKeys.MP, -mp);
        cap.add(DataKeys.SCORE, 5);
        cap.sync(DataKeys.MP, DataKeys.SCORE);
        double dash = get("dash", level);
        player.addVelocity(player.getRotationVec(1).multiply(dash));
    }
}
