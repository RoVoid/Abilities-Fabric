package robot.abilities.mixin;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import robot.abilities.network.ModMessages;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IEntityDataSaver;

@Mixin(Entity.class)
public abstract class ModEntityDataSaverMixin implements IEntityDataSaver {
    @Unique
    private static final NbtCompound DEFAULT = new NbtCompound();

    static {
        DataKeys.put(DEFAULT, DataKeys.MAGIC, "");
        DataKeys.put(DEFAULT, DataKeys.BORN, false);
        DataKeys.put(DEFAULT, DataKeys.SKILL, "");
        DataKeys.put(DEFAULT, DataKeys.MP, 0d);
        DataKeys.put(DEFAULT, DataKeys.MP_MAX, 10d);
        DataKeys.put(DEFAULT, DataKeys.SCORE, 0);
        DataKeys.put(DEFAULT, DataKeys.SKILLS, new NbtCompound());
        DataKeys.put(DEFAULT, DataKeys.COOLDOWN, 0);
    }

    @Unique
    private NbtCompound persistentData;

    @Override
    public NbtCompound getPersistentData() {
        return persistentData == null ? DEFAULT : persistentData;
    }

    @Unique
    @Override
    public void setPersistentData(NbtCompound nbt) {
        persistentData = nbt;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable info) {
        if (persistentData != null) {
            nbt.put("abilities.data", persistentData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("abilities.data", 10)) {
            persistentData = nbt.getCompound("abilities.data");
        }
    }

    @Unique
    @Override
    public <T> void put(DataKeys.Key<T> key, T value) {
        DataKeys.put(getPersistentData(), key, value);
    }

    @Unique
    @Override
    public <T> void add(DataKeys.Key<T> key, T value) {
        DataKeys.add(getPersistentData(), key, value);
    }

    @Unique
    @Override
    public <T> T get(DataKeys.Key<T> key) {
        return DataKeys.get(getPersistentData(), key);
    }


    @Override
    public void sync(Entity entity) {
        if (entity instanceof ClientPlayerEntity) return;
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString("all");
        buf.writeNbt(((IEntityDataSaver) entity).getPersistentData());
        ServerPlayNetworking.send((ServerPlayerEntity) entity, ModMessages.DATA_SYNC, buf);
    }

    @Override
    public void sync(Entity entity, DataKeys.Key... keys) {
        if (entity instanceof ClientPlayerEntity) return;
        PacketByteBuf buf = PacketByteBufs.create();
        NbtCompound nbt = ((IEntityDataSaver) entity).getPersistentData(), nbtBuf = new NbtCompound();
        for (DataKeys.Key key : keys) DataKeys.put(nbtBuf, key, get(key));
        buf.writeString("replace");
        buf.writeNbt(nbtBuf);
        ServerPlayNetworking.send((ServerPlayerEntity) entity, ModMessages.DATA_SYNC, buf);
    }
}
