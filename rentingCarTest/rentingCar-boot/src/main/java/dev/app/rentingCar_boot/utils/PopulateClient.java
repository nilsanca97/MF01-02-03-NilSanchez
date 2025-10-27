package dev.app.rentingCar_boot.utils;

import dev.app.rentingCar_boot.model.Client;
import dev.app.rentingCar_boot.repository.ClientRepository;
import dev.app.rentingCar_boot.utils.PopulateStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class PopulateClient {

    @Autowired
    private ClientRepository clientRepository;

    public PopulateStatus populateClient(int qty){
        StringBuilder messageBuilder = new StringBuilder();
        boolean[] operationResults = new boolean[1];
        int operationIndex = 0;
        
        try {
            // Operation 1: Generate Clients
            List<Client> clients = generateClients(qty);
            operationResults[operationIndex] = clients != null && clients.size() == qty;
            messageBuilder.append(" Operation 1: Generated ").append(clients != null ? clients.size() : 0)
                         .append(" clients (requested: ").append(qty).append(")\n");
            
        } catch (Exception e) {
            // Mark operation as failed
            operationResults[operationIndex] = false;
            messageBuilder.append("Error occurred during operation 1: ").append(e.getMessage()).append("\n");
        }
        
        // Check if operation succeeded
        boolean allSuccess = operationResults[0];
        
        return new PopulateStatus(allSuccess, messageBuilder.toString().trim(), qty);
    }

    public List<Client> generateClients(int qty){
        List<Client> generatedClients = new ArrayList<>();
        Random random = new Random();

        String[] firstNames = {"John", "Jane", "Michael", "Sarah", "David", "Emma", "James", "Lisa", 
                              "Robert", "Maria", "William", "Jennifer", "Richard", "Patricia", "Charles"};
        
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", 
                             "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson"};
        
        String[] emailDomains = {"@gmail.com", "@yahoo.com", "@hotmail.com", "@outlook.com", "@company.com"};
        
        String[] streetNames = {"Main St", "Oak Ave", "Pine Rd", "Elm St", "Maple Ave", "Cedar Ln", 
                               "Park Blvd", "First St", "Second Ave", "Broadway", "Washington St", "Lincoln Ave"};
        
        String[] cities = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", 
                          "San Antonio", "San Diego", "Dallas", "San Jose", "Austin", "Jacksonville"};
        
        String[] addressTypes = {"Home", "Work", "Billing", "Shipping", "Emergency"};

        for (int i = 0; i < qty; i++) {
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + 
                          emailDomains[random.nextInt(emailDomains.length)];
            boolean premium = random.nextBoolean();
            int age = 18 + random.nextInt(62); // Age between 18-80
            String password = "pass" + (1000 + random.nextInt(9000)); // pass1000-pass9999

            // Create client - note: address parameter is now null since we use @ElementCollection
            Client client = new Client(firstName, lastName, email, premium, age, password);
            
            // Add multiple addresses using @ElementCollection
            int numAddresses = 2 + random.nextInt(3); // 2-4 addresses per client
            for (int j = 0; j < numAddresses; j++) {
                int streetNumber = 100 + random.nextInt(9900); // 100-9999
                String streetName = streetNames[random.nextInt(streetNames.length)];
                String city = cities[random.nextInt(cities.length)];
                String addressType = addressTypes[random.nextInt(addressTypes.length)];
                
                String fullAddress = streetNumber + " " + streetName + ", " + city + " (" + addressType + ")";
                client.getAddresses().add(fullAddress);
            }

            generatedClients.add(client);
            clientRepository.save(client);
        }

        return generatedClients;
    }
}
