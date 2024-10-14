import java.io.*;
import java.util.*;

public class StudentModule {
    static Scanner scanner = new Scanner(System.in);
    static int studentIdCounter = 1000;
    static final String STUDENT_FILE = "students.csv";
    static final String STUDENT_COURSES_FILE = "students_courses.csv";
    // Currently Avaiavle courses
    private static final String[] COURSES = {"OOP", "ICT", "Graphic Design", "Intro to Hadith", "Calculus"};

    public static void displayStudentMenu() {
        int choice = -1;

        do {
            System.out.println("========== Student Module ==========");
            System.out.println("1. Add a New Student");
            System.out.println("2. Enroll Student in a Course");
            System.out.println("3. View All Students with Their Enrolled Courses");
            System.out.println("4. Update Student Information");
            System.out.println("5. Remove a Student");
            System.out.println("6. Search for a Student");
            System.out.println("7. Exit");
            System.out.println("====================================");
            System.out.print("Enter your choice: ");

            try {
                // Get the user's choice
                choice = Integer.parseInt(scanner.nextLine());  
                
                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        enrollStudentInCourse();
                        break;
                    case 3:
                        viewAllStudentsWithCourses();
                        break;
                    case 4:
                        updateStudentInformation();
                        break;
                    case 5:
                        removeStudent();
                        break;
                    case 6:
                        searchStudent();
                        break;
                    case 7:
                        System.out.println("Exiting Student Module...");
                        UniversityManagementSystem.mainMenu();
                        break;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }

            System.out.println();
        } while (choice != 7);
    }


public static void addStudent() {
    studentIdCounter++;  // Increment the SAP ID counter for the next student

    System.out.print("Enter Name: ");
    String name = scanner.nextLine();
    System.out.print("Enter Email: ");
    String email = scanner.nextLine();
    System.out.print("Enter Password: ");
    String password = scanner.nextLine();

    // Write the new student details to the students.csv file
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("students.csv", true))) {
        writer.write(studentIdCounter + "," + name + "," + email + "," + password);
        writer.newLine();
        System.out.println("Student added successfully with SAP ID: " + studentIdCounter);
    } catch (IOException e) {
        System.out.println("An error occurred while adding the student: " + e.getMessage());
    }
}

    public static void enrollStudentInCourse() {
        System.out.print("Enter the SAP ID of the student: ");
        String sapId = scanner.nextLine();

        if (!isStudentExist(sapId)) {
            System.out.println("No student found with SAP ID: " + sapId);
            return;
        }
        System.out.println("Available Courses:");
        for (int i = 0; i < COURSES.length; i++) {
            System.out.printf("%d. %s\n", i + 1, COURSES[i]);
        }

        System.out.print("Enter course numbers to enroll (comma-separated, e.g., 1,3,5): ");
        String courseSelection = scanner.nextLine();
        String[] courseIndexes = courseSelection.split(",");

        List<String> selectedCourses = new ArrayList<>();
        for (String index : courseIndexes) {
            try {
                int courseIndex = Integer.parseInt(index.trim()) - 1;
                if (courseIndex >= 0 && courseIndex < COURSES.length) {
                    selectedCourses.add(COURSES[courseIndex]);
                } else {
                    System.out.println("Invalid course number: " + (courseIndex + 1));
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + index);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STUDENT_COURSES_FILE, true))) {
            for (String course : selectedCourses) {
                writer.write(sapId + "," + course);
                writer.newLine();
            }
            System.out.println("Student successfully enrolled in selected courses.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing enrollment data: " + e.getMessage());
        }
    }

    public static void viewAllStudentsWithCourses() {
        try (BufferedReader studentReader = new BufferedReader(new FileReader(STUDENT_FILE))) {
           
            System.out.printf("%-10s %-20s %-30s %-20s\n", "SAP ID", "Name", "Email", "Courses");
            System.out.println("---------------------------------------------------------------");

            String studentLine;
            // Read each student
            while ((studentLine = studentReader.readLine()) != null) {
                String[] studentParts = studentLine.split(",");
                if (studentParts.length >= 4) {  
                    String sapId = studentParts[0];
                    String name = studentParts[1];
                    String email = studentParts[2];

                    //  Read all enrollments for this student from student_courses.csv
                    StringBuilder coursesList = new StringBuilder();
                    try (BufferedReader courseReader = new BufferedReader(new FileReader(STUDENT_COURSES_FILE))) {
                        String courseLine;
                        while ((courseLine = courseReader.readLine()) != null) {
                            String[] courseParts = courseLine.split(",");
                            if (courseParts.length == 2 && courseParts[0].equals(sapId)) { // Match SAP ID
                                if (coursesList.length() > 0) {
                                    coursesList.append(", "); // Add comma for multiple courses
                                }
                                coursesList.append(courseParts[1]); // Append course name
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("An error occurred while reading enrollment data: " + e.getMessage());
                    }

                    // Print student details along with their courses
                    System.out.printf("%-10s %-20s %-30s %-20s\n", sapId, name, email, coursesList.toString());
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading student data: " + e.getMessage());
        }
    }

    public static void searchStudent() {
        System.out.print("Enter SAP ID of the student to search: ");
        String sapIdToSearch = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            boolean studentFound = false; // To track if the student was found

            // Read each line from the students file
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // Check if the current line matches the SAP ID we want to search for
                if (parts.length == 4 && parts[0].equals(sapIdToSearch)) {
                    studentFound = true; // Student found
                    System.out.println("Student found: SAP ID: " + parts[0] + ", Name: " + parts[1] + ", Email: " + parts[2] + ", Password: " + parts[3]);
                    break; // Exit the loop once the student is found
                }
            }

            // If the student was not found, print a message
            if (!studentFound) {
                System.out.println("No student found with SAP ID: " + sapIdToSearch);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while searching for the student: " + e.getMessage());
        }
    }

    public static void updateStudentInformation() {
        System.out.print("Enter SAP ID of the student to update: ");
        String sapIdToUpdate = scanner.nextLine();
    
        // Temporary file to write updated data
        File tempFile = new File("temp_students.csv");
    
        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENT_FILE));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
    
            String line;
            boolean studentFound = false; // To track if the student was found
    
            // Read each line from the original file
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
    
                // Check if the current line matches the SAP ID we want to update
                if (parts.length == 4 && parts[0].equals(sapIdToUpdate)) {
                    studentFound = true; // Student found
                    System.out.println("Current Information: Name: " + parts[1] + ", Email: " + parts[2] + ", Password: " + parts[3]);
    
                    System.out.print("Enter new Name (leave blank to keep current): ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter new Email (leave blank to keep current): ");
                    String newEmail = scanner.nextLine();
                    System.out.print("Enter new Password (leave blank to keep current): ");
                    String newPassword = scanner.nextLine();
    
                    // Update the fields if the new values are not blank
                    if (!newName.isEmpty()) {
                        parts[1] = newName;
                    }
                    if (!newEmail.isEmpty()) {
                        parts[2] = newEmail;
                    }
                    if (!newPassword.isEmpty()) {
                        parts[3] = newPassword;
                    }
    
                    // Write the updated information to the temporary file
                    writer.write(String.join(",", parts));
                    writer.newLine();
                } else {
                    // If not updating, just write the original line to the temporary file
                    writer.write(line);
                    writer.newLine();
                }
            }
    
            // Check if the student was found and notify the user
            if (studentFound) {
                System.out.println("Student information updated successfully.");
            } else {
                System.out.println("No student found with SAP ID: " + sapIdToUpdate);
            }
    
        } catch (IOException e) {
            System.out.println("An error occurred while updating student information: " + e.getMessage());
            return; // Exit the function on error
        }
    
        // Rename the temp file to the original file
        File originalFile = new File(STUDENT_FILE);
        if (originalFile.delete()) { // Delete the original file first
            if (tempFile.renameTo(originalFile)) {
                System.out.println("File updated successfully.");
            } else {
                System.out.println("Could not update the file.");
            }
        } else {
            System.out.println("Could not delete the original file.");
        }
    }
    
    

    public static void removeStudent() {
        System.out.print("Enter SAP ID of the student to remove: ");
        String sapIdToRemove = scanner.nextLine();
    
        // Temporary file to write remaining data
        File tempFile = new File("temp_students.csv");
    
        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENT_FILE));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
    
            String line;
            boolean studentFound = false; 
    
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
    
                // Check if the current line matches the SAP ID we want to remove
                if (parts.length == 4 && parts[0].equals(sapIdToRemove)) {
                    studentFound = true; // Student found
                    System.out.println("Removing student: " + line); 
                    writer.write(line);
                    writer.newLine();
                }
            }
    
            // check if student was found or not
            if (studentFound) {
                System.out.println("Student removed successfully.");
            } else {
                System.out.println("No student found with SAP ID: " + sapIdToRemove);
            }
    
        } catch (IOException e) {
            System.out.println("An error occurred while removing the student: " + e.getMessage());
            return; // Exit the function on error
        }
    
        File originalFile = new File(STUDENT_FILE);
    
        // Attempt to replace the original file with the updated temporary file
        if (originalFile.delete()) { // Delete the original file first
            if (tempFile.renameTo(originalFile)) {
                System.out.println("File updated successfully.");
            } else {
                System.out.println("Could not update the file.");
            }
        } else {
            System.out.println("Could not delete the original file.");
        }
    }
    
    

    // Method to check if a student exists in students.csv
    private static boolean isStudentExist(String sapId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(sapId)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading student data: " + e.getMessage());
        }
        return false;
    }
}
