import java.io.*;
import java.util.Scanner;

public class StudentRegistrationProgram {
    private static final String FILE_NAME = "students.txt";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            printMenuOptions();
            int choice = getChoice();

            switch (choice) {
                case 1:
                    registerNewStudent();
                    break;
                case 2:
                    listStudentsByStartingLetter();
                    break;
                case 3:
                    updateStudentGrade();
                    break;
                case 4:
                    exitProgram();
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
    }

    private static void printMenuOptions() {
        System.out.println();
        System.out.println("Choose an action:");
        System.out.println("1. Register a new student");
        System.out.println("2. List students by starting letter of name");
        System.out.println("3. Update student grade");
        System.out.println("4. Exit");
    }

    private static int getChoice() {
        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return choice;
    }

    private static void registerNewStudent() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            System.out.println("Enter student information (name surname number year_of_birth GPA):");
            String studentInfo = scanner.nextLine().trim(); // Trim any extra spaces

            // Split the input by spaces and ensure it has the required information
            String[] studentData = studentInfo.split("\\s+");
            if (studentData.length != 5) {
                System.out.println("Invalid input format. Please provide name, surname, number, year of birth, and GPA separated by spaces.");
                return;
            }

            // Join the data with commas and write the formatted student information to the file
            String formattedInfo = String.join(", ", studentData);
            writer.println(formattedInfo);
            System.out.println("Student registered successfully!");
        } catch (IOException e) {
            System.out.println("Error occurred while registering the student.");
        }
    }


    private static void listStudentsByStartingLetter() {
        System.out.println("Enter the starting letter of the names to list:");
        char startingLetter = scanner.nextLine().charAt(0);

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] studentInfo = line.split(", ");
                if (studentInfo[0].charAt(0) == startingLetter) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error occurred while listing students.");
        }
    }

    private static void updateStudentGrade() {
        System.out.println("Enter the student number:");
        String studentNumber = scanner.nextLine();

        try {
            File inputFile = new File(FILE_NAME);
            File tempFile = new File("temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

            String line;
            boolean updated = false;

            while ((line = reader.readLine()) != null) {
                String[] studentInfo = line.split(", ");
                if (studentInfo.length >= 3 && studentInfo[2].equals(studentNumber)) {
                    if (!updated) {
                        System.out.println("Enter the new grade for the student:");
                        String newGradeInput = scanner.nextLine();
                        int newGrade;
                        try {
                            newGrade = Integer.parseInt(newGradeInput);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid grade format. Please enter a valid integer for the grade.");
                            continue;
                        }
                        studentInfo[4] = Integer.toString(newGrade);
                        updated = true;
                    }
                }
                writer.println(String.join(", ", studentInfo));
            }

            writer.close();
            reader.close();

            if (!updated) {
                System.out.println("Student number not found.");
                tempFile.delete(); // Delete the temporary file if no update was performed
            } else {
                if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                    System.out.println("Error occurred while updating student grade.");
                } else {
                    System.out.println("Student grade updated successfully!");
                }
            }

        } catch (IOException e) {
            System.out.println("Error occurred while updating student grade.");
        }
    }

    private static void exitProgram() {
        System.out.println("Exiting the program. Goodbye!");
        System.exit(0);
    }
}
