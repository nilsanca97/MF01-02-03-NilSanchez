package dev.app.rentingCar_boot.utils;

import java.util.Random;

public class GenerateUUID {

    /**
     * Generates a 4-digit UUID for the car
     * @return A 4-digit string ID
     */
    public static  String generateFourDigitUuid() {
        Random random = new Random();
        int uuid = 1000 + random.nextInt(9000); // Generates number between 1000-9999
        return String.valueOf(uuid);
    }
}
