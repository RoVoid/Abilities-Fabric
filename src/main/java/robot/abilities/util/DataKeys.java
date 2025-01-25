package robot.abilities.util;

import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataKeys {
    public static final HashMap<String, Key> keys = new HashMap<>();
    public static final Key<UUID> UUID_KEY = createUuidKey("player_uuid"); // Идентификатор игрока
    public static final Key<Integer> LEVEL = createIntegerKey("level"); // Уровень игрока
    public static final Key<Integer> EXPERIENCE = createIntegerKey("experience"); // Опыт, влияющий на уровень
    public static final Key<Boolean> BORN = createBooleanKey("born"); // Проверка: появлялся игрок однажды на сервере
    public static final Key<Double> MANA = createDoubleKey("mana"); // Мана
    public static final Key<Double> MAX_MANA = createDoubleKey("max_mana"); // Максимальное значение маны при котором скорость восстановления маны обычная
    public static final Key<String> MAGIC = createStringKey("magic"); // Идентификатор активной магии
    public static final Key<NbtCompound> SKILLS = createCompoundKey("skills"); // Список всех навыков
    public static final Key<NbtCompound> ACTIVE_SKILLS = createCompoundKey("active_skills"); // Список активных навыков
    public static final Key<Integer> SKILL = createIntegerKey("skill"); // Активный навык
    public static final Key<Integer> POINTS = createIntegerKey("points"); // Очки навыков, нужны для улучшения навыков
    public static final Key<Integer> COOLDOWN = createIntegerKey("cooldown"); // Задержка для корректной работы
    public static final Key<NbtCompound> ARGS = createCompoundKey("arguments"); // Задержка для корректной работы

    public static <T> void put(@NotNull NbtCompound nbt, @NotNull Key<T> key, @NotNull T value) {
        String name = key.getName();
        Class<?> type = key.getType();
        switch (type.getName()) {
            case "java.lang.Boolean" -> nbt.putBoolean(name, (Boolean) value);
            case "java.lang.Integer" -> nbt.putInt(name, (Integer) value);
            case "java.lang.Double" -> nbt.putDouble(name, (Double) value);
            case "java.lang.String" -> nbt.putString(name, (String) value);
            case "net.minecraft.nbt.NbtCompound" -> nbt.put(name, (NbtCompound) value);
            case "java.util.UUID" -> nbt.putUuid(name, (UUID) value);
            default -> throw new IllegalArgumentException("Unsupported type: " + type.getName());
        }
    }

    public static <N extends NbtCompound, T> void put(@NotNull NbtCompound nbt, @NotNull Key<N> key, @NotNull String key2, @NotNull T value) {
        String name = key.getName();
        if (!(key.getType().equals(NbtCompound.class))) return;
        NbtCompound nbt2 = (NbtCompound) nbt.get(name);
        if (nbt2 == null) return;
        switch (value.getClass().getTypeName()) {
            case "java.lang.Boolean" -> nbt2.putBoolean(key2, (Boolean) value);
            case "java.lang.Integer" -> nbt2.putInt(key2, (Integer) value);
            case "java.lang.Double" -> nbt2.putDouble(key2, (Double) value);
            case "java.lang.Float" -> nbt2.putDouble(key2, ((Number) value).doubleValue());
            case "java.lang.String" -> nbt2.putString(key2, (String) value);
            case "net.minecraft.nbt.NbtCompound" -> nbt2.put(key2, (NbtCompound) value);
            case "java.util.UUID" -> nbt2.putUuid(key2, (UUID) value);
            default -> throw new IllegalArgumentException("Unsupported type: " + value.getClass().getTypeName());
        }
        nbt.put(name, nbt2);
    }

    public static <T> void add(@NotNull NbtCompound nbt, @NotNull Key<T> key, @NotNull T value) {
        String name = key.getName();
        Class<?> type = key.getType();
        switch (type.getName()) {
            case "java.lang.Integer" -> nbt.putInt(name, nbt.getInt(name) + (Integer) value);
            case "java.lang.Double" -> nbt.putDouble(name, nbt.getDouble(name) + (Double) value);
            case "java.lang.String" -> nbt.putString(name, nbt.getString(name) + value);
            default -> throw new IllegalArgumentException("Unsupported type for addition: " + type.getName());
        }
    }

    public static <N extends NbtCompound, T> void add(@NotNull NbtCompound nbt, @NotNull Key<N> key, @NotNull String key2, @NotNull T value) {
        String name = key.getName();
        if (!(key.getType().equals(NbtCompound.class))) return;
        NbtCompound nbt2 = (NbtCompound) nbt.get(name);
        if (nbt2 == null) return;
        switch (value.getClass().getTypeName()) {
            case "java.lang.Integer" -> nbt2.putInt(key2, nbt2.getInt(key2) + (Integer) value);
            case "java.lang.Double" -> nbt2.putDouble(key2, nbt2.getDouble(key2) + (Double) value);
            case "java.lang.Float" -> nbt2.putDouble(key2, nbt2.getDouble(key2) + ((Number) value).doubleValue());
            case "java.lang.String" -> nbt2.putString(key2, nbt2.getString(key2) + value);
            default ->
                    throw new IllegalArgumentException("Unsupported type for addition: " + value.getClass().getTypeName());
        }
        nbt.put(name, nbt2);
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

    private static <T> Key<T> add(Key<T> key) {
        keys.put(key.getName(), key);
        return key;
    }

    public static <T> T get(@NotNull NbtCompound nbt, @NotNull Key<T> key) {
        String name = key.getName();
        Class<T> type = key.getType();
        Object value;
        boolean has = nbt.contains(name);
        switch (type.getName()) {
            case "java.lang.Boolean" -> value = has && nbt.getBoolean(name);
            case "java.lang.Integer" -> value = has ? nbt.getInt(name) : 0;
            case "java.lang.Double" -> value = has ? nbt.getDouble(name) : 0d;
            case "java.lang.String" -> value = has ? nbt.getString(name) : "";
            case "net.minecraft.nbt.NbtCompound" -> value = has ? nbt.get(name) : new NbtCompound();
            case "java.util.UUID" -> value = has ? nbt.getUuid(name) : null;
            default -> throw new IllegalArgumentException("Unsupported type: " + type.getName());
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
