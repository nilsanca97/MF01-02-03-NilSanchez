package dev.app.rentingCar_boot;

import dev.app.rentingCar_boot.model.Client;
import dev.app.rentingCar_boot.model.DrivingCourse;
import dev.app.rentingCar_boot.repository.ClientRepository;
import dev.app.rentingCar_boot.repository.DrivingCourseRepository;
import dev.app.rentingCar_boot.utils.PopulateDrivingCourse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DrivingCourseTests {

    @Autowired
    private DrivingCourseRepository drivingCourseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    PopulateDrivingCourse populateDrivingCourse;

    @Test
    void assignClientsToDrivingCourses() {

        DrivingCourse drivingCourse = new DrivingCourse();
        drivingCourse.setCourseName("Driving Course 1");
        drivingCourse.setPrice(100.0);
        drivingCourse.setActive(true);
        drivingCourse.setCategory("Category 1");
        drivingCourse.setLocation("Location 1");
        drivingCourse.setDurationHours(100);
        drivingCourse.setInstructor("Instructor 1");
        drivingCourse.setMaxStudents(10);
        // we do not set clients yet, first we must fetch a client from db
        //drivingCourse.setClients(new ArrayList<>());

        // fetch all clients from db
        Iterable<Client> clients =  clientRepository.findAll();
        // get the first client
        Client myClient = clients.iterator().next();

        // assign, that is, add the client to the driving course clients list attribute
        drivingCourse.getClients().add(myClient);
        // save to H2 db
        drivingCourseRepository.save(drivingCourse);

        // print all driving courses
        Iterable<DrivingCourse> myDrivingCourses = drivingCourseRepository.findAll();
        for (DrivingCourse drivingCourseToprint : myDrivingCourses) {
            System.out.println(drivingCourseToprint + "\n");
        }

    }

    @Test
    void populateBookingTest(){
        populateDrivingCourse.populateDrivingCourse(10);
    }
}
