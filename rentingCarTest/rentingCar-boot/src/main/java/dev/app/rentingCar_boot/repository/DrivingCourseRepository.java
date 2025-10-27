package dev.app.rentingCar_boot.repository;


import dev.app.rentingCar_boot.model.DrivingCourse;
import org.springframework.data.repository.CrudRepository;

public interface DrivingCourseRepository extends CrudRepository<DrivingCourse, String> {
}
