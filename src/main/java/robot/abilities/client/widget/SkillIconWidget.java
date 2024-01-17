package robot.abilities.client.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.skill.AbstractSkill;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class SkillIconWidget extends PressableWidget {
    private static final Identifier TEXTURE = new Identifier(AbilitiesMod.ID, "textures/gui/container/skill/skill_container.png");
    @NotNull
    private final AbstractSkill skill;
    protected final PressAction onPress;
    protected final NarrationSupplier narrationSupplier;

    public boolean selected = false;

    protected SkillIconWidget(@NotNull AbstractSkill skill, int x, int y, PressAction onPress, NarrationSupplier narrationSupplier) {
        super(x, y, 24, 24, Text.of(""));
        this.skill = skill;
        this.onPress = onPress;
        this.narrationSupplier = narrationSupplier;
        IPlayerMixin cap = ((IPlayerMixin) MinecraftClient.getInstance().player);
        if (cap != null) {
            this.setTooltip(skill.getTooltip(cap.get(DataKeys.SKILLS).getInt(skill.getName())));
        }
    }

    public static Builder builder(@NotNull AbstractSkill skill, PressAction onPress) {
        return new Builder(skill, onPress);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        context.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        Type t = this.selected ? Type.SELECTED : Type.UNSELECTED;
        context.drawTexture(TEXTURE, this.getX(), this.getY(), t.u, t.v, 24, 24, 156, 144);
        if (skill.getIcon() != null)
            context.drawTexture(skill.getIcon(), this.getX(), this.getY(), t.u, t.v, 24, 24, 24, 24);
    }

    public @NotNull AbstractSkill getSkill() {
        return skill;
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
        soundManager.play(PositionedSoundInstance.master(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.9f));
    }

    enum Type {
        UNSELECTED(132, 0), SELECTED(132, 24), DISABLED(132, 48), DISABLED_HOVERED(132, 72);
        final int u, v;

        Type(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    @Environment(value = EnvType.CLIENT)
    public static class Builder {
        private final PressAction onPress;
        @NotNull
        private final AbstractSkill skill;
        private int x;
        private int y;
        private NarrationSupplier narrationSupplier = Supplier::get;

        public Builder(@NotNull AbstractSkill skill, PressAction onPress) {
            this.skill = skill;
            this.onPress = onPress;
        }

        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder narrationSupplier(NarrationSupplier narrationSupplier) {
            this.narrationSupplier = narrationSupplier;
            return this;
        }

        public SkillIconWidget build() {
            //buttonWidget.setTooltip(this.skill);
            return new SkillIconWidget(this.skill, this.x, this.y, this.onPress, this.narrationSupplier);
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
