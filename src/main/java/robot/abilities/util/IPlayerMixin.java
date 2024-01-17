package robot.abilities.util;

import net.minecraft.nbt.NbtCompound;
import robot.abilities.mixin.PlayerMixin;

public interface IPlayerMixin {
    NbtCompound getPersistentData();

    void setPersistentData(NbtCompound nbt);

    <T> void put(DataKeys.Key<T> key, T value);

    <N extends NbtCompound, T> void put(DataKeys.Key<N> key, String key2, T value);

    <T> IPlayerMixin add(DataKeys.Key<T> key, T value);

    <T> T get(DataKeys.Key<T> key);

    boolean isInit();

    void sync();

    void sync(DataKeys.Key... keys);
}
