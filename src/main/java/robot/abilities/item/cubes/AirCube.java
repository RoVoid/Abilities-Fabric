package robot.abilities.item.cubes;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import robot.abilities.magic.Magic;
import robot.abilities.magic.ModMagics;
import robot.abilities.magic.skill.ModSkills;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

public class AirCube extends CubeItem implements Equipment {
    public AirCube(Settings settings) {
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
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        IPlayerMixin cap = (IPlayerMixin) player;
        if (world.isClient || cap.isNull() || !cap.get(DataKeys.MAGIC).isEmpty())
            return TypedActionResult.pass(player.getStackInHand(hand));
        applyMagic(cap, ModSkills.DASH.id());
        cap.sync(false);
        return TypedActionResult.success(player.getStackInHand(hand));
    }
}
