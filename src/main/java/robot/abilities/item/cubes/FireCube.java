package robot.abilities.item.cubes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class FireCube extends Item {
    public FireCube(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient) return TypedActionResult.pass(user.getStackInHand(hand));
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
