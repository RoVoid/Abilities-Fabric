package robot.abilities.magic.skill.earth;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import robot.abilities.AbilitiesMod;
import robot.abilities.entity.GolemEntity;
import robot.abilities.magic.property.Property;
import robot.abilities.magic.skill.Skill;

import java.text.DecimalFormat;

public class GolemSummonSkill extends Skill {

    public GolemSummonSkill() {
        super(AbilitiesMod.ID, "golem_summon", Type.SUPPORT, Rarity.COMMON, Property.of(1.0), Property.of(1));
        add("golem", Property.of(2));
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
    public MutableText getTooltipText(int level) {
        return Text.translatable(getTranslateKey() + ".tooltip", Text.literal(new DecimalFormat("#.#").format(get("golem", level))).formatted(Formatting.GOLD), Text.literal(new DecimalFormat("#.#").format(get("mp", level))).formatted(Formatting.GOLD));
    }
}
