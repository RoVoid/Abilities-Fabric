package robot.abilities.item.cubes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import robot.abilities.magic.Magic;
import robot.abilities.magic.skill.ActiveSkills;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

public abstract class CubeItem extends Item {
    public CubeItem(Settings settings) {
        super(settings);
    }

    abstract public Magic getMagic();

    public void applyMagic(IPlayerMixin cap, String skillID) {
        cap.put(DataKeys.MAGIC, getMagic().getName());
        ActiveSkills.add(cap, 6);
        ActiveSkills.put(cap, skillID, true);
        SkillHelper.upLevel(cap, skillID, 1);
        cap.getPlayer().getInventory().removeStack(cap.getPlayer().getInventory().selectedSlot);
    }

    abstract public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand);
}
