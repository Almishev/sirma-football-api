package com.sirma.football_api.validation;

public final class ValidationMessages {

    private ValidationMessages() {
    }

    public static final String NOT_BLANK = "must not be blank";
    public static final String NOT_NULL = "must not be null";
    public static final String MINUTES_RANGE = "fromMinutes must be less than or equal to toMinutes (or toMinutes null)";
    public static final String MINUTES_RANGE_0_120 = "must be between 0 and 90";
    public static final String SCORE_PATTERN = "must match pattern e.g. 1-0, 5-1";
}
