package robot.abilities.item.cubes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import robot.abilities.magic.ModMagics;
import robot.abilities.magic.skill.ModSkills;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

public class FireCube extends Item {
    public FireCube(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        IPlayerMixin cap = (IPlayerMixin) player;
        if (!cap.isInit() || !cap.get(DataKeys.MAGIC).isEmpty()) return TypedActionResult.pass(player.getStackInHand(hand));
        if (!world.isClient) {
            cap.put(DataKeys.MAGIC, ModMagics.FIRE_MAGIC.getName());
            cap.put(DataKeys.SKILL, ModSkills.FIRE_BALL.getName());
            cap.put(DataKeys.SKILLS, ModSkills.FIRE_BALL.getName(), 1);
            cap.add(DataKeys.SCORE, 10);
            cap.sync(DataKeys.MAGIC, DataKeys.SCORE, DataKeys.SKILLS);
            player.getInventory().removeStack(player.getInventory().selectedSlot);
        } else {
            for (int i = 0; i < 24; i++) {
                double x = Math.cos(2 * Math.PI / 24 * i) * 2;
                double z = Math.sin(2 * Math.PI / 24 * i) * 2;
                world.addParticle(ParticleTypes.FLAME, player.getX() + x, player.getY() + 0.2, player.getZ() + z, 0, 0.095, 0);
            }
        }
        return TypedActionResult.success(player.getStackInHand(hand));
    }
}
