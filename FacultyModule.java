import java.io.*;
import java.util.*;

public class FacultyModule {
    static Scanner scanner = new Scanner(System.in);
    static final String FACULTY_FILE = "faculty.csv";
    static final String FACULTY_COURSES_FILE = "faculty_courses.csv";
    static int facultyIdCounter = 2000;

      // Faculty operations
      public static void facultyLogin() {
        while (true) {
            System.out.println("Logged in as: Admin");
            System.out.println("-------------");
            System.out.println("1. Add Faculty Member");
            System.out.println("2. View all Faculty Members");
            System.out.println("3. Assign Course to Faculty");
            System.out.println("4. View Courses Assigned to Faculty");
            System.out.println("5. Update Faculty Member Information");
            System.out.println("6. Remove Faculty Member");
            System.out.println("7. Search Faculty Member");
            System.out.println("8. Logout");
            System.out.print("Enter your option: ");

            try {
                int option = Integer.parseInt(scanner.nextLine());  

                switch (option) {
                    case 1:
                        addFacultyMember();
                        break;
                    case 2:
                        viewAllFacultyMembers();
                        break;
                    case 3:
                        assignCourseToFaculty();
                        break;
                    case 4:
                        viewCoursesAssignedToFacultyMember();
                        break;
                    case 5:
                        updateFacultyMemberInformation();
                        break;
                    case 6:
                        removeFacultyMember();
                        break;
                    case 7:
                        searchFacultyMemberById();
                        break;
                    case 8:
                        System.out.println("Exiting the Faculty Module");
                        UniversityManagementSystem.mainMenu();
                        return; 
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }

            System.out.println();
        }
    }


    public static void searchFacultyMemberById() {
        System.out.print("Enter faculty member's ID to search: ");
        String facultyId = scanner.nextLine();
        boolean facultyFound = false; // to check if the faculty member exist

        try (BufferedReader reader = new BufferedReader(new FileReader(FACULTY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // Check if the current line's faculty ID matches the one to search
                if (parts.length >= 3 && parts[0].equals(facultyId)) {
                    facultyFound = true; //faculty member found
                    System.out.printf("Faculty Member Found:\nID: %s, Name: %s, Email: %s\n", parts[0], parts[1], parts[2]);
                    break; // Exit loop after finding the member
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading faculty data: " + e.getMessage());
            return; // Exit the method if an error occurs
        }

        if (!facultyFound) {
            System.out.println("No faculty member found with ID " + facultyId + ".");
        }
    }

    // Method to remove a faculty member
public static void removeFacultyMember() {
    System.out.print("Enter faculty member's ID to remove: ");
    String facultyId = scanner.nextLine();

    List<String> facultyMembers = new ArrayList<>(); // To hold all faculty member records
    boolean facultyFound = false; // To check if the faculty member exists

    // Read from the faculty.csv file to find and remove the given faculty ID
    try (BufferedReader reader = new BufferedReader(new FileReader(FACULTY_FILE))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            // Check if the current line's faculty ID matches the one to remove
            if (parts.length >= 3 && parts[0].equals(facultyId)) {
                facultyFound = true; // Mark that the faculty member was found
                System.out.println("Removing faculty member:");
                System.out.printf("ID: %s, Name: %s, Email: %s\n", parts[0], parts[1], parts[2]);
            } else {
                // If the faculty member is not the one being removed, add the original line
                facultyMembers.add(line);
            }
        }
    } catch (IOException e) {
        System.out.println("An error occurred while reading faculty data: " + e.getMessage());
        return; // Exit the method if an error occurs
    }

    // Checking if the faculty member was removed and updated in the faculty.csv file
    if (facultyFound) {
        // Write updated faculty information back to the faculty.csv file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FACULTY_FILE))) {
            for (String faculty : facultyMembers) {
                writer.write(faculty);
                writer.newLine(); // Ensure each entry is on a new line
            }
            System.out.println("Faculty member removed successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while updating faculty data: " + e.getMessage());
        }
    } else {
        System.out.println("No faculty member found with ID " + facultyId + ".");
    }
}

public static void updateFacultyMemberInformation() {
    System.out.print("Enter faculty member's ID to update: ");
    String facultyId = scanner.nextLine();

    List<String> facultyMembers = new ArrayList<>(); 
    boolean facultyFound = false; 

    // Read from the faculty.csv file to find and update the given faculty ID
    try (BufferedReader reader = new BufferedReader(new FileReader(FACULTY_FILE))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 3 && parts[0].equals(facultyId)) {
                facultyFound = true; // Mark that the faculty member was found
                System.out.println("Current Information:");
                System.out.printf("ID: %s, Name: %s, Email: %s\n", parts[0], parts[1], parts[2]);

                // Update faculty member information
                System.out.print("Enter new name (leave blank to keep unchanged): ");
                String newName = scanner.nextLine();
                System.out.print("Enter new email (leave blank to keep unchanged): ");
                String newEmail = scanner.nextLine();

                // Update the fields if new values are provided
                String updatedName = newName.isEmpty() ? parts[1] : newName;
                String updatedEmail = newEmail.isEmpty() ? parts[2] : newEmail;

                // Add updated faculty member to the list
                facultyMembers.add(parts[0] + "," + updatedName + "," + updatedEmail);
            } else {
                facultyMembers.add(line);
            }
        }
    } catch (IOException e) {
        System.out.println("An error occurred while reading faculty data: " + e.getMessage());
        return; // Exit the method if an error occurs
    }

    // Check if the faculty member was found and updated
    if (facultyFound) {
        // Write updated faculty information back to the faculty.csv file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FACULTY_FILE))) {
            for (String faculty : facultyMembers) {
                writer.write(faculty);
                writer.newLine(); // Ensure each entry is on a new line
            }
            System.out.println("Faculty member information updated successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while updating faculty data: " + e.getMessage());
        }
    } else {
        System.out.println("No faculty member found with ID " + facultyId + ".");
    }
}

 // Method to view all courses assigned to faculty members
public static void viewCoursesAssignedToFacultyMember() {
    // Read from the faculty_courses.csv file and display all records
    try (BufferedReader reader = new BufferedReader(new FileReader(FACULTY_COURSES_FILE))) {
        String line;
        System.out.printf("%-10s %-30s\n", "Faculty ID", "Course Name");
        System.out.println("-------------------------------------------------");

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 2) {  
                String facultyId = parts[0];
                String courseName = parts[1];
                System.out.printf("%-10s %-30s\n", facultyId, courseName);
            }
        }
    } catch (IOException e) {
        System.out.println("An error occurred while reading course assignments: " + e.getMessage());
    }
}

   // Method to add a faculty member
   public static void addFacultyMember() {
    System.out.print("Enter name: ");
    String name = scanner.nextLine();
    System.out.print("Enter email: ");
    String email = scanner.nextLine();

    String facultyId = String.valueOf(++facultyIdCounter);  // unique id every time

    // Write the new faculty member's details to the CSV file
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(FACULTY_FILE, true))) {
        writer.write(facultyId + "," + name + "," + email);
        writer.newLine();  // Ensure new line for the next entry
        System.out.println("Faculty member added successfully! Faculty ID is: " + facultyId);
    } catch (IOException e) {
        System.out.println("An error occurred while adding the faculty member: " + e.getMessage());
    }
}


  // Method to assign a course to a faculty member
public static void assignCourseToFaculty() {
    System.out.print("Enter faculty member's ID: ");
    String facultyId = scanner.nextLine();
    System.out.print("Enter the course name to assign: ");
    String courseName = scanner.nextLine();

    boolean facultyExists = false;  // To track if the faculty member exists

    // Check if the faculty member exists in the faculty.csv file
    try (BufferedReader reader = new BufferedReader(new FileReader(FACULTY_FILE))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 3 && parts[0].equals(facultyId)) {
                facultyExists = true;  // Faculty member found
                break;  // Exit the loop as we found the faculty
            }
        }
    } catch (IOException e) {
        System.out.println("An error occurred while checking faculty data: " + e.getMessage());
    }

    // If the faculty member exists, assign the course
    if (facultyExists) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FACULTY_COURSES_FILE, true))) {
            writer.write(facultyId + "," + courseName);
            writer.newLine();  // Ensure new line for the next entry
            System.out.println("Assigned course to faculty member successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while assigning the course: " + e.getMessage());
        }
    } else {
        System.out.println("Faculty member with ID " + facultyId + " does not exist. Please check the ID and try again.");
    }
}

// Method to view all faculty members
public static void viewAllFacultyMembers() {
    try (BufferedReader reader = new BufferedReader(new FileReader(FACULTY_FILE))) {
        String line;

        // Skip the header row
        reader.readLine();

        System.out.printf("%-10s %-20s %-30s\n", "Faculty ID", "Name", "Email");
        System.out.println("------------------------------------------------------------");

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 3) {  // Ensure there are at least 3 fields
                String facultyId = parts[0];
                String name = parts[1];
                String email = parts[2];
                System.out.printf("%-10s %-20s %-30s\n", facultyId, name, email);
            }
        }
    } catch (IOException e) {
        System.out.println("An error occurred while reading faculty data: " + e.getMessage());
    }
}
    
}
