package robot.abilities.item.cubes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import robot.abilities.magic.ModMagics;
import robot.abilities.magic.skill.ModSkills;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

public class WaterCube extends Item {
    public WaterCube(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world.isClient) return TypedActionResult.pass(player.getStackInHand(hand));
        IPlayerMixin cap = (IPlayerMixin) player;
        if (!cap.get(DataKeys.MAGIC).isEmpty()) return TypedActionResult.pass(player.getStackInHand(hand));
        cap.put(DataKeys.MAGIC, ModMagics.WATER_MAGIC.getName());
        cap.put(DataKeys.SKILL, ModSkills.FERTILITY.getName());
        cap.add(DataKeys.SCORE, 10);
        cap.sync(DataKeys.MAGIC, DataKeys.SCORE);
        player.getInventory().removeStack(player.getInventory().selectedSlot);
        return TypedActionResult.success(player.getStackInHand(hand));
    }
}
