package dev.app.rentingCar_boot.repository;

import dev.app.rentingCar_boot.model.Car;
import dev.app.rentingCar_boot.model.InssuranceCia;
import dev.app.rentingCar_boot.model.InsuranceContract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsuranceContractRepository extends JpaRepository<InsuranceContract, String> {
    // search Insurance contracts by Car
    List<InsuranceContract> findByCar(Car car);

    // Search contracts by CarId
    List<InsuranceContract> findByCar_Id(String carId);

    // Search contracts by Inssurance Companies
    List<InsuranceContract> findByInssuranceCia(InssuranceCia inssuranceCia);

    // Search contracts by InssuranceCiaId
    List<InsuranceContract> findByInssuranceCia_Id(String InssuranceCiaId);
}
