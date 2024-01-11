package robot.abilities.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robot.abilities.block.entity.SoulFurnaceBlockEntity;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceEntityMixin extends BlockEntity {

    private AbstractFurnaceEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "getCookTime", at = @At("RETURN"), cancellable = true)
    private static void modifyCookTime(World world, AbstractFurnaceBlockEntity furnace, CallbackInfoReturnable<Integer> cir) {
        Integer original = cir.getReturnValue();

        if (furnace instanceof SoulFurnaceBlockEntity) {
            cir.setReturnValue(original / ((SoulFurnaceBlockEntity) furnace).getSpeedModifier());
        }
    }
}
