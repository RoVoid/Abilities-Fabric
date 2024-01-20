package robot.abilities.util;

import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class DataKeys {
    public static final HashMap<String, Key> keys = new HashMap<>();

    public static final Key<String> MAGIC = createStringKey("magic");
    public static final Key<String> SKILL = createStringKey("skill");
    public static final Key<Boolean> BORN = createBooleanKey("born");
    public static final Key<Double> MP = createDoubleKey("mp");
    public static final Key<Double> MP_MAX = createDoubleKey("mp_max");
    public static final Key<Integer> SCORE = createIntegerKey("score");
    public static final Key<NbtCompound> SKILLS = createCompoundKey("skills");
    public static final Key<Integer> COOLDOWN = createIntegerKey("cooldown");
    public static final Key<UUID> UUID_KEY = createUuidKey("player_uuid");

    public static <T> void put(@NotNull NbtCompound nbt, @NotNull Key<T> key, @NotNull T value) {
        String name = key.getName();
        Class<?> type = key.getType();
        switch (type.getName()) {
            case "java.lang.Boolean" -> nbt.putBoolean(name, (boolean) value);
            case "java.lang.Integer" -> nbt.putInt(name, (int) value);
            case "java.lang.Double" -> nbt.putDouble(name, (double) value);
            case "java.lang.String" -> nbt.putString(name, (String) value);
            case "net.minecraft.nbt.NbtCompound" -> nbt.put(name, (NbtCompound) value);
            case "java.util.UUID" -> nbt.putUuid(name, (UUID) value);
        }
    }

    public static <N extends NbtCompound, T> void put(@NotNull NbtCompound nbt, @NotNull Key<N> key, @NotNull String key2, @NotNull T value) {
        String name = key.getName();
        Class<?> type = key.getType();
        if (!type.getName().equals("net.minecraft.nbt.NbtCompound")) return;
        NbtCompound nbt2 = (NbtCompound) nbt.get(name);
        if (nbt2 == null) return;
        boolean er = false;
        switch (value.getClass().getTypeName()) {
            case "java.lang.Boolean" -> nbt2.putBoolean(key2, (boolean) value);
            case "java.lang.Integer" -> nbt2.putInt(key2, (int) value);
            case "java.lang.Double" -> nbt2.putDouble(key2, (double) value);
            case "java.lang.String" -> nbt2.putString(key2, (String) value);
            case "net.minecraft.nbt.NbtCompound" -> nbt2.put(key2, (NbtCompound) value);
            case "java.util.UUID" -> nbt2.putUuid(key2, (UUID) value);
            default -> er = true;
        }
        if (!er) {
            nbt.put(name, nbt2);
        }
    }

    public static <T> void add(@NotNull NbtCompound nbt, @NotNull Key<T> key, @NotNull T value) {
        String name = key.getName();
        Class<?> type = key.getType();
        switch (type.getName()) {
            case "java.lang.Integer" ->
                    nbt.putInt(name, BigDecimal.valueOf(nbt.getInt(name)).add(BigDecimal.valueOf((int) value)).intValue());
            case "java.lang.Double" ->
                    nbt.putDouble(name, BigDecimal.valueOf(nbt.getDouble(name)).add(BigDecimal.valueOf((double) value)).doubleValue());
            case "java.lang.String" -> nbt.putString(name, nbt.getString(name) + value);
        }
    }

    public static <N extends NbtCompound, T> void add(@NotNull NbtCompound nbt, @NotNull Key<N> key, @NotNull String key2, @NotNull T value) {
        String name = key.getName();
        Class<?> type = key.getType();
        if (!type.getName().equals("net.minecraft.nbt.NbtCompound")) return;
        NbtCompound nbt2 = (NbtCompound) nbt.get(name);
        if (nbt2 == null) return;
        boolean er = false;
        switch (value.getClass().getTypeName()) {
            case "java.lang.Integer" ->
                    nbt2.putInt(key2, BigDecimal.valueOf(nbt2.getInt(key2)).add(BigDecimal.valueOf((int) value)).intValue());
            case "java.lang.Double" ->
                    nbt2.putDouble(key2, BigDecimal.valueOf(nbt2.getDouble(key2)).add(BigDecimal.valueOf((double) value)).doubleValue());
            case "java.lang.String" -> nbt2.putString(key2, value + nbt2.getString(key2));
            default -> er = true;
        }
        if (!er) {
            nbt.put(name, nbt2);
        }
    }

    public static Key<Boolean> createBooleanKey(String name) {
        return add(new Key<>(name, Boolean.class));
    }

    public static Key<Integer> createIntegerKey(String name) {
        return add(new Key<>(name, Integer.class));
    }

    public static Key<Double> createDoubleKey(String name) {
        return add(new Key<>(name, Double.class));
    }

    public static Key<String> createStringKey(String name) {
        return add(new Key<>(name, String.class));
    }

    public static Key<NbtCompound> createCompoundKey(String name) {
        return add(new Key<>(name, NbtCompound.class));
    }

    public static Key<UUID> createUuidKey(String name) {
        return add(new Key<>(name, UUID.class));
    }

    private static Key add(Key key) {
        keys.put(key.getName(), key);
        return key;
    }

    public static <T> T get(@NotNull NbtCompound nbt, @NotNull Key<T> key) {
        String name = key.getName();
        Class<T> type = key.getType();
        Object value;
        boolean has = nbt.contains(name);
        try {
            switch (type.getName()) {
                case "java.lang.Boolean" -> value = has && nbt.getBoolean(name);
                case "java.lang.Integer" -> value = has ? nbt.getInt(name) : 0;
                case "java.lang.Double" -> value = has ? nbt.getDouble(name) : 0d;
                case "java.lang.String" -> value = has ? nbt.getString(name) : "";
                case "net.minecraft.nbt.NbtCompound" -> value = has ? nbt.get(name) : new NbtCompound();
                case "java.util.UUID" -> value = has ? nbt.getUuid(name) : null;
                default -> value = null;
            }
        } catch (IllegalArgumentException e) {
            return null;
        }

        if (type.isInstance(value)) {
            return type.cast(value);
        } else {
            return null;
        }
    }

    public static Map<String, Key> getAll() {
        return keys;
    }

    public static class Key<T> {
        private final String name;
        private final Class<T> type;

        private Key(String name, Class<T> type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public Class<T> getType() {
            return type;
        }
    }
}