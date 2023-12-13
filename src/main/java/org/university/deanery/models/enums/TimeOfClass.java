package org.university.deanery.models.enums;

public enum TimeOfClass {
    CLASS_CANCELED(0),
    FIRST(1),
    SECOND(2),
    THIRD(3),
    FOURTH(4),
    FIFTH(5),
    SIXTH(6),
    SEVENTH(7),
    EIGHTH(8);

    private final int code;

    TimeOfClass(int code) {
        this.code = code;
    }

    public static TimeOfClass toTimeOfClass(int code) {
        return switch (code) {
            case 1 -> FIRST;
            case 2 -> SECOND;
            case 3 -> THIRD;
            case 4 -> FOURTH;
            case 5 -> FIFTH;
            case 6 -> SIXTH;
            case 7 -> SEVENTH;
            case 8 -> EIGHTH;
            default -> null;
        };
    }

    public int getCode() {
        return code;
    }
}
