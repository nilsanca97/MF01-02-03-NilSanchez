package dev.app.rentingCar_boot.utils;

import dev.app.rentingCar_boot.model.InssuranceCia;
import dev.app.rentingCar_boot.repository.CarRepository;
import dev.app.rentingCar_boot.repository.InssuranceCiaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class PopulateInssuranceCia {

    @Autowired
    private InssuranceCiaRepository inssuranceCiaRepository;

    @Transactional
    public PopulateStatus populateInssuranceCia(int qty) {
        StringBuilder messageBuilder = new StringBuilder();
        boolean[] operationResults = new boolean[2];
        int operationIndex = 0;

        try {
            //Operation 1: Generate main Inssurance companies
            List<InssuranceCia> inssuranceCias = generateInssuranceCias(qty);
            operationResults[operationIndex] = inssuranceCias != null && inssuranceCias.size() == qty;
            messageBuilder.append(" Operation 1: Generated ").append(inssuranceCias != null ? inssuranceCias.size() : 0)
                    .append(" insurance companies (requested: ").append(qty).append(")\n");
            operationIndex++;

            // Operation 2: Generate additional unassigned Inssurance companies
            List<InssuranceCia> additionalInssuranceCias = generateInssuranceCias(10);
            operationResults[operationIndex] = additionalInssuranceCias != null && additionalInssuranceCias.size() == 10;
            messageBuilder.append(" Operation 2: Generated ").append(additionalInssuranceCias != null ? additionalInssuranceCias.size() : 0)
                    .append(" additional insurance companies (requested: 10)\n");
        } catch (Exception e) {
            // Mark current and remaining operations as failed
            for (int i = operationIndex; i < 2; i++) {
                operationResults[i] = false;
            }
            messageBuilder.append("Error occurred during operation ")
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
        int totalQty = qty +10;

        return new PopulateStatus(allSuccess, messageBuilder.toString().trim(), totalQty);
    }

    //Helper method that creates insurance companies and persists them.
    public List<InssuranceCia> generateInssuranceCias(int qty){

        List<InssuranceCia> generatedInssuranceCias = new ArrayList<>();
        //List<InssuranceCia> generatedList= generateInssuranceCias(qty);
        Random random = new Random();

        String[] companyNames = {"State Farm", "Geico", "Progressive", "Allstate", "Liberty Mutual",
                "USAA", "Farmers", "Nationwide", "American Family", "Travelers"};

        String[] descriptions = {
                "Comprehensive auto insurance with excellent customer service",
                "Affordable car insurance with 24/7 claims support",
                "Innovative insurance solutions with competitive rates",
                "Full coverage auto insurance with roadside assistance",
                "Trusted insurance provider with nationwide coverage",
                "Premium insurance services for military families",
                "Local insurance expertise with personal touch",
                "Reliable coverage with accident forgiveness programs",
                "Family-focused insurance with multi-policy discounts",
                "Professional insurance services with quick claims processing"
        };
        // add a list of delegations
        List<List<String>> delegationsList = List.of(
                List.of("Barcelona Office\nCarrer Balmes, 123", "Madrid Office\nCalle Embajadores, 45"),
                List.of("Bilbao Office\nCalle Gran Via, 87", "Sevilla Office\nPlaza Mayor, 1"),
                List.of("Valencia Office\nAvenida del Puerto, 56"),
                List.of("Zaragoza Office\nCalle del Pilar, 12", "Granada Office\nCamino Real, 9"),
                List.of("Málaga Office\nCalle Larios, 22"),
                List.of("Alicante Office\nCalle Mayor, 33"),
                List.of("Valladolid Office\nCalle Zorrilla, 14"),
                List.of("Murcia Office\nCalle Trapería, 45"),
                List.of("Santander Office\nPaseo Pereda, 6"),
                List.of("Oviedo Office\nCalle Uría, 18")
        );

        // generate random InssuranceCia
        for (int i = 0; i < qty; i++) {
            InssuranceCia inssuranceCia = new InssuranceCia();

            //String id = "INS" + String.format("%04d", i + 1);
            String name = companyNames[random.nextInt(companyNames.length)];
            String description = descriptions[random.nextInt(descriptions.length)];
            int qtyEmployee = 50 + random.nextInt(950); // Between 50-1000 employees
            boolean isActive = random.nextBoolean();
            //choose a random list of delegations
            List<String> delegations = delegationsList.get(random.nextInt(delegationsList.size()));

            // Set all variables (attributs) of InssuranceCia class
            //inssuranceCia.setId(id);
            inssuranceCia.setName(name);
            inssuranceCia.setDescription(description);
            inssuranceCia.setQtyEmployee(qtyEmployee);
            inssuranceCia.setActive(isActive);
            // set private List<String> delegations = new ArrayList<>(); in InssuranceCia class
            inssuranceCia.setDelegations(delegations);

            // save to DB
            inssuranceCiaRepository.save(inssuranceCia);
            generatedInssuranceCias.add(inssuranceCia);

            // check print for terminal
            System.out.println("Saved " + inssuranceCia.getName() +
                    " delegations: " + inssuranceCia.getDelegations());

        }
        return generatedInssuranceCias;
    }

}
