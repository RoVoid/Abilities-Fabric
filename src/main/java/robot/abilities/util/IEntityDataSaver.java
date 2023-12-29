package robot.abilities.util;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Unique;

public interface IEntityDataSaver {
    NbtCompound getPersistentData();

    @Unique
    void setPersistentData(NbtCompound nbt);

    @Unique
    void sync(Entity entity);

    void sync(Entity entity, String... keys);
}
