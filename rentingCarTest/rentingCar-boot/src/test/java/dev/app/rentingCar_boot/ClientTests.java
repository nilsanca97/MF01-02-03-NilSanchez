package dev.app.rentingCar_boot;


import dev.app.rentingCar_boot.model.Client;
import dev.app.rentingCar_boot.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class ClientTests {

    @Autowired
    ClientRepository clientRepository;

    @Test
    void addAddressesToClient(){

        Client client = new Client("John", "Doe",
                "john@email.com", true, 30, "pass123");
        client.getAddresses().add("123 Main St");
        client.getAddresses().add("456 Work Ave");
        client.getAddresses().add("789 Vacation Rd");

        Client clientSaved = clientRepository.save(client);

        System.out.println("Client saved: " + clientSaved);

        Optional<Client> clientFound =  clientRepository.findById(clientSaved.getId());

        System.out.println("Client found: " + clientFound.get() );
    }
}
