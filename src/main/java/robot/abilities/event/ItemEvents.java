package robot.abilities.event;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import robot.abilities.magic.skill.AbstractSkill;
import robot.abilities.magic.skill.ModSkills;
import robot.abilities.magic.skill.SkillEnchantment;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ItemEvents implements UseItemCallback {
    @Override
    public TypedActionResult<ItemStack> interact(PlayerEntity player, World world, Hand hand) {
        ItemStack item = player.getStackInHand(hand);
        if (item.isEmpty()) return TypedActionResult.pass(player.getStackInHand(hand));
        if (player.getItemCooldownManager().isCoolingDown(item.getItem()))
            return TypedActionResult.fail(player.getStackInHand(hand));
        List<SkillEnchantment> enchantments = ModSkills.skills.values().stream()
                .map(AbstractSkill::getEnchantment)
                .filter(Objects::nonNull)
                .toList();
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
}
