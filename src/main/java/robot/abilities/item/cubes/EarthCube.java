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

public class EarthCube extends Item {
    public EarthCube(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world.isClient) return TypedActionResult.pass(player.getStackInHand(hand));
        IPlayerMixin cap = (IPlayerMixin) player;
        if (!cap.get(DataKeys.MAGIC).isEmpty()) return TypedActionResult.pass(player.getStackInHand(hand));
        cap.put(DataKeys.MAGIC, ModMagics.EARTH_MAGIC.getName());
        cap.put(DataKeys.SKILL, ModSkills.GOLEM_SUMMON.getName());
        cap.put(DataKeys.SKILLS, ModSkills.GOLEM_SUMMON.getName(), 1);
        cap.add(DataKeys.SCORE, 10);
        cap.sync(DataKeys.MAGIC, DataKeys.SCORE, DataKeys.SKILLS);
        player.getInventory().removeStack(player.getInventory().selectedSlot);
        return TypedActionResult.success(player.getStackInHand(hand));
    }
}
