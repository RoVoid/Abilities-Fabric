package robot.abilities.item.cubes;

import robot.abilities.magic.Magic;
import robot.abilities.magic.ModMagics;
import robot.abilities.magic.skill.ModSkills;
import robot.abilities.magic.skill.Skill;

public class EarthMagicCube extends MagicCubeItem {
    public EarthMagicCube(Settings settings) {
        super(settings);
    }

    @Override
    public Magic getMagic() {
        return ModMagics.EARTH_MAGIC;
    }

    @Override
    public Skill getTakenSkill() {
        return ModSkills.GOLEM_SUMMON;
    }
}
