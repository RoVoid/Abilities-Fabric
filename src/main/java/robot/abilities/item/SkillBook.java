package robot.abilities.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import robot.abilities.magic.ModMagics;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

public class SkillBook extends Item {
    public SkillBook(Settings settings) {
        super(settings);
    }

    public static void setSkillID(ItemStack stack, String id) {
        if (!id.isEmpty() && !SkillHelper.contain(id)) {
            id = "";
        }
        stack.getOrCreateSubNbt("skill").putString("id", id);
        stack.getOrCreateSubNbt("skill").putInt("rarity", id.isEmpty() ? Skill.Rarity.COMMON.value() : SkillHelper.get(id).getRarity().value());
    }

    public static void setSkillLevel(ItemStack stack, int level) {
        String id = stack.getOrCreateSubNbt("skill").getString("id");
        if (!id.isEmpty() || !SkillHelper.contain(id)) {
            return;
        }
        stack.getOrCreateSubNbt("skill").putInt("level", Math.max(1, Math.min(level, 100)));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        IPlayerMixin cap = (IPlayerMixin) player;
        if (world.isClient || !ModMagics.contain(cap.get(DataKeys.MAGIC))) {
            return TypedActionResult.fail(player.getStackInHand(hand));
        }
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getSubNbt("skill") == null || stack.getSubNbt("skill").isEmpty()) {
            return TypedActionResult.fail(player.getStackInHand(hand));
        }
        String id = stack.getOrCreateSubNbt("skill").getString("id");
        int level = stack.getOrCreateSubNbt("skill").getInt("level");
        if (SkillHelper.getData(cap, id, SkillHelper.Keys.LEVEL) > level ||
                !ModMagics.get(cap.get(DataKeys.MAGIC)).getAll().contains(SkillHelper.get(id))) {
            return TypedActionResult.fail(player.getStackInHand(hand));
        }
        SkillHelper.upLevel(cap, id, level - SkillHelper.getData(cap, id, SkillHelper.Keys.LEVEL));
        return TypedActionResult.success(player.getStackInHand(hand));
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = super.getDefaultStack();
        stack.getOrCreateSubNbt("skill").putString("id", "");
        stack.getOrCreateSubNbt("skill").putInt("level", 0);
        stack.getOrCreateSubNbt("skill").putInt("rarity", Skill.Rarity.COMMON.value());
        return stack;
    }
}
