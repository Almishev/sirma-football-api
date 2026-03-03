package com.sirma.football_api.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public final class DateParseUtil {

    private static final List<DateTimeFormatter> FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("M/d/yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("d.M.yyyy"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd")
    );

    private DateParseUtil() {
    }

    public static LocalDate parse(String dateString) {
        if (dateString == null || dateString.isBlank()) {
            throw new IllegalArgumentException("Date string must not be null or blank");
        }
        String trimmed = dateString.trim();
        for (int i = 0; i < FORMATTERS.size(); i++) {
            DateTimeFormatter formatter = FORMATTERS.get(i);
            try {
                return LocalDate.parse(trimmed, formatter);
            } catch (DateTimeParseException ignored) {
            }
        }
        throw new IllegalArgumentException("Unsupported date format: '" + dateString + "'");
    }

    public static LocalDate parseOrNull(String dateString) {
        if (dateString == null || dateString.isBlank()) {
            return null;
        }
        return parse(dateString);
    }
}
