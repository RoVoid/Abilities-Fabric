package robot.abilities.util;

import robot.abilities.magic.skill.Skill;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public final static int[] experienceLimits = {15, 37, 60, 95, 154, 234, 328};
    public final static int[] manaLimits = {10, 14, 17, 22, 30, 40, 50, 63, 75};
    public final static int[] grandPoints = {10, 12, 18, 24, 29, 34, 37, 42, 46};
    public final static Map<Skill.Rarity, Integer> rarityPrices = new HashMap<>();

    static {
        rarityPrices.put(Skill.Rarity.COMMON, 1);
        rarityPrices.put(Skill.Rarity.RARE, 2);
        rarityPrices.put(Skill.Rarity.EPIC, 3);
        rarityPrices.put(Skill.Rarity.LEGENDARY, 4);
    }

    public static int getLevel(IPlayerMixin cap) {
        int exp = cap.get(DataKeys.EXPERIENCE);
        for (int level = 0; level < experienceLimits.length - 1; level++) {
            if (exp < experienceLimits[level]) return level + 1;
        }
        return experienceLimits.length + exp / experienceLimits[experienceLimits.length - 1] - 1;
    }

    public static int getExperienceLimit(IPlayerMixin cap) {
        return getExperienceLimit(cap.get(DataKeys.LEVEL));
    }

    public static int getExperienceLimit(int level) {
        if (level <= 0) return 0;
        return experienceLimits[Math.min(level - 1, experienceLimits.length - 1)];
    }

    public static double getManaLimit(IPlayerMixin cap) {
        return getManaLimit(cap.get(DataKeys.LEVEL));
    }

    public static double getManaLimit(int level) {
        if (level <= 0) return 0;
        return manaLimits[Math.min(level - 1, manaLimits.length - 1)];
    }

    public static int getGrandPoints(IPlayerMixin cap) {
        return getGrandPoints(cap.get(DataKeys.LEVEL));
    }

    public static int getGrandPoints(int level) {
        if (level - 2 < 0) return 0;
        return grandPoints[Math.min(level - 2, grandPoints.length - 1)];
    }

    public static int getRarityPrice(Skill.Rarity rarity) {
        return rarityPrices.getOrDefault(rarity, 1);
    }
}
