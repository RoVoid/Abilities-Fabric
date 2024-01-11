package robot.abilities.item.food;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import robot.abilities.util.IEntityDataSaver;
import robot.abilities.util.DataKeys;

public class DistortedBerries extends AliasedBlockItem {
    double saturation;

    public DistortedBerries(Block block, Settings settings, double saturation) {
        super(block, settings);
        this.saturation = saturation;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (world.isClient) return super.finishUsing(stack, world, user);
        IEntityDataSaver cap = (IEntityDataSaver) user;
        if (cap.get(DataKeys.MP) < cap.get(DataKeys.MP_MAX)) {
            cap.put(DataKeys.MP, Math.min(cap.get(DataKeys.MP) + this.saturation, cap.get(DataKeys.MP_MAX)));
            cap.sync(user, DataKeys.MP);
        }
        return user.eatFood(world, stack);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) return super.use(world, user, hand);
        ItemStack itemStack = user.getStackInHand(hand);
        IEntityDataSaver cap = (IEntityDataSaver) user;
        if (user.canConsume(cap.get(DataKeys.MP) < cap.get(DataKeys.MP_MAX))) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }
}
