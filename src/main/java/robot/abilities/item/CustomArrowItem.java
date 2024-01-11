package robot.abilities.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import robot.abilities.entity.CustomArrowEntity;

public class CustomArrowItem extends ArrowItem {
    public CustomArrowItem(Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        CustomArrowEntity arrowEntity = new CustomArrowEntity(world, shooter, stack.copyWithCount(1));
        arrowEntity.initFromStack(stack);
        arrowEntity.setDamage(5);
        arrowEntity.setPunch(2);
        return arrowEntity;
    }
}
