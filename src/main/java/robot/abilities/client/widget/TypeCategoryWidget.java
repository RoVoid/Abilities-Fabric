package robot.abilities.client.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.skill.Skill;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class TypeCategoryWidget extends PressableWidget {
    private static final Identifier TEXTURE = new Identifier(AbilitiesMod.ID, "textures/gui/container/skill/buttons.png");
    private Identifier icon = null;
    private ItemStack item = ItemStack.EMPTY;
    public boolean selected = false;
    protected final PressAction onPress;
    protected final NarrationSupplier narrationSupplier;
    protected final Skill.Type type;

    protected TypeCategoryWidget(Skill.Type type, int x, int y, PressAction onPress, NarrationSupplier narrationSupplier) {
        super(x, y, 24, 24, Text.of(""));
        this.type = type;
        this.onPress = onPress;
        this.narrationSupplier = narrationSupplier;
    }

    public static Builder builder(Skill.Type type, PressAction onPress) {
        return new Builder(type, onPress);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        context.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        Type t = selected ? Type.SELECTED : Type.UNSELECTED;
        context.drawTexture(TEXTURE, this.getX(), this.getY(), t.u, t.v, 24, 24, 48, 96);
        if (icon != null) context.drawTexture(icon, this.getX(), this.getY(), t.u, t.v, 24, 24, 24, 24);
        if (!item.isEmpty()) context.drawItem(item, this.getX() + 4, this.getY() + 4);
    }

    protected void setIcon(Identifier icon) {
        this.icon = icon;
    }

    protected void setItem(Item item) {
        this.item = item == null ? ItemStack.EMPTY : new ItemStack(item);
    }

    public Skill.Type getSkillType() {
        return type;
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

    enum Type {
        UNSELECTED(0, 0), SELECTED(24, 0);
        final int u, v;

        Type(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    @Environment(value = EnvType.CLIENT)
    public static class Builder {
        private final PressAction onPress;
        private final Skill.Type type;
        @Nullable
        private Tooltip tooltip;
        private Identifier icon;
        private Item item;
        private int x;
        private int y;
        private NarrationSupplier narrationSupplier = Supplier::get;

        public Builder(Skill.Type type, PressAction onPress) {
            this.type = type;
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

        public Builder item(Item item) {
            this.item = item;
            return this;
        }

        public Builder narrationSupplier(NarrationSupplier narrationSupplier) {
            this.narrationSupplier = narrationSupplier;
            return this;
        }

        public TypeCategoryWidget build() {
            TypeCategoryWidget buttonWidget = new TypeCategoryWidget(this.type, this.x, this.y, this.onPress, this.narrationSupplier);
            buttonWidget.setTooltip(this.tooltip);
            buttonWidget.setIcon(this.icon);
            buttonWidget.setItem(this.item);
            return buttonWidget;
        }
    }

    @Environment(value = EnvType.CLIENT)
    public interface PressAction {
        void onPress(TypeCategoryWidget var1);
    }

    @Environment(value = EnvType.CLIENT)
    public interface NarrationSupplier {
        MutableText createNarrationMessage(Supplier<MutableText> var1);
    }
}
