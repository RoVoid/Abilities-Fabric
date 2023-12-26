package robot.abilities.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import robot.abilities.util.IEntityDataSaver;

@Mixin(Entity.class)
public abstract class ModEntityDataSaverMixin implements IEntityDataSaver {
    private NbtCompound persistentData;
    @Override
    public NbtCompound getPersistentData() {
        return persistentData == null ? new NbtCompound() : persistentData;
    }


}
