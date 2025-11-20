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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

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
        myInsuranceContract.setContractId("1188");
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

    @Transactional
    @Test
    void createInsuranceContractAfterPopulateAllTables() {
        //Step 1: Execute PopulateAllTables to ensure we have data
        String populateResult = populateAllTables.populateAllTables(5);
        System.out.println("Populate result: "+ populateResult);

        //Step 2: Verify we have cars and insurance companies availabe
        List<Car> availableCars = (List<Car>) carRepository.findAll();
        List<InssuranceCia> availableInssuranceCias = (List<InssuranceCia>) inssuranceCiaRepository.findAll();

        assertFalse(availableCars.isEmpty(), "Should have cars after population");
        assertFalse(availableInssuranceCias.isEmpty(), "Should have inssurance companies after population");

        System.out.println("Available cars: " + availableCars.size());
        System.out.println("Available inssuranceCompanies: "+ availableInssuranceCias.size());

        // Step 3: Create a new booking using populated data
        Car selectedCar = availableCars.get(0); // Get first available car
        InssuranceCia selectedInssuranceCia = availableInssuranceCias.get(0); //Get first available inssuranceCia

        // Create InsuranceContract with current date as a LocalDateFormat
        LocalDate startDate = DateUtils.parseDate("17/11/2025");
        LocalDate endDate = DateUtils.parseDate("01/12/2025");

        InsuranceContract newInsuranceContract = new InsuranceContract(
                selectedCar,
                selectedInssuranceCia,
                startDate,
                endDate
        );
        System.out.println("CIA before save = " + newInsuranceContract.getInssuranceCia());

        //step 4: Save the new insuranceContract
        InsuranceContract savedInsuranceContract = insuranceContractRepository.save(newInsuranceContract);
        System.out.println("CIA after save = " + savedInsuranceContract.getInssuranceCia());

        //step 5: Verify the insuranceContract was created successfully
        assertNotNull(savedInsuranceContract);
        assertNotNull(savedInsuranceContract.getContractId());
        assertNotNull(savedInsuranceContract.getCar().getId());
        assertNotNull(savedInsuranceContract.getInssuranceCia());
        assertNotNull(savedInsuranceContract.getStartDate());
        assertNotNull(savedInsuranceContract.getEndDate());

        System.out.println("New insuranceContract created successfully: "); //savedInsuranceContract
        System.out.println("InsuranceContract ID=" + savedInsuranceContract.getContractId());
        System.out.println("Car ID=" + savedInsuranceContract.getCar().getId());
        System.out.println("Cia ID=" + savedInsuranceContract.getInssuranceCia().getId());

        //Step 6: Verify the insuranceContract exists in database
        Optional<InsuranceContract> retievedInsuranceContract = insuranceContractRepository.findById(savedInsuranceContract.getContractId());
        assertTrue(retievedInsuranceContract.isPresent(), "InsuranceContract should exist in database");

        //Step 7: Verify the car now has this insuranceContract in its insuranceContracts list
        Car updatedCar = carRepository.findById(selectedCar.getId()).get();
        boolean insuranceContractFoundInCar = updatedCar.getInsuranceContracts().stream()
                .anyMatch((insuranceContract -> insuranceContract.getContractId().equals(savedInsuranceContract.getContractId())));
        assertTrue(insuranceContractFoundInCar, "Car should contain the new insuranceContract in its insuranceContracts list");

        System.out.println("Test completed successfully - InsuranceContract created after PopulateAllTables execution");
    }


}
