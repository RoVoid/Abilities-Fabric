package robot.abilities.magic;

import robot.abilities.AbilitiesMod;

import java.util.HashMap;
import java.util.Map;

public class ModMagics {
    private static final Map<String, Magic> magics = new HashMap<>();
    public static final Magic FIRE_MAGIC = registerMagic(new Magic(AbilitiesMod.ID + ".fire"));
    public static final Magic WATER_MAGIC = registerMagic(new Magic(AbilitiesMod.ID + ".water"));
    public static final Magic EARTH_MAGIC = registerMagic(new Magic(AbilitiesMod.ID + ".earth"));
    public static final Magic AIR_MAGIC = registerMagic(new Magic(AbilitiesMod.ID + ".air"));

    public static Magic getMagic(String name) {
        return magics.getOrDefault(name, null);
    }

    private static Magic registerMagic(Magic magic) {
        magics.put(magic.getName(), magic);
        return magic;
    }

    public static void init() {
    }
}
