package robot.abilities.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import robot.abilities.client.screen.handler.SkillManagerScreenHandler;

@Environment(EnvType.CLIENT)
public class SkillManagerScreen extends HandledScreen<SkillManagerScreenHandler> {
    public ButtonWidget button1 = ButtonWidget.builder(Text.literal("Button 1"), button -> System.out.println("You clicked button1!"))
            .dimensions(width / 2 - 205, 20, 200, 20)
            .tooltip(Tooltip.of(Text.literal("Tooltip of button1")))
            .build();
    public ButtonWidget button2 = ButtonWidget.builder(Text.literal("Button 2"), button -> {
                System.out.println("You clicked button2!");
                button.setFocused(false);
            })
            .dimensions(width / 2 + 5, 20, 200, 20)
            .tooltip(Tooltip.of(Text.literal("Tooltip of button2")))
            .build();

    public SkillManagerScreen(SkillManagerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, Text.of(""));
    }

    @Override
    protected void init() {
        button1.setPosition(width / 2 - 205, 20);
        button2.setPosition(width / 2 + 5, 20);
        addDrawableChild(button1);
        addDrawableChild(button2);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("You must see me"), width / 2, height / 2, 0xffffff);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {

    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {

    }
}
