package robot.abilities.magic.skill.fire;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.skill.AbstractSkill;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.text.DecimalFormat;

public class FireResistanceSkill extends AbstractSkill {
    public FireResistanceSkill() {
        super(AbilitiesMod.ID + ".fire_resistance", Type.DEFEND, new Property(3, 0.01), new Property(1));
        add("time", new Property(100));
    }

    public void use(PlayerEntity player, int level) {
        double mp = get("mp").get(level);
        IPlayerMixin cap = (IPlayerMixin) player;
        if (player.getWorld().isClient || cap.get(DataKeys.MP) < mp) return;
        cap.add(DataKeys.MP, -mp);
        cap.add(DataKeys.SCORE, 2);
        cap.sync(DataKeys.MP, DataKeys.SCORE);
        StatusEffectInstance customEffect = new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, (int) get("time", level), 0);
        player.addStatusEffect(customEffect);
    }

    @Override
    public MutableText getTooltipText(int level) {
        return Text.translatable(getName() + ".tooltip", Text.literal(new DecimalFormat("#.#").format(get("time", level) / 20)).formatted(Formatting.GOLD), Text.literal(new DecimalFormat("#.#").format(get("mp", level))).formatted(Formatting.GOLD));
    }
}
