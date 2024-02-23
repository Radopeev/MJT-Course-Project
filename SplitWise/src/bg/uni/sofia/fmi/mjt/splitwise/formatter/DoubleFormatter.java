package bg.uni.sofia.fmi.mjt.splitwise.formatter;

import java.text.DecimalFormat;

public class DoubleFormatter {
    public static double divide(double numerator, double denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Cannot divide by 0");
        }

        double result = numerator / denominator;

        DecimalFormat df = new DecimalFormat("#.##");
        String formattedResult1 = df.format(result).replace(",", ".");
        return Double.parseDouble(formattedResult1);
    }
}
