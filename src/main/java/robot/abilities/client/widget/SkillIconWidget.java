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
    private static final Identifier TEXTURE = new Identifier(AbilitiesMod.ID, "textures/gui/container/skills.png");
    protected final PressAction onPress;
    protected final NarrationSupplier narrationSupplier = Supplier::get;
    public boolean selected = false, active;
    private Skill skill;
    private State state;
    private int level;
    private Tooltip tooltip;

    public SkillIconWidget(Skill skill, int x, int y, PressAction onPress, boolean isActive) {
        super(x, y, 24, 24, Text.of(""));
        this.skill = skill;
        this.onPress = onPress;
        this.active = isActive;
    }

    public SkillIconWidget(Skill skill, PressAction onPress) {
        this(skill, -1, -1, onPress, false);
    }

    public SkillIconWidget(Skill skill, PressAction onPress, boolean isActive) {
        this(skill, -1, -1, onPress, isActive);
    }

    public void tooltip(Tooltip tooltip) {
        this.tooltip = tooltip;
        if (skill != null && tooltip == null) {
            IPlayerMixin cap = ((IPlayerMixin) MinecraftClient.getInstance().player);
            int level = SkillHelper.getData(cap, skill.id(), SkillHelper.Keys.LEVEL);
            this.setTooltip(skill.getTooltip(level));
            return;
        }
        this.setTooltip(tooltip);
    }

    public void clearTooltip() {
        this.tooltip(null);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        context.drawTexture(TEXTURE, this.getX(), this.getY(), state.u, state.v, 24, 24, 256, 256);
        if (skill != null && level > 0 && skill.hasIcon()) {
            context.drawTexture(skill.getIcon(), this.getX() + 4, this.getY(), state.u, state.v, 24, 24, 24, 24);
        }
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skillID) {
        this.skill = skillID;
        this.tooltip(tooltip);
    }

    public void updateSkill() {
        level = SkillHelper.getData((IPlayerMixin) MinecraftClient.getInstance().player, skill.id(), SkillHelper.Keys.LEVEL);
        if (skill == null) state = State.NULL_ACTIVE;
        else if (level == 0) state = State.UNOPENED;
        else if (active) state = selected ? State.SELECTED_ACTIVE : State.UNSELECTED_ACTIVE;
        else state = selected ? State.SELECTED : State.UNSELECTED;
    }

    @Override
    public void onPress() {
        if (this.onPress != null) this.onPress.onPress(this);
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
        if (skill != null && onPress != null)
            soundManager.play(PositionedSoundInstance.master(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.9f));
    }

    public enum State {
        UNSELECTED(172, 0),
        SELECTED(172, 24),
        NULL(172, 48),
        UNOPENED(172, 72),
        NULL_ACTIVE(172, 96),
        UNSELECTED_ACTIVE(172, 120),
        SELECTED_ACTIVE(172, 144);
        final int u, v;

        State(int u, int v) {
            this.u = u;
            this.v = v;
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
