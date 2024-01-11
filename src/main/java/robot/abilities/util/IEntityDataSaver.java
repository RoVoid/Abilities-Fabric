package robot.abilities.util;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Unique;

public interface IEntityDataSaver {
    NbtCompound getPersistentData();

    @Unique
    void setPersistentData(NbtCompound nbt);

    @Unique
    World getWorld();

    @Unique
    <T> void put(DataKeys.Key<T> key, T value);

    @Unique
    <T> void add(DataKeys.Key<T> key, T value);

    @Unique
    <T> T get(DataKeys.Key<T> key);

    @Unique
    void sync(Entity entity);

    void sync(Entity entity, DataKeys.Key<?>... keys);
}
