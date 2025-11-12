package dev.app.rentingCar_boot.repository;

import dev.app.rentingCar_boot.model.InssuranceCia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InssuranceCiaRepository extends JpaRepository<InssuranceCia, String> {
    //@Override
    Optional<InssuranceCia> findById(String id);
}
