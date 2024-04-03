package robot.abilities.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import robot.abilities.AbilitiesMod;
import robot.abilities.block.MagicCircleBlock;
import robot.abilities.block.ModBlocks;
import robot.abilities.magic.ModMagics;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

public class CircleTemplate extends Item {
    public CircleTemplate(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(context.getWorld().isClient) return ActionResult.CONSUME;
        ItemStack stack = context.getPlayer().getStackInHand(context.getHand());
        if(!(stack.getItem() instanceof CircleTemplate)) return ActionResult.FAIL;
        BlockPos pos = context.getBlockPos().offset(context.getSide());
        context.getWorld().setBlockState(pos, ModBlocks.MAGIC_CIRCLE.getDefaultState(), 3);
        context.getPlayer().setStackInHand(context.getHand(), ItemStack.EMPTY);
        return ActionResult.SUCCESS;
    }
}
