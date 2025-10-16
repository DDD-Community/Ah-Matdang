package be.ddd.domain.entity.crawling;

public enum BeverageType {
    ESPRESSO,
    COLD_BREW,
    FRAPPUCCINO,
    BLENDED,
    TEA,
    REFRESHER,
    FIZZIO,
    OTHERS,
    JUICE_YOGURT,
    COFFEE, // Added for general coffee category
    SMOOTHIE_FRAPPE, // Added for smoothie and frappe types
    ADE_JUICE, // Added for ade and juice types
    CHOCOLATE, // Added for chocolate beverages
    ANY;

    public static BeverageType fromString(String typeString) {
        if (typeString == null || typeString.trim().isEmpty()) {
            return OTHERS; // Default for null or empty strings
        }
        String formattedType = typeString.toUpperCase().replace(" ", "_").replace("/", "_");
        try {
            return BeverageType.valueOf(formattedType);
        } catch (IllegalArgumentException e) {
            return OTHERS; // Default for unknown strings
        }
    }
}
