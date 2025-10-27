package dev.app.rentingCar_boot.utils;

import dev.app.rentingCar_boot.model.Client;
import dev.app.rentingCar_boot.model.DrivingCourse;
import dev.app.rentingCar_boot.repository.ClientRepository;
import dev.app.rentingCar_boot.repository.DrivingCourseRepository;
import dev.app.rentingCar_boot.utils.PopulateStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
public class PopulateDrivingCourse {

    @Autowired
    private DrivingCourseRepository drivingCourseRepository;

    @Autowired
    private ClientRepository clientRepository;

    public PopulateStatus populateDrivingCourse(int qty) {
        StringBuilder messageBuilder = new StringBuilder();
        boolean[] operationResults = new boolean[2];
        int operationIndex = 0;
        
        try {
            // Operation 1: Generate Driving Courses
            ArrayList<DrivingCourse> drivingCourses = generateDrivingCourses(qty);
            operationResults[operationIndex] = drivingCourses != null && drivingCourses.size() == qty;
            messageBuilder.append(" Operation 1: Generated ").append(drivingCourses != null ? drivingCourses.size() : 0)
                         .append(" driving courses (requested: ").append(qty).append(")\n");
            operationIndex++;
            
            // Operation 2: Assign Clients to Driving Courses
            assignClientsToDrivingCourses(drivingCourses);
            operationResults[operationIndex] = true; // Assume success if no exception
            messageBuilder.append(" Operation 2: Assigned clients to driving courses successfully\n");
            
        } catch (Exception e) {
            // Mark current and remaining operations as failed
            for (int i = operationIndex; i < 2; i++) {
                operationResults[i] = false;
            }
            messageBuilder.append("Error occurred during operation ").append(operationIndex + 1)
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
        
        return new PopulateStatus(allSuccess, messageBuilder.toString().trim(), qty);
    }

    public ArrayList<DrivingCourse> generateDrivingCourses(int qty) {
        ArrayList<DrivingCourse> generatedDrivingCourses = new ArrayList<>();
        Random random = new Random();

        String[] courseNames = {"Basic Driving", "Advanced Driving", "Defensive Driving", "Highway Driving", 
                               "City Driving", "Night Driving", "Winter Driving", "Commercial Driving", 
                               "Motorcycle Basics", "Parallel Parking Mastery"};
        
        String[] descriptions = {
            "Learn the fundamentals of safe driving with experienced instructors",
            "Advanced techniques for confident and skilled driving",
            "Defensive driving strategies to avoid accidents and hazards",
            "Master highway driving with proper merging and lane changing",
            "Navigate city traffic with confidence and precision",
            "Safe night driving techniques and visibility management",
            "Winter driving skills for challenging weather conditions",
            "Professional commercial vehicle operation training",
            "Complete motorcycle riding course for beginners",
            "Perfect your parallel parking and tight space maneuvering"
        };

        String[] instructors = {"John Smith", "Sarah Johnson", "Mike Davis", "Lisa Brown", "David Wilson", 
                               "Emma Garcia", "James Miller", "Maria Rodriguez", "Robert Taylor", "Jennifer Lee"};

        String[] categories = {"Basic", "Advanced", "Defensive", "Commercial", "Specialized"};

        String[] locations = {"Downtown Center", "North Campus", "South Branch", "East Side", "West End", 
                             "City Center", "Suburban Branch", "Highway Training Ground", "Practice Lot A", "Practice Lot B"};

        for (int i = 0; i < qty; i++) {
            DrivingCourse drivingCourse = new DrivingCourse();
            
            String courseName = courseNames[random.nextInt(courseNames.length)];
            String description = descriptions[random.nextInt(descriptions.length)];
            String instructor = instructors[random.nextInt(instructors.length)];
            int durationHours = 10 + random.nextInt(41); // Between 10-50 hours
            double price = 100.0 + (random.nextDouble() * 400.0); // Between 100-500
            String category = categories[random.nextInt(categories.length)];
            int maxStudents = 5 + random.nextInt(16); // Between 5-20 students
            boolean isActive = random.nextBoolean();
            String location = locations[random.nextInt(locations.length)];

            drivingCourse.setCourseName(courseName);
            drivingCourse.setDescription(description);
            drivingCourse.setInstructor(instructor);
            drivingCourse.setDurationHours(durationHours);
            drivingCourse.setPrice(price);
            drivingCourse.setCategory(category);
            drivingCourse.setMaxStudents(maxStudents);
            drivingCourse.setActive(isActive);
            drivingCourse.setLocation(location);

            generatedDrivingCourses.add(drivingCourse);
            drivingCourseRepository.save(drivingCourse);
        }

        return generatedDrivingCourses;
    }

    public void assignClientsToDrivingCourses(ArrayList<DrivingCourse> drivingCourses){
        //List<Client> allClients = (List<Client>) clientRepository.findAll();
        // Fetch all clients from database
        List<Client> allClients = new ArrayList<>();
        clientRepository.findAll().forEach(allClients::add);
        // defensive programming: check if there are clients in the database
        if (allClients.isEmpty()) {
            System.out.println("No clients found in database. Cannot assign clients to driving courses.");
            return;
        }
        
        Random random = new Random();
        // loop: for each driving course
        for (DrivingCourse drivingCourse : drivingCourses) {
            // Determine number of clients to assign (between 5 and 10)
            int numClientsToAssign = 5 + random.nextInt(6); // 5 to 10 clients

            List<Client> clientsToAdd = new ArrayList<>();
            
            // Add random clients without duplicates
            // clientsToAdd.size() < numClientsToAssign: until we have assigned the required number of clients
            // clientsToAdd.size() < allClients.size(): until we run out of clients
            while (clientsToAdd.size() < numClientsToAssign && clientsToAdd.size() < allClients.size()) {
                // Get a random client from allClients
                Client randomClient = allClients.get(random.nextInt(allClients.size()));
                
                // Check if this client is already in clientsToAdd
                if (!clientsToAdd.contains(randomClient)) {
                    clientsToAdd.add(randomClient);
                }
            }
            
            // Add all selected clients to the driving course
            drivingCourse.getClients().addAll(clientsToAdd);
            
            // Save the updated driving course with assigned clients
            drivingCourseRepository.save(drivingCourse);
            
            System.out.println("Assigned " + clientsToAdd.size() + " clients to course: '" + drivingCourse.getCourseName() +
                             "' (ID: " + drivingCourse.getId() + ", Instructor: " + drivingCourse.getInstructor() + 
                             ", Max Students: " + drivingCourse.getMaxStudents() + ")");
        }
    }
}
