package dev.app.rentingCar_boot.utils;

import dev.app.rentingCar_boot.model.InssuranceCia;
import dev.app.rentingCar_boot.repository.InssuranceCiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PopulateInssuranceCia {

 @Autowired
 private InssuranceCiaRepository inssuranceCiaRepository;

 public void populateInssuranceCiaData() {
    // create insuranceCia 1: Mapfre
    InssuranceCia mapfre = new InssuranceCia();
    mapfre.setName("Mapfre Seguros");
    mapfre.getDelegations().add("Barcelona Office \n Carrer Balmes, 123");
    mapfre.getDelegations().add("Madrid Office \n Calle Embajadores, 45");
    inssuranceCiaRepository.save(mapfre);

    // create inssuranceCia2: Allianz
     InssuranceCia allianz = new InssuranceCia();
     allianz.setName("Allianz Seguros");
     allianz.getDelegations().add("Bilbao Office \n Calle Gran Via, 87");
     allianz.getDelegations().add("Sevilla Office \n Plaza Mayor, 1");
     inssuranceCiaRepository.save(allianz);

 }
}
