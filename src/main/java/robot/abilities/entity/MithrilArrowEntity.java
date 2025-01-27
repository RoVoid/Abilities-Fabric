package robot.abilities.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import robot.abilities.item.ModItems;

public class MithrilArrowEntity extends PersistentProjectileEntity {

    private static final ItemStack DEFAULT_STACK = new ItemStack(ModItems.MITHRIL_ARROW);

    public MithrilArrowEntity(EntityType<? extends MithrilArrowEntity> entityType, World world) {
        super(entityType, world, DEFAULT_STACK);
    }

    public MithrilArrowEntity(World world, double x, double y, double z, ItemStack stack) {
        super(ModEntities.MITHRIL_ARROW, x, y, z, world, stack);
    }

    public MithrilArrowEntity(World world, LivingEntity owner, ItemStack stack) {
        super(ModEntities.MITHRIL_ARROW, owner, world, stack);
    }
}
