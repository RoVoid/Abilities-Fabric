package robot.abilities.item.cubes;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Equipment;
import robot.abilities.magic.Magic;
import robot.abilities.magic.ModMagics;
import robot.abilities.magic.skill.ModSkills;
import robot.abilities.magic.skill.Skill;

public class AirMagicCube extends MagicCubeItem implements Equipment {
    public AirMagicCube(Settings settings) {
        super(settings);
    }

    @Override
    public EquipmentSlot getSlotType() {

        return EquipmentSlot.HEAD;
    }

    @Override
    public Magic getMagic() {
        return ModMagics.AIR_MAGIC;
    }

    @Override
    public Skill getTakenSkill() {
        return ModSkills.DASH;
    }
}
