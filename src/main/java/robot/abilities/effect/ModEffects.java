package robot.abilities.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;

public class ModEffects {
    public static final StatusEffect STRONG_FIST = registerEffect("strong_fist", new StrongFistEffect());

    public static StatusEffect registerEffect(String name, StatusEffect effect) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(AbilitiesMod.ID, name), effect);
    }

    public static void register() {
    }
}
