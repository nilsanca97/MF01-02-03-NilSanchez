package dev.app.rentingCar_boot;

import dev.app.rentingCar_boot.model.Car;
import dev.app.rentingCar_boot.model.InssuranceCia;
import dev.app.rentingCar_boot.model.InsuranceContract;
import dev.app.rentingCar_boot.repository.CarRepository;
import dev.app.rentingCar_boot.repository.InssuranceCiaRepository;
import dev.app.rentingCar_boot.repository.InsuranceContractRepository;
import dev.app.rentingCar_boot.utils.DateUtils;
import dev.app.rentingCar_boot.utils.PopulateAllTables;
import dev.app.rentingCar_boot.utils.PopulateInsuranceContract;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@Transactional
@SpringBootTest
public class InsuranceContractTest {

    @Autowired
    InsuranceContractRepository insuranceContractRepository;
    @Autowired
    CarRepository carRepository;

    @Autowired
    InssuranceCiaRepository inssuranceCiaRepository;

    @Autowired
    PopulateInsuranceContract populateInsuranceContract;

    @Autowired
    PopulateAllTables populateAllTables;

    @Transactional
    @Test
    void insuranceContractTest() {

        Car car = carRepository.findById("5559").get();

        InssuranceCia inssuranceCia = inssuranceCiaRepository.findById("3339").get();

        InsuranceContract myInsuranceContract = new InsuranceContract();
        myInsuranceContract.setContractId("IC019");
        myInsuranceContract.setCar(car);
        myInsuranceContract.setInssuranceCia(inssuranceCia);
        myInsuranceContract.setStartDate(DateUtils.parseDate("12-11-2025"));
        myInsuranceContract.setEndDate(DateUtils.parseDate("01-12-2025"));

        insuranceContractRepository.save(myInsuranceContract);

        System.out.println("InsuranceContract: "+ myInsuranceContract);

        System.out.println("InsuranceContract --from DB--: "+ insuranceContractRepository.findById("IC019").get());
        System.out.println("Car --from DB--: "+ carRepository.findById("5225"));
        System.out.println("InssuranceCia --from DB--: "+ inssuranceCiaRepository.findById("3339"));
    }

    @Test
    void setPopulateInsuranceContractTest() {
        populateInsuranceContract.populateInsuranceContract(10);
    }


}
