package util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Provides some static methods for formatting numbers
 *
 * Created by ingo on 11/14/16.
 */

public class NumberUtil {

    public static String formatMagnitude(Double mag) {
        NumberFormat formatter = NumberFormat.getInstance();
        if (formatter instanceof DecimalFormat) {
            ((DecimalFormat) formatter).applyPattern("0.0");
        }
        return formatter.format(mag);
    } // close method formatMagnitude

} // close class NumberUtil
