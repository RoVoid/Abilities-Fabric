package robot.abilities.client.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.IPlayerMixin;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class SkillIconWidget extends PressableWidget {
    private static final Identifier TEXTURE = new Identifier(AbilitiesMod.ID, "textures/gui/container/skill/buttons.png");
    private Skill skill;
    protected final PressAction onPress;
    protected final NarrationSupplier narrationSupplier;

    public boolean selected = false, canUse = false, alwaysCan;

    protected SkillIconWidget(Skill skill, int x, int y, boolean alwaysCan, PressAction onPress, NarrationSupplier narrationSupplier) {
        super(x, y, 24, 24, Text.of(""));
        this.skill = skill;
        this.alwaysCan = alwaysCan;
        this.onPress = onPress;
        this.narrationSupplier = narrationSupplier;
        IPlayerMixin cap = ((IPlayerMixin) MinecraftClient.getInstance().player);
        if (skill != null) {
            int level = SkillHelper.getData(cap, skill.getID(), SkillHelper.Keys.LEVEL);
            this.canUse = level > 0;
            this.setTooltip(this.canUse ? skill.getTooltip(level) : Tooltip.of(skill.getDisplayName()));
        }
    }

    public static Builder builder(Skill skill, PressAction onPress) {
        return new Builder(skill, onPress);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        context.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        Type t = (skill == null) ? (this.selected ? Type.NULL_SELECTED : Type.NULL) : (canUse ? (this.selected ? Type.SELECTED : Type.UNSELECTED) : Type.LOCKED);
        context.drawTexture(TEXTURE, this.getX(), this.getY(), t.u, t.v, 24, 24, 48, 96);
        if (skill != null && skill.hasIcon()) {
            context.drawTexture(skill.getIcon(), this.getX(), this.getY(), t.u, t.v, 24, 24, 24, 24);
            if (t == Type.LOCKED) {
                context.setShaderColor(1.0f, 1.0f, 1.0f, 0.8f);
                context.drawTexture(TEXTURE, this.getX(), this.getY(), t.u, t.v, 24, 24, 48, 96);
                context.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
            }
        }

    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
        IPlayerMixin cap = ((IPlayerMixin) MinecraftClient.getInstance().player);
        int level = SkillHelper.getData(cap, skill.getID(), SkillHelper.Keys.LEVEL);
        this.canUse = level > 0;
        this.setTooltip(this.canUse ? skill.getTooltip(level) : Tooltip.of(skill.getDisplayName()));
    }

    @Override
    public void onPress() {
        if (this.onPress != null && (this.canUse || this.alwaysCan)) this.onPress.onPress(this);
    }

    @Override
    protected MutableText getNarrationMessage() {
        return this.narrationSupplier.createNarrationMessage(super::getNarrationMessage);
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        if (canUse || this.alwaysCan)
            soundManager.play(PositionedSoundInstance.master(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.9f));
    }

    enum Type {
        UNSELECTED(0, 24), SELECTED(24, 24),
        NULL(0, 72), NULL_SELECTED(24, 72),
        LOCKED(0, 48), ASK(24, 48);
        final int u, v;

        Type(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    @Environment(value = EnvType.CLIENT)
    public static class Builder {
        private final PressAction onPress;
        private final Skill skill;
        private int x;
        private int y;
        private boolean alwaysCan = false;
        private NarrationSupplier narrationSupplier = Supplier::get;

        public Builder(Skill skill, PressAction onPress) {
            this.skill = skill;
            this.onPress = onPress;
        }

        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder always() {
            this.alwaysCan = true;
            return this;
        }

        public Builder narrationSupplier(NarrationSupplier narrationSupplier) {
            this.narrationSupplier = narrationSupplier;
            return this;
        }

        public SkillIconWidget build() {
            return new SkillIconWidget(this.skill, this.x, this.y, this.alwaysCan, this.onPress, this.narrationSupplier);
        }
    }

    @Environment(value = EnvType.CLIENT)
    public interface PressAction {
        void onPress(SkillIconWidget var1);
    }

    @Environment(value = EnvType.CLIENT)
    public interface NarrationSupplier {
        MutableText createNarrationMessage(Supplier<MutableText> var1);
    }
}
