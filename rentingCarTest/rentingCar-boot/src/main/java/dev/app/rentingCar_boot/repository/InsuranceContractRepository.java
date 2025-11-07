package dev.app.rentingCar_boot.repository;

import dev.app.rentingCar_boot.model.InsuranceContract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceContractRepository extends JpaRepository<InsuranceContract, String> {

}
