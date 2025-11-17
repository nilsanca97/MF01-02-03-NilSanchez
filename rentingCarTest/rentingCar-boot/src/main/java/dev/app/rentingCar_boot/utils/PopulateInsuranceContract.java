package dev.app.rentingCar_boot.utils;

import dev.app.rentingCar_boot.model.Car;
import dev.app.rentingCar_boot.model.InssuranceCia;
import dev.app.rentingCar_boot.model.InsuranceContract;
import dev.app.rentingCar_boot.repository.CarRepository;
import dev.app.rentingCar_boot.repository.InssuranceCiaRepository;
import dev.app.rentingCar_boot.repository.InsuranceContractRepository;
import dev.app.rentingCar_boot.service.PopulateInsuranceContractService;
import dev.app.rentingCar_boot.utils.PopulateStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class PopulateInsuranceContract implements PopulateInsuranceContractService {

    @Autowired
    private InsuranceContractRepository insuranceContractRepository;

    @Autowired
    CarRepository carRepository;

    @Autowired
    InssuranceCiaRepository inssuranceCiaRepository;

    @Transactional
    public PopulateStatus populateInsuranceContract (int qty) {
        StringBuilder messageBuilder = new StringBuilder();
        boolean[] operationResults = new boolean[2];
        int operationIndex = 0;

        try {
            // Operation 1: Generate main Insurance Contracts
            List<InsuranceContract> insuranceContracts = generateInsuranceContracts(qty);
            operationResults[operationIndex] = insuranceContracts != null && insuranceContracts.size() == qty;
            messageBuilder.append(" Operation 1: Generated ").append(insuranceContracts != null ? insuranceContracts.size() : 0)
                    .append(" insurance contracts (requested: ").append(qty).append(")\n");
            operationIndex++;

            // Operation 2: Generate additional unassigned Insurance Contracts
            List<InsuranceContract> additionalInsuranceContracts = generateInsuranceContracts(10);
            operationResults[operationIndex] = additionalInsuranceContracts != null && additionalInsuranceContracts.size() == 10;
            messageBuilder.append(" Operation 2: Generated ").append(additionalInsuranceContracts != null ? additionalInsuranceContracts.size() : 0)
                    .append(" additional insurance contracts (requested: 10)\n");

        } catch (Exception e) {
            //Mark current and remaining operations as failed
            for (int i = operationIndex; i < 2; i++) {
                operationResults[i] = false;
            }
            messageBuilder.append("Error occurred during operation")
                    .append(operationIndex + 1)
                    .append(": ").append(e.getMessage()).append("\n");

        }
        // Check if all operations succeeded
        boolean allSuccess = true;
        for (boolean result : operationResults) {
            if (!result) {
                allSuccess = false;
                break;
            }
        }
        // Calculate total quantity (main qty + 10 additional entities)
        int totalQty = qty + 10;

        return new PopulateStatus(allSuccess, messageBuilder.toString().trim(), totalQty);
    }

    //Helper method that creates insurance contracts and persists them.
    public List<InsuranceContract> generateInsuranceContracts(int qty) {
        List<InsuranceContract> generatedInsuranceContracts = new ArrayList<>();
        Random random = new Random();
        RandomDates2025 randomDates2025 = new RandomDates2025().generateRandomDates2025();

        for (int i = 0; i < qty; i++) {
            String id = "IC" + String.format("%03d", i + 1);

            // Generate random contract date
            LocalDate startDate = randomDates2025.getStartDate();
            LocalDate endDate = randomDates2025.getEndDate();

            InsuranceContract insuranceContract = new InsuranceContract(null, null, startDate, endDate);
            generatedInsuranceContracts.add(insuranceContract);
            insuranceContractRepository.save(insuranceContract);
        }

        return generatedInsuranceContracts;
    }

    public void assignCarAndInssuranceCiaToInsuranceContract(List<InsuranceContract> insuranceContracts) {
        Random random = new Random();

        // Get all available cars and inssuranceCias from db
        List<Car> availableCars = (List<Car>) carRepository.findAll();
        List<InssuranceCia> availableInssuranceCias = (List<InssuranceCia>) inssuranceCiaRepository.findAll();

        for (InsuranceContract insuranceContract : insuranceContracts) {
            // Assign random car
            Car randomCar = availableCars.get(random.nextInt(availableCars.size()));
            insuranceContract.setCar(randomCar);

            //Assign random inssuranceCia
            InssuranceCia randomInssuranceCia = availableInssuranceCias.get(random.nextInt(availableInssuranceCias.size()));
            insuranceContract.setInssuranceCia(randomInssuranceCia);

            insuranceContractRepository.save(insuranceContract);
        }
    }
}
