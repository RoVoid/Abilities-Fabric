package robot.abilities.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import robot.abilities.AbilitiesMod;
import robot.abilities.magic.ModMagics;
import robot.abilities.magic.skill.ModSkills;
import robot.abilities.magic.skill.Skill;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.DataKeys;
import robot.abilities.util.IPlayerMixin;

import java.util.List;
import java.util.Random;

public class SkillBook extends Item {
    public SkillBook(Settings settings) {
        super(settings);
    }

    public static void setSkill(ItemStack stack, String id) {
        if (!id.isEmpty() && !SkillHelper.contain(id)) {
            id = "";
        }
        stack.getOrCreateSubNbt("skill").putString("id", id);
        stack.getOrCreateSubNbt("skill").putInt("rarity", id.isEmpty() ? Skill.Rarity.COMMON.value() : SkillHelper.get(id).getRarity().value());
    }

    public static void setSkill(ItemStack stack, Skill skill) {
        if (skill == null) return;
        stack.getOrCreateSubNbt("skill").putString("id", skill.id());
        stack.getOrCreateSubNbt("skill").putInt("rarity", skill.getRarity().value());
    }

    public static void setLevel(ItemStack stack, int level) {
        stack.getOrCreateSubNbt("skill").putInt("level", Math.max(1, Math.min(level, 100)));
    }

    public static Skill getSkill(ItemStack stack) {
        return stack.hasNbt() && stack.getNbt().getCompound("skill").contains("id") ? SkillHelper.get(stack.getNbt().getCompound("skill").getString("id")) : null;
    }

    public static int getLevel(ItemStack stack) {
        return stack.hasNbt() && stack.getNbt().getCompound("skill").contains("level") ? stack.getNbt().getCompound("skill").getInt("level") : 0;
    }

    public static int getSkillRarity(ItemStack stack) {
        return stack.hasNbt() && stack.getNbt().getCompound("skill").contains("rarity") ? stack.getNbt().getCompound("skill").getInt("rarity") : 0;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient) {
            if (getSkill(stack) == null) {
                Skill skill = randomChooseSkill();
                setSkill(stack, skill);
                setLevel(stack, randomLevel());
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        Skill skill = getSkill(stack);
        int level = getLevel(stack);
        if (skill != null) {
            tooltip.add(Text.translatable(skill.getTranslateKey()).append(" [").append(Text.literal("" + level).formatted(Formatting.AQUA)).append("]"));
        }
    }

    public Skill randomChooseSkill() {
        List<Integer> chances = List.of(1, 2, 5, 10, 20);
        Random random = new Random();
        Skill.Rarity rarity = Skill.Rarity.COMMON;
        AbilitiesMod.LOGGER.info("");
        int chance = random.nextInt(100);
        for (int i = chances.size() - 1; i >= 0; i--) {
            Skill.Rarity r = Skill.Rarity.of(i);
            AbilitiesMod.LOGGER.info(r.value() + " (" + 100/chances.get(i) + " >= " + chance);
            if (100 / chances.get(i) >= chance) {
                rarity = r;
                break;
            }
        }
        List<Skill> skills = SkillHelper.getSkillsWithRarity(rarity);
        while (skills.isEmpty()) {
            if (rarity == Skill.Rarity.COMMON) break;
            rarity = Skill.Rarity.of(rarity.value() - 1);
            skills = SkillHelper.getSkillsWithRarity(rarity);
        }
        return skills.isEmpty() ? ModSkills.FIRE_RESISTANCE : skills.get(random.nextInt(skills.size()));
    }

    public int randomLevel() {
        Random random = new Random();
        int chance = random.nextInt(100);
        if (chance <= 3) {
            return random.nextInt(31) + 20;
        } else if (chance <= 10) {
            return random.nextInt(11) + 10;
        } else if (chance <= 30) {
            return random.nextInt(8) + 3;
        } else {
            return random.nextInt(3) + 1;
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        IPlayerMixin cap = (IPlayerMixin) player;
        if (world.isClient || !ModMagics.contain(cap.get(DataKeys.MAGIC))) {
            return TypedActionResult.fail(player.getStackInHand(hand));
        }
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getNbt() == null || stack.getNbt().getCompound("skill").isEmpty()) {
            return TypedActionResult.fail(player.getStackInHand(hand));
        }
        String id = stack.getNbt().getCompound("skill").getString("id");
        int level = stack.getNbt().getCompound("skill").getInt("level");
        if (SkillHelper.getData(cap, id, SkillHelper.Keys.LEVEL) > level || !ModMagics.get(cap.get(DataKeys.MAGIC)).getAll().contains(SkillHelper.get(id))) {
            return TypedActionResult.fail(player.getStackInHand(hand));
        }
        SkillHelper.upLevel(cap, id, level - SkillHelper.getData(cap, id, SkillHelper.Keys.LEVEL));
        player.setStackInHand(hand, new ItemStack(Items.BOOK, 1));
        return TypedActionResult.success(player.getStackInHand(hand));
    }


    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = super.getDefaultStack();
        stack.getNbt().getCompound("skill").putString("id", "");
        stack.getNbt().getCompound("skill").putInt("level", 0);
        stack.getNbt().getCompound("skill").putInt("rarity", Skill.Rarity.COMMON.value());
        return stack;
    }
}
