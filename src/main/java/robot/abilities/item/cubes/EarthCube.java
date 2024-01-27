package robot.abilities.item.cubes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import robot.abilities.magic.ModMagics;
import robot.abilities.magic.skill.MainSkills;
import robot.abilities.magic.skill.ModSkills;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

public class EarthCube extends Item implements ICube {
    public EarthCube(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world.isClient) return TypedActionResult.pass(player.getStackInHand(hand));
        IPlayerMixin cap = (IPlayerMixin) player;
        if (!cap.get(DataKeys.MAGIC).isEmpty()) return TypedActionResult.pass(player.getStackInHand(hand));
        cap.put(DataKeys.MAGIC, ModMagics.EARTH_MAGIC.getName());
        MainSkills.put(cap, ModSkills.GOLEM_SUMMON, true);
        MainSkills.updateIndex(cap, ModSkills.GOLEM_SUMMON);
        SkillHelper.upLevel(cap, ModSkills.GOLEM_SUMMON, 1);
        cap.sync(false);
        player.getInventory().removeStack(player.getInventory().selectedSlot);
        return TypedActionResult.success(player.getStackInHand(hand));
    }
}
