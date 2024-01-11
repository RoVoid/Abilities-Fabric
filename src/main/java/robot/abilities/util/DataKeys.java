package robot.abilities.util;

import net.minecraft.nbt.NbtCompound;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;


public class DataKeys {

    public static final Key<String> MAGIC = createStringKey("magic");
    public static final Key<String> SKILL = createStringKey("skill");
    public static final Key<Boolean> BORN = createBooleanKey("born");
    public static final Key<Double> MP = createDoubleKey("mp");
    public static final Key<Double> MP_MAX = createDoubleKey("mp_max");
    public static final Key<Integer> SCORE = createIntegerKey("score");
    public static final Key<NbtCompound> SKILLS = createCompoundKey("skills");
    public static final Key<Integer> COOLDOWN = createIntegerKey("cooldown");

    public static <T> void put(NbtCompound nbt, Key<T> key, T value) {
        String name = key.getName();
        Class<?> type = key.getType();
        switch (type.getName()) {
            case "java.lang.Boolean" -> nbt.putBoolean(name, (boolean) value);
            case "java.lang.Integer" -> nbt.putInt(name, (int) value);
            case "java.lang.Double" -> nbt.putDouble(name, (double) value);
            case "java.lang.String" -> nbt.putString(name, (String) value);
            case "net.minecraft.nbt.NbtCompound" -> nbt.put(name, (NbtCompound) value);
        }
    }

    public static <T> void add(NbtCompound nbt, Key<T> key, T value) {
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

    public static Key<Boolean> createBooleanKey(String name) {
        return new Key<>(name, Boolean.class);
    }

    public static Key<Integer> createIntegerKey(String name) {
        return new Key<>(name, Integer.class);
    }

    public static Key<Double> createDoubleKey(String name) {
        return new Key<>(name, Double.class);
    }

    public static Key<String> createStringKey(String name) {
        return new Key<>(name, String.class);
    }

    public static Key<NbtCompound> createCompoundKey(String name) {
        return new Key<>(name, NbtCompound.class);
    }

    public static <T> T get(NbtCompound nbt, Key<T> key) {
        String name = key.getName();
        Class<T> type = key.getType();
        Object value;
        switch (type.getName()) {
            case "java.lang.Boolean" -> value = nbt != null && nbt.getBoolean(name);
            case "java.lang.Integer" -> value = nbt == null ? 0 : nbt.getInt(name);
            case "java.lang.Double" -> value = nbt == null ? 0d : nbt.getDouble(name);
            case "java.lang.String" -> value = nbt == null ? "" : nbt.getString(name);
            case "net.minecraft.nbt.NbtCompound" -> value = nbt == null ? null : nbt.get(name);
            default -> value = null;
        }

        if (type.isInstance(value)) {
            return type.cast(value);
        } else {
            return null;
        }
    }

    public static Collection<Key<?>> getAll() {
        return List.of(MAGIC, MP, MP_MAX, SCORE, SKILL, BORN, SKILLS, COOLDOWN);
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