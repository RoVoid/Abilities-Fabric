package robot.abilities.item.cubes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import robot.abilities.magic.Magic;
import robot.abilities.magic.skill.ActiveSkills;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

public abstract class MagicCubeItem extends Item {
    public MagicCubeItem(Settings settings) {
        super(settings);
    }

    abstract public Magic getMagic();

    abstract public Skill getTakenSkill();

    public void applyMagic(IPlayerMixin cap) {
        cap.put(DataKeys.MAGIC, getMagic().getName());
        ActiveSkills.add(cap, 6);
        ActiveSkills.put(cap, getTakenSkill(), true);
        SkillHelper.upLevel(cap, getTakenSkill(), 1);
        cap.getPlayer().getInventory().removeStack(cap.getPlayer().getInventory().selectedSlot);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        IPlayerMixin cap = (IPlayerMixin) player;
        if (cap.isNull() || !cap.get(DataKeys.MAGIC).isEmpty())
            return TypedActionResult.pass(player.getStackInHand(hand));
        if (!world.isClient) {
            applyMagic(cap);
            cap.sync(false);
        }
        return TypedActionResult.success(player.getStackInHand(hand));
    }
}
