package robot.abilities.item.food;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import robot.abilities.util.IPlayerMixin;
import robot.abilities.util.DataKeys;

public class DistortedBerries extends AliasedBlockItem {
    double saturation;

    public DistortedBerries(Block block, Settings settings, double saturation) {
        super(block, settings);
        this.saturation = saturation;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (world.isClient || !(user instanceof PlayerEntity player)) return super.finishUsing(stack, world, user);
        IPlayerMixin cap = (IPlayerMixin) player;
        if (cap.get(DataKeys.MANA) < cap.get(DataKeys.MAX_MANA)) {
            cap.put(DataKeys.MANA, Math.min(cap.get(DataKeys.MANA) + this.saturation, cap.get(DataKeys.MAX_MANA)));
            cap.sync(DataKeys.MANA);
        }
        return user.eatFood(world, stack);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) return super.use(world, user, hand);
        ItemStack itemStack = user.getStackInHand(hand);
        IPlayerMixin cap = (IPlayerMixin) user;
        if (user.canConsume(cap.get(DataKeys.MANA) < cap.get(DataKeys.MAX_MANA))) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }
}
