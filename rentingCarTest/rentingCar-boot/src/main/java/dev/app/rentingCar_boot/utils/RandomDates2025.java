package dev.app.rentingCar_boot.utils;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class RandomDates2025 {

    private LocalDate startDate;
    private LocalDate endDate;

    // Constructor vacío
    public RandomDates2025() {}

    // Constructor que genera fechas aleatorias dentro de 2025
    public RandomDates2025 generateRandomDates2025() {
        LocalDate startOfYear = LocalDate.of(2025, 1, 1);
        LocalDate endOfYear = LocalDate.of(2025, 12, 31);

        long minDay = startOfYear.toEpochDay();
        long maxDay = endOfYear.toEpochDay();

        long randomStartDay = ThreadLocalRandom.current().nextLong(minDay, maxDay + 1);
        LocalDate startDate = LocalDate.ofEpochDay(randomStartDay);

        long randomEndDay = ThreadLocalRandom.current().nextLong(randomStartDay, maxDay + 1);
        LocalDate endDate = LocalDate.ofEpochDay(randomEndDay);

        this.startDate = startDate;
        this.endDate = endDate;

        return this;
    }

    // Getters y Setters
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    // toString
    @Override
    public String toString() {
        return "ContractDate {" +
               "startDate=" + startDate +
               ", endDate=" + endDate +
              '}';
    }
}