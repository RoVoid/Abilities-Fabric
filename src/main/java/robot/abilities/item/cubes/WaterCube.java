package robot.abilities.item.cubes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import robot.abilities.magic.Magic;
import robot.abilities.magic.ModMagics;
import robot.abilities.magic.skill.ModSkills;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

public class WaterCube extends CubeItem {
    public WaterCube(Settings settings) {
        super(settings);
    }

    @Override
    public Magic getMagic() {
        return ModMagics.WATER_MAGIC;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        IPlayerMixin cap = (IPlayerMixin) player;
        if (world.isClient || cap.isNull() || !cap.get(DataKeys.MAGIC).isEmpty())
            return TypedActionResult.pass(player.getStackInHand(hand));
        applyMagic(cap, ModSkills.FERTILITY.id());
        cap.sync(false);
        return TypedActionResult.success(player.getStackInHand(hand));
    }
}
