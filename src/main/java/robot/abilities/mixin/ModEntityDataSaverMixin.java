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
import robot.abilities.util.IEntityDataSaver;

@Mixin(Entity.class)
public abstract class ModEntityDataSaverMixin implements IEntityDataSaver {
    @Unique
    private static final NbtCompound DEFAULT = new NbtCompound();

    static {
        DEFAULT.putBoolean("born", false);
        DEFAULT.putDouble("mp", 0);
        DEFAULT.putDouble("mpMax", 10);
        DEFAULT.putString("magic", "");
        DEFAULT.putString("skill", "");
        //DEFAULT.putByteArray("skill", "");
        DEFAULT.putInt("score", 0);
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

    @Override
    public void sync(Entity entity) {
        if (entity instanceof ClientPlayerEntity) return;
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString("all");
        buf.writeNbt(((IEntityDataSaver) entity).getPersistentData());
        ServerPlayNetworking.send((ServerPlayerEntity) entity, ModMessages.DATA_SYNC, buf);
    }

    @Override
    public void sync(Entity entity, String... keys) {
        if (entity instanceof ClientPlayerEntity) return;
        PacketByteBuf buf = PacketByteBufs.create();
        NbtCompound nbt = ((IEntityDataSaver) entity).getPersistentData(), nbtBuf = new NbtCompound();
        for (String key : keys) {
            String type = key.substring(0, key.indexOf(":")),
                    name = key.substring(key.indexOf(":") + 1);
            if (!nbt.contains(name)) continue;
            switch (type) {
                case "bool" -> nbtBuf.putBoolean(key, nbt.getBoolean(name));
                case "int" -> nbtBuf.putInt(key, nbt.getInt(name));
                case "double" -> nbtBuf.putDouble(key, nbt.getDouble(name));
                case "string" -> nbtBuf.putString(key, nbt.getString(name));
                case "nbt" -> nbtBuf.put(key, nbt.get(name));
            }
        }
        buf.writeString("replace");
        buf.writeNbt(nbtBuf);
        ServerPlayNetworking.send((ServerPlayerEntity) entity, ModMessages.DATA_SYNC, buf);
    }
}
