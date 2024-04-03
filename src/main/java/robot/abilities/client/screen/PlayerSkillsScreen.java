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

    protected void initCategories(boolean flag) {
        if (flag) {
            categoryList.clear();
            categoryList.add(TypeCategoryWidget.builder(Skill.Type.ATTACK, this::onCategory).build());
            categoryList.add(TypeCategoryWidget.builder(Skill.Type.DEFEND, this::onCategory).build());
            categoryList.add(TypeCategoryWidget.builder(Skill.Type.SUPPORT, this::onCategory).build());
            lastCategory = categoryList.get(0);
            lastCategory.selected = true;
        }
        int ky = 0;
        for (TypeCategoryWidget category : categoryList) {
            category.setPosition(width / 2 + 142, height / 2 - 48 + ky * 25);
            addDrawableChild(category);
            ky++;
        }
    }

    protected void initSkills(boolean flag) {
        if (flag) skillList.clear();
        if (client == null) return;
        IPlayerMixin cap = (IPlayerMixin) client.player;
        if (cap == null) return;
        for (Skill.Type type : Skill.Type.values()) {
            List<SkillIconWidget> skillWidgets = flag ? new ArrayList<>() : skillList.get(type);
            if (flag) {
                List<Skill> skills = SkillHelper.getSkillsWithType(cap.get(DataKeys.MAGIC), type);
                skills.forEach((skill) -> {
                    if (SkillHelper.getData(cap, skill.id(), SkillHelper.Keys.LEVEL) > 0)
                        skillWidgets.add(SkillIconWidget.builder(skill, this::onSkill).build());
                });
                skillList.put(type, skillWidgets);
            }
            int kx = 0, ky = 0;
            for (SkillIconWidget widget : skillList.get(type)) {
                widget.setPosition(width / 2 + 52 + kx * 28, height / 2 - 46 + ky * 28);
                widget.selected = widget == lastSkill;
                addDrawableChild(widget);
                kx++;
                if (kx >= 3) {
                    kx = 0;
                    ky = (ky + 1) % 6;
                }
            }
        }
    }

    protected void initActiveSkills(boolean flag) {
        if (flag) activeSkillList.clear();
        if (client == null) return;

        IPlayerMixin cap = (IPlayerMixin) client.player;
        if (cap == null) return;

        List<SkillIconWidget> skillWidgets = flag ? new ArrayList<>() : activeSkillList;

        if (flag) {
            ActiveSkills.getSkillsID(cap).stream().map(SkillHelper::get)
                    .forEach(skill -> activeSkillList.add(SkillIconWidget.builder(skill, this::onActiveSkill).build()));
        }

        double radius = 27;
        double startAngle = Math.PI / 6;

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
        if (lastCategory == category && lastCategory.selected) return;
        if (lastCategory != null && lastCategory.selected) {
            skillList.get(lastCategory.getSkillType()).forEach((widget) -> widget.visible = false);
            lastCategory.selected = false;
        }
        skillList.get(category.getSkillType()).forEach((widget) -> widget.visible = true);
        category.selected = true;
        this.lastCategory = category;
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
            Text text;
            if (cap.get(DataKeys.POINTS) >= price)
                text = Text.translatable("container.abilities.magic_beacon.upgrade", skill.getSkill().getDisplayName().formatted(Formatting.GOLD), Text.literal(String.valueOf(level + 1)).formatted(Formatting.GOLD));
            else
                text = Text.translatable("container.abilities.magic_beacon.upgrade_fail", skill.getSkill().getDisplayName().formatted(Formatting.GOLD), Text.literal(String.valueOf(level + 1)).formatted(Formatting.GOLD));
            skill.insertTooltip(Tooltip.of(text));
            skill.selected = true;
        }
        if (lastSkill != null && lastSkill != skill) {
            lastSkill.clearTooltip();
            lastSkill.selected = false;
        }
        this.lastSkill = skill;
    }

    private void onActiveSkill(SkillIconWidget skill) {
        if (client == null) return;
        IPlayerMixin cap = (IPlayerMixin) client.player;
        if (skill.getSkill() != null) {
            if (lastActiveSkill != null) lastActiveSkill.selected = false;
            skill.selected = true;
            this.lastActiveSkill = skill;
            ClientPlayNetworking.send(ModMessages.SKILL_MANAGER_CHANGE, PacketByteBufs.create().writeNbt(ActiveSkills.toNbt(cap)).writeString(skill.getSkill().id()));
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 1) {
            overlayActiveIndex = -10;
            for (var s : skillList.get(lastCategory.getSkillType())) {
                if (s.isHovered()) {
                    overlayActiveIndex = -1;
                    overlaySkill.setSkill(s.getSkill());
                    overlaySkill.setPosition(s.getX(), s.getY());
                    break;
                }
            }
            if (overlayActiveIndex == -10) for (var s : activeSkillList) {
                if (s.isHovered()) {
                    overlayActiveIndex = activeSkillList.indexOf(s);
                    overlaySkill.setSkill(s.getSkill());
                    overlaySkill.setPosition(s.getX(), s.getY());
                    break;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
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
            overlaySkill.visible = false;
            IPlayerMixin cap = (IPlayerMixin) client.player;
            boolean f = false;
            for (var s : activeSkillList) {
                if (s.isHovered()) {
                    if (overlayActiveIndex < 0)
                        ActiveSkills.set(cap, overlaySkill.getSkill().id(), activeSkillList.indexOf(s), true);
                    else ActiveSkills.swap(cap, overlayActiveIndex, activeSkillList.indexOf(s));
                    ClientPlayNetworking.send(ModMessages.SKILL_MANAGER_CHANGE, PacketByteBufs.create().writeNbt(ActiveSkills.toNbt(cap)).writeString(overlaySkill.getSkill().id()));
                    f = true;
                    break;
                }
            }
            if (f) updateActiveSkills(cap);
            else if (overlayActiveIndex >= 0) {
                ActiveSkills.clear(cap, overlayActiveIndex);
                ClientPlayNetworking.send(ModMessages.SKILL_MANAGER_CHANGE, PacketByteBufs.create().writeNbt(ActiveSkills.toNbt(cap)).writeString(""));
                updateActiveSkills(cap);
            }

            overlaySkill.setSkill(null);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void updateActiveSkills(IPlayerMixin cap) {
        int i = 0;
        for (String skillID : ActiveSkills.getSkillsID(cap)) {
            if (activeSkillList.size() <= i) break;
            Skill skill = SkillHelper.get(skillID);
            activeSkillList.get(i).setSkill(skill);
            activeSkillList.get(i).selected = false;
            if (i == cap.get(DataKeys.SKILL)) {
                activeSkillList.get(i).selected = true;
                lastActiveSkill = activeSkillList.get(i);
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
        IPlayerMixin cap = (IPlayerMixin) client.player;
        context.drawTexture(TEXTURE, width / 2 - 42, height / 2 - 52, 0, 0, 84, 104, 84, 104);
        context.drawTexture(LABEL_TEXTURE, width / 2 - 17, height / 2 - 44, 0, 0, 34, 8, 34, 9);
        context.drawText(textRenderer, cap.get(DataKeys.LEVEL).toString(), (width - textRenderer.getWidth(cap.get(DataKeys.LEVEL).toString())) / 2, height / 2 - 34, 0xFFFFFFFF, true);
        if (Math.abs(width / 2 - mouseX) <= 50 && Math.abs(height / 2 - 34 + textRenderer.fontHeight - mouseY) <= textRenderer.fontHeight) {
            context.drawTooltip(textRenderer, Text.literal("Point: " + cap.get(DataKeys.POINTS)), mouseX, mouseY);
        }
        context.drawTexture(LABEL_TEXTURE, width / 2 - 17, height / 2 - 34 + textRenderer.fontHeight, 0, 8, 34, 1, 34, 9);
        MutableText magicName = Text.translatable(ModMagics.get(cap.get(DataKeys.MAGIC)).getTranslateKey());
        context.drawText(textRenderer, magicName, (width - textRenderer.getWidth(magicName)) / 2, height / 2 - 26 + textRenderer.fontHeight, 0xFFFFFFFF, true);
        double mana = cap.get(DataKeys.MANA), max_mana = cap.get(DataKeys.MAX_MANA);
        MutableText manaText = Text.literal((mana > max_mana ? Utils.decimal("#", max_mana) + "+" : Utils.decimal("#.#", mana)) + " /");
        MutableText manaFullText = Text.empty().append(manaText).append(" " + Utils.decimal("#", max_mana));
        context.drawText(textRenderer, manaFullText, width / 2 - textRenderer.getWidth(manaText) + textRenderer.getWidth(" ") / 2, height / 2 - 18 + textRenderer.fontHeight * 2, 0xFFFFFFFF, true);
        context.drawTexture(SKILL_LIST_TEXTURE, width / 2 + 46, height / 2 - 52, 0, 0, 98, 78, 116, 78);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {

    }
}
