package dev.app.rentingCar_boot.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, FORMATTER);
    }
}
