package robot.abilities.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CustomArrowEntity extends ArrowEntity {

    public CustomArrowEntity(EntityType<? extends ArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public CustomArrowEntity(World world, double x, double y, double z, ItemStack stack) {
        super(world, x, y, z, stack);
    }

    public CustomArrowEntity(World world, LivingEntity owner, ItemStack stack) {
        super(world, owner, stack);
    }


}
