package robot.abilities.util;

import java.text.DecimalFormat;

public class Constants {
    public static double getMpPointsLimit(IPlayerMixin cap) {
        int level = cap.get(DataKeys.MP_LEVEL);
        return Double.parseDouble(new DecimalFormat("#.##").format(Math.pow(level, 1.07) + 10).replace(',', '.'));
    }

    public static double getMpMax(IPlayerMixin cap) {
        int level = cap.get(DataKeys.MP_LEVEL);
        return Double.parseDouble(new DecimalFormat("#.##").format(Math.pow(level, 1.04) + 10).replace(',', '.'));
    }
}
