package dev.app.rentingCar_boot;

//import dev.app.rentingCar_boot.utils.PopulateInssuranceCia;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RentingCarBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentingCarBootApplication.class, args);

        //ConfigurableApplicationContext context = SpringApplication.run(RentingCarBootApplication.class, args);

        //PopulateInssuranceCia populateInssuranceCia = context.getBean(PopulateInssuranceCia.class);
        //populateInssuranceCia.populateInssuranceCiaData();
    }

}
