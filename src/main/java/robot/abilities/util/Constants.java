package robot.abilities.util;

public class Constants {
    public final static int[] experienceLimits = {15, 37, 60, 95, 154, 234, 328};
    public final static int[] manaLimits = {10, 14, 17, 22, 30, 40, 50, 63, 75};
    public final static int[] grandPoints = {1, 1, 2, 2, 2, 3, 3, 3, 4};

    public static int getExperienceLimit(IPlayerMixin cap) {
        int level = cap.get(DataKeys.LEVEL);
        return getExperienceLimit(level);
    }

    public static int getExperienceLimit(int level) {
        if (level <= 0) return 0;
        //return (int) (7 * Math.pow(1.9, Math.max(level - 1, 0)) + 3);
        return experienceLimits[Math.min(level - 1, experienceLimits.length - 1)];
    }

    public static double getManaLimit(IPlayerMixin cap) {
        int level = cap.get(DataKeys.LEVEL);
        return getManaLimit(level);
    }

    public static double getManaLimit(int level) {
        if (level <= 0) return 0;
        //return Double.parseDouble(Utils.decimal("#.##", Math.pow(level, 1.04) + 10));
        return manaLimits[Math.min(level - 1, manaLimits.length - 1)];
    }

    public static int getGrandPoints(IPlayerMixin cap) {
        int level = cap.get(DataKeys.LEVEL);
        return getGrandPoints(level);
    }

    public static int getGrandPoints(int level) {
        if (level <= 0) return 0;
        return grandPoints[Math.min(level - 1, grandPoints.length - 1)];
    }
}
