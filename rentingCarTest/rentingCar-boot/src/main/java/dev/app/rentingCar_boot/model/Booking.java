package dev.app.rentingCar_boot.model;

import dev.app.rentingCar_boot.utils.GenerateUUID;
import jakarta.persistence.*;

@Entity
public class Booking {

    @Id
    private String id;
    private int bookingDate;
    private int qtyDays;
    private double totalAmount;
    private boolean isActive;

    @JoinColumn(name = "CAR_FK")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Car car;

    @JoinColumn(name = "CLIENT_FK")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Client client;

    public Booking() {
        this.id = GenerateUUID.generateFourDigitUuid();
    }

    public Booking(int bookingDate, int qtyDays, double totalAmount, boolean isActive, Car car, Client client) {
        this.id = GenerateUUID.generateFourDigitUuid();
        this.bookingDate = bookingDate;
        this.qtyDays = qtyDays;
        this.totalAmount = totalAmount;
        this.isActive = isActive;
        this.car = car;
        this.client = client;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(int bookingDate) {
        this.bookingDate = bookingDate;
    }

    public int getQtyDays() {
        return qtyDays;
    }

    public void setQtyDays(int qtyDays) {
        this.qtyDays = qtyDays;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id='" + id + '\'' +
                ", bookingDate=" + bookingDate +
                ", qtyDays=" + qtyDays +
                ", totalAmount=" + totalAmount +
                ", isActive=" + isActive +
                ", car=" + (car != null ? car.getBrand() + " " + car.getModel() + " (" + car.getId() + ")" : "null") +
                ", client=" + (client != null ? client.getName() + " " + client.getLastName() + " (" + client.getId() + ")" : "null") +
                '}';
    }
}
