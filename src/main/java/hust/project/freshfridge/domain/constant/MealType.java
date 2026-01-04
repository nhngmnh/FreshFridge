package hust.project.freshfridge.domain.constant;

public enum MealType {
    SANG("sáng"),
    TRUA("trưa"),
    TOI("tối");

    private final String displayName;

    MealType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static MealType fromDisplayName(String name) {
        for (MealType type : values()) {
            if (type.displayName.equalsIgnoreCase(name) || type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid meal type: " + name);
    }
}
