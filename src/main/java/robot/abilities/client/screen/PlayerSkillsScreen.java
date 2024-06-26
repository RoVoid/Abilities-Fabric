package robot.abilities.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import robot.abilities.AbilitiesMod;
import robot.abilities.client.screen.handler.PlayerSkillsScreenHandler;
import robot.abilities.client.widget.SkillIconWidget;
import robot.abilities.client.widget.TypeCategoryWidget;
import robot.abilities.magic.ModMagics;
import robot.abilities.magic.skill.ActiveSkills;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.network.ModMessages;
import robot.abilities.util.Constants;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;
import robot.abilities.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class PlayerSkillsScreen extends HandledScreen<PlayerSkillsScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(AbilitiesMod.ID, "textures/gui/container/skill/ncontainer.png");
    private static final Identifier LABEL_TEXTURE = new Identifier(AbilitiesMod.ID, "textures/gui/container/skill/label_border.png");
    private static final Identifier SKILL_LIST_TEXTURE = new Identifier(AbilitiesMod.ID, "textures/gui/container/skill/skill_list.png");
    private final List<TypeCategoryWidget> categoryList = new ArrayList<>();
    private final List<SkillIconWidget> activeSkillList = new ArrayList<>();
    private final Map<Skill.Type, List<SkillIconWidget>> skillList = new HashMap<>();
    private final SkillIconWidget overlaySkill = SkillIconWidget.builder(null, null).tooltip(Tooltip.of(Text.empty())).build();
    private boolean firstInit = true;
    private int overlayActiveIndex;
    private TypeCategoryWidget lastCategory = null;
    private SkillIconWidget lastSkill = null, lastActiveSkill = null;

    public PlayerSkillsScreen(PlayerSkillsScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        initCategories(firstInit);
        initSkills(firstInit);
        initActiveSkills(firstInit);
        if (firstInit) firstInit = false;
        overlaySkill.setSkill(null);
        overlaySkill.visible = false;
        addDrawableChild(overlaySkill);
    }

    private void initCategories(boolean firstInit) {
        if (firstInit) {
            categoryList.clear();
            for (Skill.Type type : Skill.Type.values()) {
                TypeCategoryWidget category = TypeCategoryWidget.builder(type, this::onCategory).build();
                categoryList.add(category);
                if (categoryList.size() == 1) {
                    category.selected = true;
                    lastCategory = category;
                }
            }
        }
        for (int i = 0; i < categoryList.size(); i++) {
            TypeCategoryWidget category = categoryList.get(i);
            category.setPosition(width / 2 + 142, height / 2 - 48 + i * 25);
            addDrawableChild(category);
        }
    }

    private void initSkills(boolean firstInit) {
        if (firstInit) skillList.clear();
        IPlayerMixin cap = getPlayerMixin();
        if (cap == null) return;

        for (Skill.Type type : Skill.Type.values()) {
            List<SkillIconWidget> skillWidgets = firstInit ? new ArrayList<>() : skillList.get(type);
            if (firstInit) {
                List<Skill> skills = SkillHelper.getSkillsWithType(cap.get(DataKeys.MAGIC), type);
                for (Skill skill : skills) {
                    if (SkillHelper.getData(cap, skill.id(), SkillHelper.Keys.LEVEL) > 0) {
                        skillWidgets.add(SkillIconWidget.builder(skill, this::onSkill).build());
                    }
                }
                skillList.put(type, skillWidgets);
            }
            positionSkillWidgets(skillWidgets, type);
        }
    }

    private void initActiveSkills(boolean firstInit) {
        if (firstInit) activeSkillList.clear();
        IPlayerMixin cap = getPlayerMixin();
        if (cap == null) return;

        if (firstInit) {
            for (String skillID : ActiveSkills.getSkillsID(cap)) {
                Skill skill = SkillHelper.get(skillID);
                activeSkillList.add(SkillIconWidget.builder(skill, this::onActiveSkill).build());
            }
        }
        positionActiveSkillWidgets();
    }

    private IPlayerMixin getPlayerMixin() {
        return client != null ? (IPlayerMixin) client.player : null;
    }

    private void positionSkillWidgets(List<SkillIconWidget> skillWidgets, Skill.Type type) {
        int kx = 0, ky = 0;
        for (SkillIconWidget widget : skillWidgets) {
            widget.setPosition(width / 2 + 52 + kx * 28, height / 2 - 46 + ky * 28);
            widget.selected = widget == lastSkill;
            addDrawableChild(widget);
            if (++kx >= 3) {
                kx = 0;
                ky = (ky + 1) % 6;
            }
        }
    }

    private void positionActiveSkillWidgets() {
        double radius = 27;
        double startAngle = Math.PI / 6;
        IPlayerMixin cap = getPlayerMixin();

        for (int i = 0; i < activeSkillList.size(); i++) {
            double angle = startAngle + (2 * Math.PI * i) / activeSkillList.size();
            int x = (int) (width / 2 + radius * Math.cos(angle)) - 12 - 92;
            int y = (int) (height / 2 + radius * -Math.sin(angle)) - 12;

            SkillIconWidget widget = activeSkillList.get(i);
            widget.setPosition(x, y);
            widget.selected = i == cap.get(DataKeys.SKILL);
            if (widget.selected) lastActiveSkill = widget;
            addDrawableChild(widget);
        }
    }

    private void onCategory(TypeCategoryWidget category) {
        if (category == lastCategory && category.selected) return;

        if (lastCategory != null) {
            skillList.get(lastCategory.getSkillType()).forEach(widget -> widget.visible = false);
            lastCategory.selected = false;
        }
        skillList.get(category.getSkillType()).forEach(widget -> widget.visible = true);
        category.selected = true;
        lastCategory = category;
    }

    private void onSkill(SkillIconWidget skill) {
        if (client == null || skill.getSkill() == null) return;
        IPlayerMixin cap = (IPlayerMixin) client.player;
        int level = SkillHelper.getData(cap, skill.getSkill().id(), SkillHelper.Keys.LEVEL);
        int price = Constants.getRarityPrice(skill.getSkill().getRarity());

        if (skill.selected) {
            if (cap.get(DataKeys.POINTS) >= price) {
                cap.add(DataKeys.POINTS, -price);
                SkillHelper.upLevel(cap, skill.getSkill().id(), 1);
                ClientPlayNetworking.send(ModMessages.SKILL_MANAGER_UP, PacketByteBufs.create().writeString(skill.getSkill().id()));
            }
            skill.clearTooltip();
            skill.selected = false;
        } else {
            setSkillTooltip(skill, cap, level, price);
            skill.selected = true;
        }
        updateLastSkill(skill);
    }

    private void setSkillTooltip(SkillIconWidget skill, IPlayerMixin cap, int level, int price) {
        Text tooltipText = cap.get(DataKeys.POINTS) >= price
                ? Text.translatable("container.abilities.magic_beacon.upgrade", skill.getSkill().getDisplayName().formatted(Formatting.GOLD), Text.literal(String.valueOf(level + 1)).formatted(Formatting.GOLD))
                : Text.translatable("container.abilities.magic_beacon.upgrade_fail", skill.getSkill().getDisplayName().formatted(Formatting.GOLD), Text.literal(String.valueOf(level + 1)).formatted(Formatting.GOLD));
        skill.insertTooltip(Tooltip.of(tooltipText));
    }

    private void updateLastSkill(SkillIconWidget skill) {
        if (lastSkill != null && lastSkill != skill) {
            lastSkill.clearTooltip();
            lastSkill.selected = false;
        }
        lastSkill = skill;
    }

    private void onActiveSkill(SkillIconWidget skill) {
        if (client == null) return;
        IPlayerMixin cap = (IPlayerMixin) client.player;
        if (skill.getSkill() != null) {
            if (lastActiveSkill != null) lastActiveSkill.selected = false;
            skill.selected = true;
            lastActiveSkill = skill;
            ClientPlayNetworking.send(ModMessages.SKILL_MANAGER_CHANGE, PacketByteBufs.create().writeNbt(ActiveSkills.toNbt(cap)).writeString(skill.getSkill().id()));
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 1) {
            handleRightClick(mouseX, mouseY);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void handleRightClick(double mouseX, double mouseY) {
        overlayActiveIndex = -10;
        if (!findHoveredSkill(mouseX, mouseY)) {
            findHoveredActiveSkill(mouseX, mouseY);
        }
    }

    private boolean findHoveredSkill(double mouseX, double mouseY) {
        for (SkillIconWidget s : skillList.get(lastCategory.getSkillType())) {
            if (s.isHovered()) {
                overlayActiveIndex = -1;
                overlaySkill.setSkill(s.getSkill());
                overlaySkill.setPosition(s.getX(), s.getY());
                return true;
            }
        }
        return false;
    }

    private void findHoveredActiveSkill(double mouseX, double mouseY) {
        for (SkillIconWidget s : activeSkillList) {
            if (s.isHovered()) {
                overlayActiveIndex = activeSkillList.indexOf(s);
                overlaySkill.setSkill(s.getSkill());
                overlaySkill.setPosition(s.getX(), s.getY());
                return;
            }
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (overlaySkill.getSkill() != null) {
            if (!overlaySkill.visible) overlaySkill.visible = true;
            overlaySkill.setPosition((int) (mouseX - 12), (int) (mouseY - 12));
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (overlaySkill.visible) {
            handleMouseRelease(mouseX, mouseY);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void handleMouseRelease(double mouseX, double mouseY) {
        overlaySkill.visible = false;
        IPlayerMixin cap = getPlayerMixin();
        boolean skillChanged = false;

        for (SkillIconWidget s : activeSkillList) {
            if (s.isHovered()) {
                handleSkillSwapOrSet(cap, s);
                skillChanged = true;
                break;
            }
        }

        if (skillChanged) {
            updateActiveSkills(cap);
        } else if (overlayActiveIndex >= 0) {
            ActiveSkills.clear(cap, overlayActiveIndex);
            ClientPlayNetworking.send(ModMessages.SKILL_MANAGER_CHANGE, PacketByteBufs.create().writeNbt(ActiveSkills.toNbt(cap)).writeString(""));
            updateActiveSkills(cap);
        }

        overlaySkill.setSkill(null);
    }

    private void handleSkillSwapOrSet(IPlayerMixin cap, SkillIconWidget s) {
        if (overlayActiveIndex < 0) {
            ActiveSkills.set(cap, overlaySkill.getSkill().id(), activeSkillList.indexOf(s), true);
        } else {
            ActiveSkills.swap(cap, overlayActiveIndex, activeSkillList.indexOf(s));
        }
        ClientPlayNetworking.send(ModMessages.SKILL_MANAGER_CHANGE, PacketByteBufs.create().writeNbt(ActiveSkills.toNbt(cap)).writeString(overlaySkill.getSkill().id()));
    }

    private void updateActiveSkills(IPlayerMixin cap) {
        int i = 0;
        for (String skillID : ActiveSkills.getSkillsID(cap)) {
            if (activeSkillList.size() <= i) break;
            Skill skill = SkillHelper.get(skillID);
            SkillIconWidget widget = activeSkillList.get(i);
            widget.setSkill(skill);
            widget.selected = i == cap.get(DataKeys.SKILL);
            if (widget.selected) {
                lastActiveSkill = widget;
            }
            i++;
        }
    }

    @Override
    public void render(@NotNull DrawContext context, int mouseX, int mouseY, float delta) {
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void drawBackground(@NotNull DrawContext context, float delta, int mouseX, int mouseY) {
        IPlayerMixin cap = getPlayerMixin();
        context.drawTexture(TEXTURE, width / 2 - 42, height / 2 - 52, 0, 0, 84, 104, 84, 104);
        context.drawTexture(LABEL_TEXTURE, width / 2 - 17, height / 2 - 44, 0, 0, 34, 8, 34, 9);
        drawPlayerLevel(context, cap, mouseX, mouseY);
        drawPlayerMana(context, cap);
        context.drawTexture(SKILL_LIST_TEXTURE, width / 2 + 46, height / 2 - 52, 0, 0, 98, 78, 116, 78);
    }

    private void drawPlayerLevel(DrawContext context, IPlayerMixin cap, int mouseX, int mouseY) {
        String levelText = cap.get(DataKeys.LEVEL).toString();
        context.drawText(textRenderer, levelText, (width - textRenderer.getWidth(levelText)) / 2, height / 2 - 34, 0xFFFFFFFF, true);
        if (Math.abs(width / 2 - mouseX) <= 50 && Math.abs(height / 2 - 34 + textRenderer.fontHeight - mouseY) <= textRenderer.fontHeight) {
            context.drawTooltip(textRenderer, Text.literal("Point: " + cap.get(DataKeys.POINTS)), mouseX, mouseY);
        }
        context.drawTexture(LABEL_TEXTURE, width / 2 - 17, height / 2 - 34 + textRenderer.fontHeight, 0, 8, 34, 1, 34, 9);
    }

    private void drawPlayerMana(DrawContext context, IPlayerMixin cap) {
        MutableText magicName = Text.translatable(ModMagics.get(cap.get(DataKeys.MAGIC)).getTranslateKey());
        context.drawText(textRenderer, magicName, (width - textRenderer.getWidth(magicName)) / 2, height / 2 - 26 + textRenderer.fontHeight, 0xFFFFFFFF, true);

        double mana = cap.get(DataKeys.MANA), maxMana = cap.get(DataKeys.MAX_MANA);
        MutableText manaText = Text.literal((mana > maxMana ? Utils.decimal("#", maxMana) + "+" : Utils.decimal("#.#", mana)) + " /");
        MutableText manaFullText = Text.empty().append(manaText).append(" " + Utils.decimal("#", maxMana));
        context.drawText(textRenderer, manaFullText, width / 2 - textRenderer.getWidth(manaText) + textRenderer.getWidth(" ") / 2, height / 2 - 18 + textRenderer.fontHeight * 2, 0xFFFFFFFF, true);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        // Intentionally left empty
    }
}
