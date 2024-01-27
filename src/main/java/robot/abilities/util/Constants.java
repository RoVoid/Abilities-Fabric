package robot.abilities.util;

public class Constants {
    public static double getMpPointsLimit(IPlayerMixin cap) {
        int level = cap.get(DataKeys.MP_LEVEL);
        return Math.pow(level, 1.07);
    }

    public static double getMpMax(IPlayerMixin cap) {
        int level = cap.get(DataKeys.MP_LEVEL);
        return Math.pow(level, 1.04) + 10;
    }
}
