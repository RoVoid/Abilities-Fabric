package robot.abilities.event;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;
import robot.abilities.magic.skill.SkillEnchantment;
import robot.abilities.magic.skill.SkillHelper;
import robot.abilities.util.Utils;

import java.util.List;
import java.util.Map;

public class ItemEvents implements UseItemCallback, ItemTooltipCallback {
    @Override
    public TypedActionResult<ItemStack> interact(PlayerEntity player, World world, Hand hand) {
        ItemStack item = player.getStackInHand(hand);
        if (item.isEmpty()) return TypedActionResult.pass(player.getStackInHand(hand));
        if (player.getItemCooldownManager().isCoolingDown(item.getItem()))
            return TypedActionResult.fail(player.getStackInHand(hand));
        List<SkillEnchantment> enchantments = SkillHelper.getEnchantments();
        Map<Enchantment, Integer> itemEnchantments = EnchantmentHelper.get(item);
        for (SkillEnchantment enchantment : enchantments) {
            if (!itemEnchantments.containsKey(enchantment) || itemEnchantments.getOrDefault(enchantment, 0) <= 0)
                continue;
            if (enchantment.onUsed(player, itemEnchantments.get(enchantment))) {
                item.damage(1, player, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
                player.getItemCooldownManager().set(item.getItem(), 10);
            }
        }
        return TypedActionResult.pass(player.getStackInHand(hand));
    }

    @Override
    public void getTooltip(ItemStack stack, TooltipContext tooltip, List<Text> lines) {
        if (Utils.isPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            List<SkillEnchantment> enchantments = SkillHelper.getEnchantments();
            Map<Enchantment, Integer> itemEnchantments = EnchantmentHelper.get(stack);
            for (SkillEnchantment enchantment : enchantments) {
                if (itemEnchantments.containsKey(enchantment)) {
                    lines.add(Text.translatable(enchantment.getTranslateKey() + ".tooltip"));
                }
            }
        }
    }
}
