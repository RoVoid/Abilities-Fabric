package robot.abilities.client.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import robot.abilities.AbilitiesMod;

public class SkillIconWidget extends ButtonWidget {
    private static final Identifier TEXTURE = new Identifier(AbilitiesMod.ID, "textures/gui/skill/skill_button.png");
    private Identifier ICON = null;

    protected SkillIconWidget(int x, int y, Text message, PressAction onPress, NarrationSupplier narrationSupplier) {
        super(x, y, 24, 24, message, onPress, narrationSupplier);
        this.setTooltip(Tooltip.of(message));
    }

    public static Builder build(Text message, PressAction onPress) {
        return new Builder(message, onPress);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        context.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        Type t = this.isHovered() ? Type.HOVERED : Type.ENABLED;
        context.drawTexture(TEXTURE, this.getX(), this.getY(), t.u, t.v, 24, 24, 48, 24);
        if (ICON != null) context.drawTexture(ICON, this.getX(), this.getY(), t.u, t.v, 24, 24, 24, 24);
    }

    protected void setIcon(Identifier icon) {
        this.ICON = icon;
    }

    enum Type {
        ENABLED(0, 0), DISABLED(0, 0), HOVERED(24, 0);
        final int u, v;

        Type(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    @Environment(value = EnvType.CLIENT)
    public static class Builder {
        private final Text message;
        private final PressAction onPress;
        @Nullable
        private Tooltip tooltip;
        private Identifier icon;
        private int x;
        private int y;
        private NarrationSupplier narrationSupplier = DEFAULT_NARRATION_SUPPLIER;

        public Builder(Text message, PressAction onPress) {
            this.message = message;
            this.onPress = onPress;
        }

        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public Builder icon(Identifier icon) {
            this.icon = icon;
            return this;
        }

        public Builder narrationSupplier(NarrationSupplier narrationSupplier) {
            this.narrationSupplier = narrationSupplier;
            return this;
        }

        public SkillIconWidget build() {
            SkillIconWidget buttonWidget = new SkillIconWidget(this.x, this.y, this.message, this.onPress, this.narrationSupplier);
            buttonWidget.setTooltip(this.tooltip);
            buttonWidget.setIcon(this.icon);
            return buttonWidget;
        }
    }
}
