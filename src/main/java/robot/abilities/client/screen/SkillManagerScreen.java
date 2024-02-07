package robot.abilities.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import robot.abilities.AbilitiesMod;
import robot.abilities.client.screen.handler.SkillManagerScreenHandler;
import robot.abilities.client.widget.SkillIconWidget;
import robot.abilities.client.widget.TypeCategoryWidget;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.MainSkills;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.network.ModMessages;
import robot.abilities.util.Constants;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class SkillManagerScreen extends HandledScreen<SkillManagerScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(AbilitiesMod.ID, "textures/gui/container/skill/container.png");
    private static final Identifier POINT_BARS = new Identifier(AbilitiesMod.ID, "textures/gui/container/skill/point_bars.png");
    private final List<TypeCategoryWidget> categoryList = new ArrayList<>();
    private final Map<Skill.Type, List<SkillIconWidget>> skillList = new HashMap<>();
    private final List<SkillIconWidget> mainSkillList = new ArrayList<>();
    private TypeCategoryWidget lastCategory = null;
    private SkillIconWidget lastSkill = null, lastMainSkill = null;
    private boolean firstInit = true;

    public SkillManagerScreen(SkillManagerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        initCategories(firstInit);
        initSkills(firstInit);
        initMainSkills(firstInit);
        if (firstInit) {
            firstInit = false;
            onCategory(categoryList.get(0));
        }
    }

    protected void initCategories(boolean flag) {
        if (flag) {
            this.categoryList.clear();
            this.categoryList.add(TypeCategoryWidget.builder(Skill.Type.ATTACK, this::onCategory).item(Items.IRON_SWORD).build());
            this.categoryList.add(TypeCategoryWidget.builder(Skill.Type.DEFEND, this::onCategory).item(Items.SHIELD).build());
            this.categoryList.add(TypeCategoryWidget.builder(Skill.Type.SUPPORT, this::onCategory).item(Items.POTION).build());
        }
        int ky = 0;
        for (TypeCategoryWidget category : categoryList) {
            category.setPosition(width / 2 - 71, height / 2 - 60 + ky * 26);
            addDrawableChild(category);
            ky++;
        }
    }

    protected void initSkills(boolean flag) {
        if (flag) this.skillList.clear();
        if (client == null) return;
        IPlayerMixin cap = (IPlayerMixin) client.player;
        if (cap == null) return;
        for (TypeCategoryWidget category : categoryList) {
            List<SkillIconWidget> skillWidgets = flag ? new ArrayList<>() : skillList.get(category.getSkillType());
            if (flag) {
                List<Skill> skills = SkillHelper.getSkillsWithType(cap.get(DataKeys.MAGIC), category.getSkillType());
                skills.forEach((skill) -> skillWidgets.add(SkillIconWidget.builder(skill, this::onSkill).build()));
                skillList.put(category.getSkillType(), skillWidgets);
            }
            int kx = 0, ky = 0;
            for (SkillIconWidget widget : skillList.get(category.getSkillType())) {
                widget.setPosition(width / 2 - 41 + kx * 30, height / 2 - 64 + ky * 30);
                widget.visible = category == lastCategory;
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

    protected void initMainSkills(boolean flag) {
        if (flag) this.mainSkillList.clear();
        if (client == null) return;
        IPlayerMixin cap = (IPlayerMixin) client.player;
        if (cap == null) return;
        List<SkillIconWidget> skillWidgets = flag ? new ArrayList<>() : mainSkillList;
        if (flag) {
            MainSkills.getSkillNames(cap).stream().map(SkillHelper::getSkill).forEach(skill -> mainSkillList.add(SkillIconWidget.builder(skill, this::onMainSkill).always().build()));
        }
        int ky = 0;
        for (SkillIconWidget widget : mainSkillList) {
            widget.setPosition(width / 2 + 52, height / 2 - 60 + ky * 26);
            widget.selected = ky == cap.get(DataKeys.SKILL);
            if(widget.selected) lastMainSkill = widget;
            addDrawableChild(widget);
            ky++;
        }

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1);
        super.render(context, mouseX, mouseY, delta);
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
        if (lastSkill != null) lastSkill.selected = false;
        if ((skill == lastSkill && skill.selected) || !skill.canUse || client == null) return;
        skill.selected = true;
        this.lastSkill = skill;
    }

    private void onMainSkill(SkillIconWidget skill) {
        if (client == null) return;
        IPlayerMixin cap = (IPlayerMixin) client.player;
        if (lastSkill != null && lastSkill.selected) {
            lastSkill.selected = false;
            if (skill.getSkill() != lastSkill.getSkill()) {
                skill.setSkill(lastSkill.getSkill());
                MainSkills.set(cap, lastSkill.getSkill().getID(), mainSkillList.indexOf(skill), true);
                ClientPlayNetworking.send(ModMessages.SKILL_MANAGER_CHANGE, PacketByteBufs.create().writeNbt(MainSkills.toNbt(cap)).writeString(lastSkill.getSkill().getID()));
            }
        } else if (lastMainSkill != null && !skill.selected && skill.getSkill() != null) {
            MainSkills.set(cap, skill.getSkill().getID(), mainSkillList.indexOf(skill), true);
            ClientPlayNetworking.send(ModMessages.SKILL_MANAGER_CHANGE, PacketByteBufs.create().writeNbt(MainSkills.toNbt(cap)).writeString(skill.getSkill().getID()));
        }
        if (skill.getSkill() != null) {
            if (lastMainSkill != null) lastMainSkill.selected = false;
            skill.selected = true;
            this.lastMainSkill = skill;
        }
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(TEXTURE, width / 2 - 49, height / 2 - 72, 0, 0, 98, 144, 116, 144);
        context.drawTexture(POINT_BARS, width / 2 - 51, height / 2 - 80, 0, 5, 102, 5, 102, 10);
        double m = Math.min(1, ((IPlayerMixin) client.player).get(DataKeys.POINTS) / Constants.getMpPointsLimit((IPlayerMixin) client.player));
        context.drawTexture(POINT_BARS, width / 2 - 51, height / 2 - 80, 0, 0, (int) Math.floor(102 * m), 5, 102, 10);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {

    }
}
