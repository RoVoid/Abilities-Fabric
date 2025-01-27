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
import robot.abilities.magic.property.DoubleProperty;
import robot.abilities.magic.property.IntProperty;
import robot.abilities.network.ModMessages;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.util.Arrays;

public abstract class Skill {
    private final String name, namespace;
    private final Type type;
    private final Rarity rarity;
    private boolean hasIcon = false;

    public final DoubleProperty MP;
    public final IntProperty CAST_TIME;

    public Skill(String name, Type type, Rarity rarity, DoubleProperty mp, IntProperty castTime) {
        this(name.substring(0, name.indexOf(".")), name.substring(name.indexOf(".") + 1), type, rarity, mp, castTime);
    }

    public Skill(String namespace, String name, Type type, Rarity rarity, DoubleProperty mp, IntProperty castTime) {
        this.namespace = namespace;
        this.name = name;
        this.type = type;
        this.rarity = rarity;
        MP = mp;
        CAST_TIME = castTime;
    }

    public abstract boolean use(LivingEntity user, int level);

    public void usePlayer(PlayerEntity player, int level) {
        if (!canPlayerUse(player, level) || player.getWorld().isClient) return;
        if (!use(player, level)) return;
        afterUsing(player, level);
    }

    public boolean canPlayerUse(PlayerEntity player, int level) {
        return level > 0 && ((IPlayerMixin) player).get(DataKeys.MANA) >= MP.get(level);
    }

    public boolean canPlayerUse(IPlayerMixin cap, int level) {
        return level > 0 && cap.get(DataKeys.MANA) >= MP.get(level);
    }

    public int getUsefulLevel(IPlayerMixin cap, int pressedTime) {
        int maxLevel = SkillHelper.getData(cap, id(), SkillHelper.Keys.LEVEL);
        for (int level = 1; level <= maxLevel; level++) {
            if (cap.get(DataKeys.MANA) < MP.get(level) || (pressedTime >= 0 && pressedTime < CAST_TIME.get(level))) {
                return level - 1;
            }
        }
        return maxLevel;
    }

    public void afterUsing(PlayerEntity player, int level) {
        afterUsing((IPlayerMixin) player, level);
    }

    public void afterUsing(IPlayerMixin cap, int level) {
        SkillHelper.addExperience(cap, this, 5);
        cap.add(DataKeys.MANA, -MP.get(level));
        cap.sync();
    }

    public void toClient(LivingEntity entity, int level) {
        if (entity.getWorld().isClient) return;
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(entity instanceof PlayerEntity);
        if (entity instanceof PlayerEntity) buf.writeUuid(entity.getUuid());
        else buf.writeInt(entity.getId());
        buf.writeString(id());
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

    public Rarity getRarity() {
        return rarity;
    }

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }

    public String id() {
        return "%s:%s".formatted(namespace, name);
    }

    public String getTranslateKey() {
        return "skill.%s.%s".formatted(namespace, name);
    }

    public MutableText getDisplayName() {
        return Text.translatable(getTranslateKey());
    }

    public MutableText getTooltipText(int level) {
        return Text.translatable("skill.%s.%s.tooltip".formatted(namespace, name));
    }

    public Tooltip getTooltip(int level) {
        return Tooltip.of(getDisplayName().append(" " + level + "\n").append(getTooltipText(level)));
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

    public void applyEventsHandler() {
    }

    public enum Type {
        ATTACK, DEFEND, SUPPORT
    }

    public enum Rarity {
        COMMON(0), UNCOMMON(1), RARE(2), EPIC(3), LEGENDARY(4);
        final int rarity;

        Rarity(int rarity) {
            this.rarity = rarity;
        }

        public int value() {
            return rarity;
        }

        public static Rarity of(int value) {
            return Arrays.stream(Rarity.values()).filter(rarity -> rarity.value() == value).findFirst().orElse(COMMON);
        }

        public static int compare(Rarity rarity, Rarity rarity1) {
            return rarity.rarity - rarity1.rarity;
        }
    }
}
