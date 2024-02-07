package robot.abilities.magic;

import robot.abilities.AbilitiesMod;

import java.util.HashMap;
import java.util.Map;

public class ModMagics {
    public static final Magic FIRE_MAGIC = new Magic(AbilitiesMod.ID + ":fire_magic");
    public static final Magic WATER_MAGIC = new Magic(AbilitiesMod.ID + ":water_magic");
    public static final Magic EARTH_MAGIC = new Magic(AbilitiesMod.ID + ":earth_magic");
    public static final Magic AIR_MAGIC = new Magic(AbilitiesMod.ID + ":air_magic");

    public static final Map<String, Magic> magics = new HashMap<>();

    static {
        add(FIRE_MAGIC);
        add(WATER_MAGIC);
        add(EARTH_MAGIC);
        add(AIR_MAGIC);
    }

    public static Magic getMagic(String name) {
        return magics.getOrDefault(name, null);
    }

    private static void add(Magic magic) {
        magics.put(magic.getName(), magic);
    }
}
