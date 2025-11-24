package dev.app.rentingCar_boot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dev.app.rentingCar_boot.utils.GenerateUUID;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class InsuranceContract {

    @Id
    private String contractId;

    // parte Car (n:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CAR_FK")
    @JsonBackReference
    private Car car;

    // parte InssuranceCia (n:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INSSURANCECIA_FK")
    @JsonBackReference
    private InssuranceCia inssuranceCia;

    private LocalDate startDate;
    private LocalDate endDate;

    public InsuranceContract() {
        this.contractId = GenerateUUID.generateFourDigitUuid();
    }

    public InsuranceContract(Car car, InssuranceCia inssuranceCia, LocalDate startDate, LocalDate endDate ) {
        this.contractId = GenerateUUID.generateFourDigitUuid();
        this.car = car;
        this.inssuranceCia = inssuranceCia;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public InssuranceCia getInssuranceCia() {
        return inssuranceCia;
    }

    public void setInssuranceCia(InssuranceCia inssuranceCia) {
        this.inssuranceCia = inssuranceCia;
    }

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

    @Override
    public String toString() {
        return "InsuranceContract{" +
                "contractId='" + contractId + '\'' +
                //", car=" + (car != null ? car.getBrand() + " " + car.getModel() + " (" + car.getId() + ")" : "null") +
                ", insuranceCiaId=" + (inssuranceCia != null ? inssuranceCia.getId() : "null") +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                "}";
    }
    // helpers methods (optional)

    /*@JoinColumn(name = "CAR_FK")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Car car;

    @JoinColumn(name = "INSURANCE_FK")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Client client;*/
}
