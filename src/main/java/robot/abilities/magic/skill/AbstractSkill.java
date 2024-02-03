package robot.abilities.magic.skill;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import robot.abilities.network.ModMessages;
import robot.abilities.util.IPlayerMixin;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSkill {
    private final String name, namespace;
    private final Map<String, Property> properties = new HashMap<>();
    private final Type type;
    private boolean hasIcon = false;
    private SkillEnchantment enchantment;

    public AbstractSkill(String name, Type type, Property mp, Property price, Property castTime) {
        this(name.substring(0, name.indexOf(".")), name.substring(name.indexOf(".") + 1), type, mp, price, castTime);
    }

    public AbstractSkill(String namespace, String name, Type type, Property mp, Property price, Property castTime) {
        this.namespace = namespace;
        this.name = name;
        this.type = type;
        add("mp", mp);
        add("price", price);
        add("castTime", castTime);
    }

    public abstract boolean use(LivingEntity user, int level);

    public abstract void usePlayer(PlayerEntity player, int level);

    public void toClient(LivingEntity entity, int level) {
        if (entity.getWorld().isClient) return;
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(entity instanceof PlayerEntity);
        if (entity instanceof PlayerEntity) buf.writeUuid(entity.getUuid());
        else buf.writeInt(entity.getId());
        buf.writeString(getID());
        buf.writeInt(level);
        for (ServerPlayerEntity p : entity.getServer().getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(p, ModMessages.SKILL_USE_ON_CLIENT, buf);
        }
    }

    public void onClient(LivingEntity entity, int level) {
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getID() {
        return "%s:%s".formatted(namespace, name);
    }

    public String getTranslateKey() {
        return "skill.%s.%s".formatted(namespace, name);
    }

    public MutableText getDisplayName() {
        return Text.translatable(getTranslateKey());
    }

    public void enchantment(SkillEnchantment enchantment) {
        this.enchantment = enchantment;
    }

    public SkillEnchantment getEnchantment() {
        return enchantment;
    }

    public boolean isEnchantment() {
        return enchantment != null;
    }

    public Property get(String key) {
        return this.properties.get(key);
    }

    public double get(String key, int level) {
        Property property = get(key);
        return property == null ? -1 : property.get(level);
    }

    public void add(String key, Property property) {
        if (!has(key)) this.properties.put(key, property);
    }

    public boolean has(String key) {
        return this.properties.containsKey(key);
    }

    public void icon() {
        hasIcon = true;
    }

    public boolean hasIcon() {
        return hasIcon;
    }

    public Identifier getIcon() {
        return hasIcon() ? new Identifier(getNamespace(), "textures/gui/skills/%s.png".formatted(getName())) : null;
    }

    public MutableText getTooltipText(int level) {
        return Text.translatable("skill.%s.%s.tooltip".formatted(namespace, name));
    }

    public Tooltip getTooltip(int level) {
        return Tooltip.of(getDisplayName().append("\n").append(getTooltipText(level)));
    }

    public boolean canUse(PlayerEntity player, int level) {
        return level > 0;
    }

    public boolean canUse(PlayerEntity player) {
        return canUse(player, SkillHelper.getData(((IPlayerMixin) player), getID(), SkillHelper.Keys.LEVEL));
    }

    public enum Type {
        ATTACK, DEFEND, SUPPORT
    }

    public static class Property {
        double initial, delta;

        public Property(double initial, double delta) {
            this.initial = initial;
            this.delta = delta;
        }

        public Property(double initial) {
            this(initial, 0);
        }

        public double get(int level) {
            return (level - 1) < 0 ? 0 : this.delta == 0 ? this.initial : this.initial + this.delta * (level - 1);
        }
    }
}
