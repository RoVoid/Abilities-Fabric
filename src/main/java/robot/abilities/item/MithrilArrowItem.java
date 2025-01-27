package robot.abilities.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import robot.abilities.entity.MithrilArrowEntity;

public class MithrilArrowItem extends ArrowItem {
    public MithrilArrowItem(Settings settings) {
        super(settings);
    }

    @Override
    public MithrilArrowEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        MithrilArrowEntity arrowEntity = new MithrilArrowEntity(world, shooter, stack.copyWithCount(1));
        arrowEntity.setDamage(5);
        arrowEntity.setPunch(2);
        return arrowEntity;
    }

}
