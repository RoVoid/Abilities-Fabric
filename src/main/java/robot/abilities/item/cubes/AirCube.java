package robot.abilities.item.cubes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import robot.abilities.magic.ModMagics;
import robot.abilities.magic.skill.ModSkills;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.util.List;

public class AirCube extends Item {
    public AirCube(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        IPlayerMixin cap = (IPlayerMixin) player;
        if (!world.isClient || !cap.get(DataKeys.MAGIC).isEmpty())
            return TypedActionResult.pass(player.getStackInHand(hand));
        cap.put(DataKeys.MAGIC, ModMagics.AIR_MAGIC.getName());
        cap.put(DataKeys.SKILL, ModSkills.DASH.getName());
        cap.put(DataKeys.SKILLS, ModSkills.DASH.getName(), 1);
        cap.add(DataKeys.SCORE, 10);
        cap.sync(DataKeys.MAGIC, DataKeys.SCORE);
        player.getInventory().removeStack(player.getInventory().selectedSlot);
        Vec3d pos = player.getPos();
        List<Entity> entities = world.getOtherEntities(null, new Box(pos.add(-5, -5, -5), pos.add(5, 5, 5)));
        for (Entity e : entities) {
            if (e instanceof LivingEntity entity) {
                entity.addVelocity(entity.getPos().add(pos.multiply(-1)).multiply(4));
            }
        }
        return TypedActionResult.success(player.getStackInHand(hand));
    }
}
