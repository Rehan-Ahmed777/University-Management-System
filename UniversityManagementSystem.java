import java.util.Scanner;

public class UniversityManagementSystem {

    static Scanner scanner = new Scanner(System.in);
    
    public static void mainMenu(){
        System.out.println("Welcome to the University Management System");
        System.out.println("Logged in as Admin");
        while (true) {
            System.out.println();
            System.out.println("Please select a modeule to manage");
            System.out.println("\t1. Student");
            System.out.println("\t2. Faculty");
            System.out.println("\t3. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  

            switch (choice) {
                case 1:
                    StudentModule.displayStudentMenu();
                    break;
                case 2:
                    FacultyModule.facultyLogin();
                case 3:
                    System.out.println("Exiting the system.");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

   
    public static void main(String[] args) {
        UniversityManagementSystem.mainMenu();
    }
}
