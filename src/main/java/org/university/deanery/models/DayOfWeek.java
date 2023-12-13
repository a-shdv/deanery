package org.university.deanery.models;

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

    public int getCode() {
        return code;
    }

    public static DayOfWeek toDayOfWeek(int dayOfWeekId) {
        switch (dayOfWeekId) {
            case 0:
                return SUNDAY;
            case 1:
                return MONDAY;
            case 2:
                return TUESDAY;
            case 3:
                return WEDNESDAY;
            case 4:
                return THURSDAY;
            case 5:
                return FRIDAY;
            case 6:
                return SATURDAY;
        }
        return null;
    }
}
