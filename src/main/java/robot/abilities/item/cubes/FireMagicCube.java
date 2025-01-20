package robot.abilities.item.cubes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import robot.abilities.magic.Magic;
import robot.abilities.magic.ModMagics;
import robot.abilities.magic.skill.ModSkills;
import robot.abilities.magic.skill.Skill;

public class FireMagicCube extends MagicCubeItem {
    public FireMagicCube(Settings settings) {
        super(settings);
    }

    @Override
    public Magic getMagic() {
        return ModMagics.FIRE_MAGIC;
    }

    @Override
    public Skill getTakenSkill() {
        return ModSkills.FIREBALL;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        TypedActionResult<ItemStack> result = super.use(world, player, hand);
        if (result.getResult().isAccepted() && world.isClient) {
            for (int i = 0; i < 24; i++) {
                double x = Math.cos(2 * Math.PI / 24 * i) * 2;
                double z = Math.sin(2 * Math.PI / 24 * i) * 2;
                world.addParticle(ParticleTypes.FLAME, player.getX() + x, player.getY() + 0.2, player.getZ() + z, 0, 0.095, 0);
            }
        }
        return result;
    }
}
