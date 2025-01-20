package robot.abilities.item.cubes;

import robot.abilities.magic.Magic;
import robot.abilities.magic.ModMagics;
import robot.abilities.magic.skill.ModSkills;
import robot.abilities.magic.skill.Skill;

public class WaterMagicCube extends MagicCubeItem {
    public WaterMagicCube(Settings settings) {
        super(settings);
    }

    @Override
    public Magic getMagic() {
        return ModMagics.WATER_MAGIC;
    }

    @Override
    public Skill getTakenSkill() {
        return ModSkills.FERTILITY;
    }
}
