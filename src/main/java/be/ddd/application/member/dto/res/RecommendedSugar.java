package be.ddd.application.member.dto.res;

public record RecommendedSugar(int sugarMaxG, int sugarIdealG) {

    public static RecommendedSugar fromIntegerValues(Double sugarMaxG, Double sugarIdealG) {
        return new RecommendedSugar(roundToInteger(sugarMaxG), roundToInteger(sugarIdealG));
    }

    private static int roundToInteger(Double value) {
        if (value == null) {
            return 0;
        }
        return (int) Math.round(value);
    }
}
