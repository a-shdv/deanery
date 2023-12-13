package org.university.deanery.models.enums;

public enum DayOfWeek {
    SUNDAY(0),
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6);
    private final int code;

    DayOfWeek(int code) {
        this.code = code;
    }

    public static DayOfWeek toDayOfWeek(int dayOfWeekId) {
        return switch (dayOfWeekId) {
            case 0 -> SUNDAY;
            case 1 -> MONDAY;
            case 2 -> TUESDAY;
            case 3 -> WEDNESDAY;
            case 4 -> THURSDAY;
            case 5 -> FRIDAY;
            case 6 -> SATURDAY;
            default -> null;
        };
    }

    public int getCode() {
        return code;
    }
}
