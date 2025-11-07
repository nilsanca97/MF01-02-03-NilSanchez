package dev.app.rentingCar_boot.repository;

import dev.app.rentingCar_boot.model.InsuranceContract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsuranceContractRepository extends JpaRepository<InsuranceContract, String> {

    // Search contracts by CarId
    List<InsuranceContract> findByCar_Id(String carId);

    // Search contracts by InssuranceCiaId
    List<InsuranceContract> findByInssuranceCia_Id(String InssuranceCiaId);
}
