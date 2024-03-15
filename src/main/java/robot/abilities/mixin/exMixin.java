package robot.abilities.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class exMixin {

//    @Inject(method = "copyAndEmpty", at = @At("HEAD"), cancellable = true)
//    public void copyAndEmpty(CallbackInfoReturnable<ItemStack> cir) {
//        ItemStack original = cir.getReturnValue();
//        if (((ItemStack) this).isEmpty()) {
//            return EMPTY;
//        }
//        ItemStack itemStack = this.copy();
//        this.setCount(0);
//        cir.setReturnValue();
//    }
    /*@Inject(method = "equipStack", at = @At("HEAD"))
    public void equipStack(EquipmentSlot slot, ItemStack stack, CallbackInfo ci) {
        AbilitiesMod.LOGGER.info(stack.getItem().getName() + " " + stack.getCount());
    }*/
}
