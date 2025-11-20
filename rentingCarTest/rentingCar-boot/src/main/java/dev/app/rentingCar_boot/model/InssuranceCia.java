package dev.app.rentingCar_boot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import dev.app.rentingCar_boot.utils.GenerateUUID;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class InssuranceCia {

    @Id
    private String id;
    private String name;
    private String description;
    private int qtyEmployee;
    private boolean isActive;

    /*@JsonBackReference  // This marks the "back" part of the reference (the child side) and
                        // prevents it from being serialized
    @OneToMany(mappedBy = "inssuranceCia", cascade = CascadeType.ALL)
    private List<Car> cars;
     */

    // Implement @ElementCollection for Delegations in InsuranceCia
    @ElementCollection
    @CollectionTable(name = "INSURANCECIA_DELEGATIONS",
                    joinColumns = @JoinColumn(name = "INSURANCECIA_FK"))
    @Column (name = "DELEGATION")
    private List<String> delegations = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy= "inssuranceCia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InsuranceContract> insuranceContracts = new ArrayList<>();

    public InssuranceCia() {
        this.id = GenerateUUID.generateFourDigitUuid();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQtyEmployee() {
        return qtyEmployee;
    }

    public void setQtyEmployee(int qtyEmployee) {
        this.qtyEmployee = qtyEmployee;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<String> getDelegations() {
        return delegations;
    }

    /*public void setDelegations(List<String> delegations) {
        this.delegations = delegations;
    }*/
    public void setDelegations(List<String> delegations) {
        this.delegations.clear();
        if (delegations != null) {
            this.delegations.addAll(delegations);
        }
    }



    public List<InsuranceContract> getInsuranceContracts() {
        return insuranceContracts;
    }

    public void setInsuranceContracts(List<InsuranceContract> insuranceContracts) {
        this.insuranceContracts = insuranceContracts;
    }
    /*public List<Car> getCars() {
        return cars;
    }
    public void setCars(List<Car> cars) {
        this.cars = cars;
    }*/

    @Override
    public String toString() {
        return "InssuranceCia{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", qtyEmployee=" + qtyEmployee +
                ", isActive=" + isActive +
                //", delegations= "+ delegations +
                //", insuranceContracts= "+ insuranceContracts+
                //", car= "+ cars +
                '}';
    }

    // methods helpers (optionals)
    public void addInsuranceContract(InsuranceContract contract) {
        insuranceContracts.add(contract);
        contract.setInssuranceCia(this);
    }

    public void removeInsuranceContract(InsuranceContract contract) {
        insuranceContracts.remove(contract);
        contract.setInssuranceCia(null);
    }
}
