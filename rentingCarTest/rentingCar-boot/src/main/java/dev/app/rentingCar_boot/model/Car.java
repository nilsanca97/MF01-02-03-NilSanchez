package dev.app.rentingCar_boot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.app.rentingCar_boot.utils.GenerateUUID;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
public class Car {

    @Id
    private String id;
    private String brand;
    private String model;
    private String plate;
    @Column(name = "car_year")
    private int year;
    private double price;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "inssurance_cia_id")
    private InssuranceCia inssuranceCia;

    @OneToMany(mappedBy= "carFK" , cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CarExtras> carExtras = new ArrayList<>();

    //@ElementCollection(fetch = FetchType.LAZY)
    @JsonIgnore
    @OneToMany(mappedBy= "car" , cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Booking> bookings = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "car_available_dates", joinColumns = @JoinColumn(name = "car_id"))
    @MapKeyColumn(name = "date_key")
    @Column(name = "is_available")
    private Map<Integer, Boolean> availableDates = new HashMap<>();

    public Car() {
        this.id = GenerateUUID.generateFourDigitUuid();
    }

    public Car(String brand, String model, String plate, int year, double price) {
        this.id = GenerateUUID.generateFourDigitUuid();
        this.brand = brand;
        this.model = model;
        this.plate = plate;
        this.year = year;
        this.price = price;
    }

    public Car(String id) {
        this.id = id;
        this.carExtras = new ArrayList<>();
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int carAge() {
        return 2025 - year;
    }

    public List<CarExtras> getCarExtras() {
        return carExtras;
    }

    public void setCarExtras(List<CarExtras> carExtras) {
        this.carExtras = carExtras;
    }

    public InssuranceCia getInssuranceCia() {
        return inssuranceCia;
    }

    public void setInssuranceCia(InssuranceCia inssuranceCia) {
        this.inssuranceCia = inssuranceCia;
    }

    public Map<Integer, Boolean> getAvailableDates() {
        return availableDates;
    }

    public void setAvailableDates(Map<Integer, Boolean> availableDates) {
        this.availableDates = availableDates;
    }



    @Override
    public String toString() {
        return "Car{" +
                "id='" + id + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", plate='" + plate + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", carAge=" + carAge() +
                ", inssuranceCia=" + (inssuranceCia != null ? inssuranceCia.getName() : "null") +
                ", carExtras=" + carExtras.size() + " extras" +
                ", bookings=" + bookings.size() + " bookings [" +
                String.join(", ", bookings.stream().map(Booking::getId).toList()) + "]" +
                ", availabilityRanges=" + formatAvailabilityRanges() +
                '}';
    }

    // -------------------------- UTIL to formatAvailabilityRanges() ---------------------------------

    /**
     * Formats available dates into ranges showing available and booked periods
     * @return String representation of availability ranges
     */
    private String formatAvailabilityRanges() {
        if (availableDates == null || availableDates.isEmpty()) {
            return "no dates configured";
        }

        // Sort dates and separate by availability status
        List<Integer> sortedDates = availableDates.keySet().stream()
                .sorted()
                .collect(Collectors.toList());

        if (sortedDates.isEmpty()) {
            return "no dates";
        }

        StringBuilder result = new StringBuilder();
        result.append("\n");
        
        // Create a list of all ranges with their start timestamps for chronological sorting
        List<RangeInfo> allRanges = new ArrayList<>();
        int rangeCounter = 1;
        
        // Process dates and group consecutive ones by status
        Integer rangeStart = null;
        Integer rangeEnd = null;
        Boolean currentStatus = null;
        
        for (int currentTimestamp : sortedDates) {
            boolean dateStatus = availableDates.get(currentTimestamp);
            
            if (rangeStart == null) {
                // First date
                rangeStart = currentTimestamp;
                rangeEnd = currentTimestamp;
                currentStatus = dateStatus;
            } else if (dateStatus == currentStatus && currentTimestamp == rangeEnd + 86400) {
                // Continue current range (consecutive day - 86400 seconds apart)
                rangeEnd = currentTimestamp;
            } else {
                // End current range and add to list
                String range = formatDateRange(rangeStart, rangeEnd);
                String status = currentStatus ? "Available" : "Booked";
                allRanges.add(new RangeInfo(rangeStart, "Range#" + rangeCounter++ + ": " + range + " " + status));
                
                // Start new range
                rangeStart = currentTimestamp;
                rangeEnd = currentTimestamp;
                currentStatus = dateStatus;
            }
        }
        
        // Add the last range
        if (rangeStart != null) {
            String range = formatDateRange(rangeStart, rangeEnd);
            String status = currentStatus ? "Available" : "Booked";
            allRanges.add(new RangeInfo(rangeStart, "Range#" + rangeCounter++ + ": " + range + " " + status));
        }
        
        // Sort by start timestamp and append to result with line breaks
        allRanges.sort((a, b) -> Integer.compare(a.startTimestamp, b.startTimestamp));
        for (RangeInfo rangeInfo : allRanges) {
            result.append(rangeInfo.formattedRange).append("\n");
        }

        return result.toString();
    }

    /**
     * Formats a date range from Unix timestamps to readable dd/MM/yyyy format
     * @param startTimestamp start date in Unix timestamp (seconds)
     * @param endTimestamp end date in Unix timestamp (seconds)
     * @return formatted date range string
     */
    private String formatDateRange(int startTimestamp, int endTimestamp) {
        // Convert Unix timestamps to LocalDate
        LocalDate startDate = Instant.ofEpochSecond(startTimestamp).atZone(ZoneOffset.UTC).toLocalDate();
        LocalDate endDate = Instant.ofEpochSecond(endTimestamp).atZone(ZoneOffset.UTC).toLocalDate();

        // Format as dd/MM/yyyy
        String startFormatted = String.format("%02d/%02d/%d", 
            startDate.getDayOfMonth(), startDate.getMonthValue(), startDate.getYear());
        String endFormatted = String.format("%02d/%02d/%d", 
            endDate.getDayOfMonth(), endDate.getMonthValue(), endDate.getYear());

        if (startTimestamp == endTimestamp) {
            return startFormatted;
        } else {
            return startFormatted + " - " + endFormatted;
        }
    }

    /**
     * Helper class for chronological sorting of date ranges
     */
    private static class RangeInfo {
        final int startTimestamp;
        final String formattedRange;
        
        RangeInfo(int startTimestamp, String formattedRange) {
            this.startTimestamp = startTimestamp;
            this.formattedRange = formattedRange;
        }
    }
}
