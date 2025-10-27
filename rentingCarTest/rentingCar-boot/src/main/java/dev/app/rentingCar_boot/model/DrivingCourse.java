package dev.app.rentingCar_boot.model;

import dev.app.rentingCar_boot.utils.GenerateUUID;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class DrivingCourse {

    @Id
    private String id;
    private String courseName;
    private String description;
    private String instructor;
    private int durationHours;
    private double price;
    private String category; // e.g., "Basic", "Advanced", "Defensive", "Commercial"
    private int maxStudents;
    private boolean isActive;
    private String location;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "client_driving_course",
            joinColumns = @JoinColumn(name = "driving_course_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id"))
    private List<Client> clients = new ArrayList<>();

    public DrivingCourse() {
        this.id = GenerateUUID.generateFourDigitUuid();
    }

    public DrivingCourse(String courseName, String description, String instructor, 
                        int durationHours, double price, String category, 
                        int maxStudents, boolean isActive, String location) {
        this.id = GenerateUUID.generateFourDigitUuid();
        this.courseName = courseName;
        this.description = description;
        this.instructor = instructor;
        this.durationHours = durationHours;
        this.price = price;
        this.category = category;
        this.maxStudents = maxStudents;
        this.isActive = isActive;
        this.location = location;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public int getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(int durationHours) {
        this.durationHours = durationHours;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "DrivingCourse{" +
                "id='" + id + '\'' +
                ", courseName='" + courseName + '\'' +
                ", description='" + description + '\'' +
                ", instructor='" + instructor + '\'' +
                ", durationHours=" + durationHours +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", maxStudents=" + maxStudents +
                ", isActive=" + isActive +
                ", location='" + location + '\'' +
                ", clients=" + clients +
                '}';
    }
}
