package robot.abilities.util;

public class Constants {
    public final static int[] experienceLimits = {15, 37, 60, 95, 154, 234, 328};
    public final static int[] manaLimits = {10, 14, 17, 22, 30, 40, 50, 63, 75};

    public static int getExperienceLimit(IPlayerMixin cap) {
        int level = cap.get(DataKeys.LEVEL);
        return getExperienceLimit(level);
    }

    public static int getExperienceLimit(int level) {
        //return (int) (7 * Math.pow(1.9, Math.max(level - 1, 0)) + 3);
        return experienceLimits[Math.min(level, experienceLimits.length - 1)];
    }

    public static double getManaLimit(IPlayerMixin cap) {
        int level = cap.get(DataKeys.LEVEL);
        return getManaLimit(level);
    }

    public static double getManaLimit(int level) {
        //return Double.parseDouble(Utils.decimal("#.##", Math.pow(level, 1.04) + 10));
        return manaLimits[Math.min(level, manaLimits.length - 1)];
    }
}
