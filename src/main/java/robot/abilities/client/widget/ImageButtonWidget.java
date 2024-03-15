package robot.abilities.client.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ImageButtonWidget extends PressableWidget {
    private final Identifier texture;// = new Identifier(AbilitiesMod.ID, "textures/gui/container/skill/skill_container.png");
    protected final PressAction onPress;
    protected int texW, texH, width, height;
    Type enabled, disabled, hover;

    public ImageButtonWidget(Identifier texture, int x, int y, int width, int height, PressAction onPress) {
        super(x, y, width, height, Text.of(""));
        this.texture = texture;
        this.texW = width;
        this.texH = height;
        this.onPress = onPress;
        this.enabled = new Type(0, 0);
        this.disabled = new Type(0, 0);
        this.hover = new Type(0, 0);
    }

    public ImageButtonWidget texWH(int texW, int texH) {
        this.texW = texW;
        this.texH = texH;
        return this;
    }

    public ImageButtonWidget wh(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public ImageButtonWidget enabled(int u, int v) {
        this.enabled = new Type(u, v);
        return this;
    }

    public ImageButtonWidget disabled(int u, int v) {
        this.disabled = new Type(u, v);
        return this;
    }

    public ImageButtonWidget hover(int u, int v) {
        this.hover = new Type(u, v);
        return this;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        context.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        if (texture != null) {
            Type t = this.active ? this.isHovered() ? this.hover : this.enabled : this.disabled;
            context.drawTexture(texture, this.getX(), this.getY(), t.u, t.v, width, height, this.texW, this.texH);
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public void onPress() {
        if (this.onPress != null && this.active) this.onPress.onPress(this);
    }

    @Environment(value = EnvType.CLIENT)
    public interface PressAction {
        void onPress(ImageButtonWidget var1);
    }

    @Environment(value = EnvType.CLIENT)
    public record Type(int u, int v) {
    }
}
