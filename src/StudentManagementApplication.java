import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

//StudentManagementApplication class
public class StudentManagementApplication {
    private static final int MODULE_COUNT = 3;
    private static final int MAXIMUM_STUDENTS_CAPACITY = 100;                                             //initialize MAXIMUM_STUDENTS_CAPACITY to store total student capacity
    private static int currentStudentsCount = 0;                                                          //initialize currentStudentsCount to track student count
    private static final String[][] STUDENT_INFORMATION_ARRAY = new String[MAXIMUM_STUDENTS_CAPACITY][2]; //initialize STUDENT_INFORMATION_ARRAY to store student information
    private static final Student[] STUDENTS_ARRAY = new Student[MAXIMUM_STUDENTS_CAPACITY];               //initialize STUDENTS_ARRAY to store student objects
    private static final Scanner GET_INPUT = new Scanner(System.in);                                      //initialize GET_INPUT to get user inputs
    private static boolean using = true;                                                                  //initialize using to keep application running
    private static final String FILE_NAME = "StudentDetails.txt";                                         //initialize FILE_NAME to store file used in application

    //main method of the java program
    public static void main(String[] args) {
        initializeStudentInfoArray();       //Call initializeStudentInfoArray() method
        initializeStudentsArray();          //Call initializeStudentsArray() method
        loadFromFile();                     //Call loadFromFile() method
        mainMenu();                         //Call mainMenu() method
    }

    //Initialize Student information array with empty strings for ID and name
    private static void initializeStudentInfoArray() {
        for (int i = 0; i < MAXIMUM_STUDENTS_CAPACITY; i++) {           //loop from 0 to student capacity
            STUDENT_INFORMATION_ARRAY[i] = new String[] {"", ""};       //loop through STUDENT_INFORMATION_ARRAY and fill with empty String arrays
        }
    }

    //Initialize(fill) Student array with empty student slots
    private static void initializeStudentsArray() {
        Student emptyStudent = emptyStudentSlot();              //get empty student slots
        Arrays.fill(STUDENTS_ARRAY, emptyStudent);              // fill STUDENTS_ARRAY items with emptyStudent slots

    }

    //Generate empty student objects to initialize Students array
    private static Student emptyStudentSlot() {
        return new Student("empty"          //return initial Student objects
                , "empty"
                ,new Module[MODULE_COUNT]
                , "MarksNotAdded");
    }

    //Main Menu for handle the system
    private static void mainMenu() {
        while (using) {                                 //loop while using is true
            System.out.println("\nMain Menu");          //display Menu messages and prompt user choice
            System.out.println("(1) Check available seats");
            System.out.println("(2) Register student");
            System.out.println("(3) Delete student");
            System.out.println("(4) Find student");
            System.out.println("(5) Store data into file");
            System.out.println("(6) Load from file to system");
            System.out.println("(7) View student list");
            System.out.println("(8) Additional Menu");
            System.out.println("(0) Exit");
            System.out.print("Enter your choice: ");

            while (!GET_INPUT.hasNextInt()) {           //validate userChoice
                System.out.print("Invalid option! Enter again: ");
                GET_INPUT.next();
            }

            int userChoice = GET_INPUT.nextInt();       //get the userChoice
            switch (userChoice) {                       //switch case to handle Main menu options
                case 1:
                    System.out.println(checkAvailableSeats(currentStudentsCount));
                    break;
                case 2:
                    registerStudent();
                    break;
                case 3:
                    deleteStudent();
                    break;
                case 4:
                    searchStudent();
                    break;
                case 5:
                    storeToFile();
                    System.out.println("Student details stored successfully!");
                    break;
                case 6:
                    loadFromFile();
                    System.out.println("Student details loaded successfully!");
                    break;
                case 7:
                    System.out.println("Students list sorted according to names");
                    studentList();
                    break;
                case 8:
                    subMenu();
                    break;
                case 0:
                    System.out.println("Exit from the application");
                    GET_INPUT.close();      //close Scanner resource if user chose exit
                    using = false;          //set using as false if user chose to exit
                    break;
                default:                    //handle invalid userChoice
                    System.out.println("Wrong choice, Check again!");
                    break;
            }
        }
    }

    //Check student seats availability
    private static String checkAvailableSeats(int current) {
        System.out.println();
        int availableSeats = MAXIMUM_STUDENTS_CAPACITY - current;       //calculate available seat count
        return "Registered student count: " + currentStudentsCount + "\nAvailable Seats count: " + availableSeats;      //display messages for seat availability
    }

    //Register students with student ID
    private static void registerStudent() {
        if (currentStudentsCount == MAXIMUM_STUDENTS_CAPACITY) {        //return from the method if max student capacity reached
            System.out.println("Maximum capacity of students reached!");
            return;
        }

        while (true) {      //start an infinite loop
            System.out.println();
            System.out.print("Enter student ID[w*******] or Enter 'q' to exit: ");      //prompt student ID
            String studentID = GET_INPUT.next().trim();
            GET_INPUT.nextLine();

            if (studentID.equalsIgnoreCase("q")) {      //return from the method if user entered 'q'
                System.out.println("Back to main menu");
                return;
            }

            if (validStudentID(studentID)) {        //check the validity of student ID
                for (String[] studentDetails : STUDENT_INFORMATION_ARRAY) {         //loop through STUDENT_INFORMATION_ARRAY
                    String registeredID = studentDetails[0];                        //retrieve previously registered IDs from studentDetails sub array
                    if (registeredID.equalsIgnoreCase(studentID)) {                 //return from method if ID already exists
                        System.out.println("Student already registered!");
                        return;
                    }
                }
                while (true) {      //start a infinite loop
                    System.out.print("Enter student name: ");                   //prompt student ID
                    String studentName = GET_INPUT.nextLine().trim();
                    if (validStudentName(studentName)) {                        //check the validity of student name
                        String formattedID = formatStudentID(studentID);        //get the formatted student Name

                        STUDENT_INFORMATION_ARRAY[currentStudentsCount] = new String[] {formattedID, formatName(studentName)};
                        //update STUDENT_INFORMATION_ARRAY with formatted ID, and formatted Name

                        STUDENTS_ARRAY[currentStudentsCount] = new Student(formattedID
                                                              , formatName(studentName)
                                                              , new Module[MODULE_COUNT]
                                                              , "MarksNotAdded");
                        //update STUDENTS_ARRAY with Student object

                        currentStudentsCount++;                         //increment student count
                        storeToFile();                                  //save data to file
                        System.out.println("Student Registered successfully!");
                        return;                                         //return from method
                    } else {
                        System.out.println("Enter a valid name!");      //inform if name is not valid
                    }
                }
            } else {
                    System.out.println("Invalid student ID!");              //inform if name is not valid
            }
        }
    }

    //Check the validity of student ID
    private static boolean validStudentID(String IDToCheck) {
        return IDToCheck.length() == 8 && IDToCheck.substring(0, 1).equalsIgnoreCase("W");      //return true if student ID in correct format
    }

    //Format student ID inputs
    private static String formatStudentID(String studentID) {
        return studentID.substring(0, 1).toUpperCase().concat(studentID.substring(1));            //format student and return formatted one
    }

    //Format student name inputs
    private static String formatName(String nameToFormat) {
        String[] nameParts = nameToFormat.split(" ");       //split name into parts and store in nameParts array
        StringBuilder formattedName = new StringBuilder();        //create a new StringBuilder
        for (String namePart : nameParts) {                       //loop through nameParts
            formattedName.append(namePart.substring(0, 1).toUpperCase().concat(namePart.substring(1)));     //update formattedName with formatted name
            formattedName.append(" ");                            //add a whitespace between name parts
        }
        return formattedName.toString().trim();                   //return formatted names
    }

    //Search student names with student ID
    private static void searchStudent() {
        if (currentStudentsCount == 0) {        //check the student count
            System.out.println("No student records available!");
            return;                             //return from the method
        }

        while (true) {      //start an infinite loop
            System.out.println();
            System.out.print("Enter Student ID[w*******] or Enter 'q' to exit: ");      //prompt student ID
            String studentID = GET_INPUT.next().trim();
            GET_INPUT.nextLine();

            if (studentID.equalsIgnoreCase("q")) {      //return from the method if user entered 'q'
                System.out.println("Back to main menu");
                return;
            }

            if (validStudentID(studentID)){         //check the validity of student ID
                for (String[] studentDetails : STUDENT_INFORMATION_ARRAY) {        //loop through STUDENT_INFORMATION_ARRAY
                    if (studentDetails[0].equalsIgnoreCase(studentID)) {           //check for equality for student ID
                        System.out.println("Student ID: " + studentDetails[0]);    //Display student ID
                        System.out.println("Student Name: " + studentDetails[1]);  //Display student name
                        return;                                                    //return from method
                    }
                }
                System.out.println("Student not found!");       //inform if student not found
                return;
            } else {
                System.out.println("Invalid ID! Enter again");      //inform if ID is not valid
            }
        }
    }

    //Display detailed information for a student object
    private static void displayInformation(Student student) {
        System.out.println();
        System.out.println("Student ID    : " + student.getId());       //display student details for a student object with class methods
        System.out.println("Name          : " + student.getName());
//        System.out.println("Module 1 marks: " + student.getModule1().getMarks());
//        System.out.println("Module 2 marks: " + student.getModule2().getMarks());
//        System.out.println("Module 3 marks: " + student.getModule3().getMarks());
        for (int i = 0; i < MODULE_COUNT; i++) {
            System.out.printf("Module %d marks: %.2f%n", i + 1,
                    student.getModules()[i] == null ? 0.0 : student.getModules()[i].getMarks());
        }
        System.out.println("Average mark  : " + student.getAverage());
        System.out.println("Grade         : " + student.getGrade());
        System.out.println();
    }

    //Generate student list according to student names sorted in descending order
    private static void studentList() {
        if (currentStudentsCount == 0) {        //return from the method if student records not available
            System.out.println("No Student records available!");
            return;
        }
        String[][] tempArray = STUDENT_INFORMATION_ARRAY.clone();       //create a temporary array to sort
        for (int i = 0; i < tempArray.length; i++) {                    //loop from 0 to tempArray length
            for (int j = 0; j < tempArray.length - 1 - i; j++) {        //loop from 0 to initial loop variable - 1
                if (tempArray[j][1].trim().isEmpty()) {                 //break the loop if student name is empty
                    break;
                }
                if (tempArray[j][1].compareTo(tempArray[j+1][1]) > 0) {     //compare adjacent student names
                    String[] temp = tempArray[j];                                           //temporary variable to store names
                    tempArray[j] = tempArray[j+1];                          //update array according to names
                    tempArray[j+1] = temp;
                }
            }
        }
        for (String[] studentDetails : tempArray) {                     //loop through tempArray
            if (studentDetails[0].trim().isEmpty()) {                   //break the loop if student ID is empty
                continue;
            }
            System.out.println();
            System.out.println("Student ID: " + studentDetails[0]);     //display student ID, names
            System.out.println("Student Name: " + studentDetails[1]);
            System.out.println();
        }
    }

    //Delete student details, objects with student ID
    private static void deleteStudent() {
        if (currentStudentsCount == 0) {        //check the student count
            System.out.println("No student records available!");
            return;                             //return from the method
        }

        while (true) {      //start an infinite loop
            System.out.println();
            System.out.print("Enter Student ID[w*******] or Enter 'q' to exit: ");      //prompt student ID
            String studentID = GET_INPUT.next().trim();
            GET_INPUT.nextLine();

            if (studentID.equalsIgnoreCase("q")) {      //return from the method if user entered 'q'
                System.out.println("Back to main menu");
                return;
            }

            if (validStudentID(studentID)) {         //check the validity of student ID
                boolean studentFound = false;        //initialize a boolean value to track student existence (studentFound)
                int deleteIndex = 0;                 //initialize a int variable to track delete index (deleteIndex)

                for (Student student : STUDENTS_ARRAY) {                   //loop through STUDENT_INFORMATION_ARRAY
                    if (student.getId().equals("empty")) {                 //break the loop if student name equals to 'empty'
                        break;
                    }
                    if (student.getId().equalsIgnoreCase(studentID)) {      //check for equality for student ID
                        studentFound = true;                                //update studentFound as true
                        System.out.println();
                        System.out.println("Deleting student: " + getParts(student));       //display information about deleting student
                        updateArray(deleteIndex);                                           //call updateArray() method with deleteIndex
                        currentStudentsCount--;                                             //decrement student count
                        storeToFile();                                                      //save data to file
                        System.out.println("Student details deleted successfully!");
                        break;                                                              //break the loop
                    }
                    deleteIndex++;          //increment delete index
                }

                if (!studentFound) {            //check the value of studentFound and display message
                    System.out.println("Student not found!");
                }
                return;     //return from the method
            } else {
                System.out.println("Invalid ID! Enter again");      //inform if name is not valid
            }
        }
    }

    //Call both array restructuring methods
    private static void updateArray(int deleteIndex) {
        restructureStudentInfoArray(deleteIndex);   //call restructureStudentInfoArray() with deleteIndex
        restructureStudentsArray(deleteIndex);      //call restructureStudentsArray() with deleteIndex
    }

    //Restructure Student information array after a deletion
    private static void restructureStudentInfoArray(int deleteIndex) {
        for (int i = deleteIndex; i < currentStudentsCount - 1; i++) {          //loop from 0 to current student count
            STUDENT_INFORMATION_ARRAY[i] = STUDENT_INFORMATION_ARRAY[i + 1];    //update STUDENT_INFORMATION_ARRAY
        }
        STUDENT_INFORMATION_ARRAY[currentStudentsCount - 1] = new String[] {"", ""};    //set last value as initial
    }

    //Restructure Student object array after a deletion
    private static void restructureStudentsArray(int deleteIndex) {
        for (int i = deleteIndex; i < currentStudentsCount - 1; i++) {          //loop from 0 to current student count
            STUDENTS_ARRAY[i] = STUDENTS_ARRAY[i + 1];                          //update STUDENTS_ARRAY
        }
        STUDENTS_ARRAY[currentStudentsCount - 1] = emptyStudentSlot();          //set last value as initial
    }

    //Sub Menu for handle student module details
    private static void subMenu() {
        while (true) {                              //start an infinite loop
            System.out.println("\nSub Menu");       //display Menu messages and prompt user choice
            System.out.println("(A) Update Student Names");
            System.out.println("(B) Enter Module Marks");
            System.out.println("(C) Generate Summary");
            System.out.println("(D) Report");
            System.out.println("(E) Check Marks");
            System.out.println("(q) Main Menu");
            System.out.print("Enter your choice: ");

            char subMenuChoice = GET_INPUT.next().trim().toLowerCase().charAt(0);       //prompt subMenuChoice
            GET_INPUT.nextLine();
            switch (subMenuChoice) {        //switch case to handle Main menu options
                case 'a':
                    updateStudentNames();
                    break;
                case 'b':
                    addModuleDetails();
                    break;
                case 'c':
                    generateSummary();
                    break;
                case 'd':
                    System.out.println("Students list sorted according to average mark");
                    generateReport();
                    break;
                case 'e':
                    checkStudentMarks();
                    break;
                case 'q':
                    System.out.println("Back to Main Menu");
                    return;        //return from the method
                default:           //handle invalid userChoice
                    System.out.println("Invalid choice! Enter again");
                    break;
            }
        }
    }

    //Update registered student names with student ID
    private static void updateStudentNames() {
        if (currentStudentsCount == 0) {        //check the student count
            System.out.println("No student records available!");
            return;                             //return from the method
        }

        while (true) {      //start an infinite loop
            System.out.println();
            System.out.print("Enter Student ID[w*******] or Enter 'q' to exit: ");      //prompt student ID
            String studentID = GET_INPUT.next().trim();
            GET_INPUT.nextLine();

            if (studentID.equalsIgnoreCase("q")) {      //return from the method if user entered 'q'
                System.out.println("Back to main menu");
                return;
            }

            if (validStudentID(studentID)) {        //check the validity of student ID
                for (Student student : STUDENTS_ARRAY) {        //loop through STUDENTS_ARRAY
                    if (student.getId().equals("empty")) {      //break the loop if student name equals to 'empty'
                        break;
                    }
                    if (student.getId().equalsIgnoreCase(studentID)) {                  //check for equality for student ID
                        System.out.println("Registered name: " + student.getName());    //display initially registered name
                        System.out.print("Enter updated student name: ");               //prompt name to update
                        String studentName = GET_INPUT.nextLine().trim();
                        while (true) {      //start an infinite loop
                            if (validStudentName(studentName)) {                        //check the validity of student name
                                String nameToUpdate = formatName(studentName);          //store formatted student name in nameToUpdate
                                student.setName(nameToUpdate);                          //set new student name to the object
                                for (String[] studentDetails : STUDENT_INFORMATION_ARRAY) {         //loop through STUDENT_INFORMATION_ARRAY
                                    if (studentDetails[0].equalsIgnoreCase(studentID)) {            //check for equality for student ID
                                        studentDetails[1] = nameToUpdate;                           //update new name in the STUDENT_INFORMATION_ARRAY
                                        storeToFile();                                              //save data to file
                                        break;                                                      //break the loop
                                    }
                                }
                                System.out.println("Student name updated successfully");
                                return;         //return from the method
                            } else {
                                System.out.println("Enter a valid name!");      //inform if name is not valid
                            }
                        }
                    }
                }
            }
        }
    }

    //Validate student Name
    private static boolean validStudentName(String nameToCheck) {
        return !nameToCheck.trim().isEmpty();       //return true if nameToCheck length is not empty
    }

    //Add module marks with student ID
    private static void addModuleDetails() {
        if (currentStudentsCount == 0) {        //check the student count
            System.out.println("No student records available!");
            return;                             //return from the method
        }

        while (true) {      //start an infinite loop
            System.out.println();
            System.out.print("Enter Student ID[w*******] or Enter 'q' to exit: ");      //prompt student ID
            String studentID = GET_INPUT.next().trim();
            GET_INPUT.nextLine();

            if (studentID.equalsIgnoreCase("q")) {      //return from the method if user entered 'q'
                System.out.println("Back to main menu");
                return;
            }

            if (validStudentID(studentID)) {        //check the validity of student ID
                boolean studentFound = false;       //initialize a boolean value to track student existence (studentFound)

                for (Student student : STUDENTS_ARRAY) {        //loop through STUDENT_INFORMATION_ARRAY
                    if (student.getId().equals("empty")) {      //break the loop if student name equals to 'empty'
                        break;
                    }
                    if (student.getId().equalsIgnoreCase(studentID)) {      //check for equality for student ID
                        if (student.isMarkAdded().equals("MarksAdded")) {   //check isMarksAdded
                            System.out.println();                           //if "MarksAdded" display initial marks
                            Module[] modules = student.getModules();
                            System.out.println("You are updating existing marks");
                            for (int i = 0; i < modules.length; i++) {
                                System.out.printf("Module %s: ", i+1);
                                System.out.println(modules[i].getMarks());
                            }
//                            System.out.printf("You are updating existing marks (Module1: %s)(Module2: %s)(Module3: %s)"
//                                    , student.getModule1().getMarks()
//                                    , student.getModule2().getMarks()
//                                    , student.getModule3().getMarks());
                            System.out.println();
                        }
                        for (String[] studentDetails : STUDENT_INFORMATION_ARRAY) {       //loop through STUDENTS_ARRAY
                            if (studentDetails[0].equalsIgnoreCase(studentID)) {          //check for equality for student ID
                                String registeredName = studentDetails[1];                //get registeredName from studentDetails
                                student.setName(registeredName);                          //set name for student
                                System.out.println("Add marks for Student: " + registeredName);     //display registered name
                                break;                                                     //break the loop
                            }
                        }
                        List<Double> moduleMarks = new ArrayList<>();
                        for (int i = 0; i < MODULE_COUNT; i++) {
                            double moduleMark = getModuleMarks(String.format("Enter mark for Module %d: ", i + 1));
                            student.getModules()[i].setMarks(moduleMark);
                        }
//                        double module1Marks = getModuleMarks("Enter mark for Module 1: ");      //call getModuleMarks() with messages
//                        double module2Marks = getModuleMarks("Enter mark for Module 2: ");
//                        double module3Marks = getModuleMarks("Enter mark for Module 3: ");

//                        student.getModule1().setMarks(module1Marks);        //set student marks with user input values
//                        student.getModule2().setMarks(module2Marks);
//                        student.getModule3().setMarks(module3Marks);
                        student.addingMarks("MarksAdded");        //set isMarksAdded as "MarksAdded"
                        storeToFile();                                       //save data to file
                        System.out.println("Module marks updated successfully!");
                        return;                                              //return from method
                    }
                }

                if (!studentFound) {            //check the value of studentFound and display message
                    System.out.println("Student not found!");
                    return;
                }
            } else {
                System.out.println("Invalid ID! Enter again");      //inform if name is not valid
            }
        }
    }

    //Validate student mark inputs
    private static double getModuleMarks(String message) {
        double moduleMark;      //declare double moduleMark
        while (true){           //start an infinite loop
            System.out.print(message);      //display String message
            try{                //try to input marks
                moduleMark = GET_INPUT.nextDouble();
                if (moduleMark > 100 || moduleMark < 0){        //check the validity of marks
                    System.out.println("Marks should in range (0-100)");
                } else {
                    return moduleMark;      //return moduleMark
                }
            } catch (InputMismatchException e){         //catch InputMismatchException
                System.out.println("Number required for marks!");    //inform if mark is not valid
                GET_INPUT.next();
            }
        }
    }

    //Check student marks with student ID
    private static void checkStudentMarks() {
        if (currentStudentsCount == 0) {        //check the student count
            System.out.println("No student records available!");
            return;                             //return from the method
        }

        while (true) {      //start an infinite loop
            System.out.println();
            System.out.print("Enter Student ID[w*******] or Enter 'q' to exit: ");      //prompt student ID
            String studentID = GET_INPUT.next().trim();
            GET_INPUT.nextLine();

            if (studentID.equalsIgnoreCase("q")) {      //return from the method if user entered 'q'
                System.out.println("Back to main menu");
                return;
            }

            if (validStudentID(studentID)) {        //check the validity of student ID
                for (Student student : STUDENTS_ARRAY) {        //loop through STUDENTS_ARRAY
                    if (student.getName().equals("empty")) {    //break the loop if student name equals to 'empty'
                        break;
                    }
                    if (student.getId().equalsIgnoreCase(studentID)) {      //check for equality for student ID
                        System.out.println();
                        System.out.println("Student ID    : " + student.getId());       //display student, module details for equal ID
                        System.out.println("Name          : " + student.getName());
//                        System.out.println("Module 1 marks: " + student.getModule1().getMarks());
//                        System.out.println("Module 2 marks: " + student.getModule2().getMarks());
//                        System.out.println("Module 3 marks: " + student.getModule3().getMarks());
                        for (int i = 0; i < MODULE_COUNT; i++) {
                            double marks = (student.getModules()[i] == null) ? 0.0 : student.getModules()[i].getMarks();
                            System.out.printf("Module %d marks: %.2f%n", i + 1, marks);
                        }
                        System.out.println("Average mark  : " + student.getAverage());
                        System.out.println("Grade         : " + student.getGrade());
                        System.out.println();
                        return;         //return from the method
                    }
                }
            } else {
                System.out.println("Invalid ID! Enter again");      //inform if name is not valid
            }
        }
    }

    //Generate a summary according to module marks
    private static void generateSummary() {
        if (currentStudentsCount == 0) {        //check the student count
            System.out.println("No student records available!");
            return;                             //return from the method
        }
        int studentsWithMoreThan40ForEveryModule = 0;       //initialize variables to store module detail
        int studentsWithMoreThan40ForModule1 = 0;
        int studentsWithMoreThan40ForModule2 = 0;
        int studentsWithMoreThan40ForModule3 = 0;

        for (Student student : STUDENTS_ARRAY) {        //loop through STUDENTS_ARRAY
            if (student.getId().equals("empty")) {      //break the loop if student name equals to 'empty'
                break;
            }
            double mark1 = student.getModule1() == null ? 0 : student.getModule1().getMarks();     //store marks derived from student objects
            double mark2 = student.getModule2() == null ? 0 : student.getModule2().getMarks();
            double mark3 = student.getModule3() == null ? 0 : student.getModule3().getMarks();
            if (mark1 >= 40) {                                   //check mark ranges for modules
                studentsWithMoreThan40ForModule1++;
            }
            if (mark2 >= 40) {
                studentsWithMoreThan40ForModule2++;
            }
            if (mark3 >= 40) {
                studentsWithMoreThan40ForModule3++;
            }
            if (mark1 >= 40 && mark2 >= 40 && mark3 >= 40) {
                studentsWithMoreThan40ForEveryModule++;
            }
        }
        System.out.println();       //display module details
        System.out.println("Total student registrations: " + currentStudentsCount);
        System.out.println("Students count scored more than 40 in module 1 : " + studentsWithMoreThan40ForModule1);
        System.out.println("Students count scored more than 40 in module 2 : " + studentsWithMoreThan40ForModule2);
        System.out.println("Students count scored more than 40 in module 3 : " + studentsWithMoreThan40ForModule3);
        System.out.println("Students count scored more than 40 in every module   : " + studentsWithMoreThan40ForEveryModule);
        System.out.println();
    }

    //Generate student report according to average mark sorted in descending order
    private static void generateReport() {
        if (currentStudentsCount == 0) {        //check the student count
            System.out.println("No Student records available!");
            return;                             //return from the method
        }
        Student[] tempArray = STUDENTS_ARRAY.clone();               //create a temporary array to sort
        for (int i = 0; i < currentStudentsCount; i++){             //loop from 0 to tempArray length
            for (int j = 0; j <currentStudentsCount - i - 1; j++){  //loop from 0 to initial loop variable - 1
                if (tempArray[j].getId().equals("empty")) {         //break the loop if student name is empty
                    break;
                }
                if (tempArray[j].getAverage() < tempArray[j+1].getAverage()) {      //compare adjacent student module average
                    Student temp = tempArray[j];                                    //temporary variable to store names
                    tempArray[j] = tempArray[j+1];                                  //update array according to average
                    tempArray[j+1] = temp;
                }
            }
        }
        detailedReport(tempArray);      //call detailedReport() with tempArray
    }

    //Call displayInformation() method in a loop with student objects
    private static void detailedReport(Student[] arrayToGenerateReport) {
        for (Student student : arrayToGenerateReport) {     //loop through arrayToGenerateReport array
            if (!student.getId().equals("empty")) {         //break the loop if student id equals 'empty'
                displayInformation(student);                //call displayInformation() with student
            }
        }
    }

    //Save student details from array to file
    private static void storeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {       //file writing process
            for (Student student: STUDENTS_ARRAY) {         //loop through STUDENTS_ARRAY
                if (student.getId().equals("empty")) {      //break the loop if student id equals 'empty'
                    break;
                }
                String parts = getParts(student);           //get String parts from the getParts() with student
                writer.write(parts);                        //write parts to file
                writer.newLine();                           //write a newline
            }
        } catch (IOException e) {
            System.out.println("Error while storing student details into file");
        }
    }

    //Get formatted string parts from student objects to write in the file
    private static String getParts(Student object) {
        String id = object.getId();         //derive String parts from student objects
        String name = object.getName();
//        String module1Marks = String.valueOf(object.getModule1() == null ? 0 : object.getModule1().getMarks());
//        String module2Marks = String.valueOf(object.getModule2() == null ? 0 : object.getModule2().getMarks());
//        String module3Marks = String.valueOf(object.getModule3() == null ? 0 : object.getModule3().getMarks());
        StringBuilder moduleMarksString = new StringBuilder();
        for (int i = 0; i < MODULE_COUNT; i++) {
            double mark = object.getModules()[i] == null ? 0 : object.getModules()[i].getMarks();
            moduleMarksString.append(String.valueOf(mark));
            moduleMarksString.append(", ");
        }

        String isMarksAdded = object.isMarkAdded();
        return id + ", " + name + ", " + moduleMarksString.toString()  + isMarksAdded;    //return formatted String
    }

    //Load student details from file to system
    private static void loadFromFile() {
        try (FileReader reader = new FileReader(FILE_NAME); Scanner scanFile = new Scanner(reader)) {       //file reading process
            int savedStudentCount = 0;      //initialize int savedStudentCount to track number of lines
            while (scanFile.hasNextLine()) {            //start a loop while file has a next line
                String line = scanFile.nextLine();      //get the content into line
                if (line.isEmpty()) {                   //skip if line is empty
                    continue;
                }
                try {
                    String[] stringParts = line.split(", ");     //split parts from the line into stringParts
                    if (stringParts.length < 6) {                      //check for the valid size
                        System.out.println("Invalid data size found while loading marks: " + line);
                        continue;
                    }
                    String id = stringParts[0].trim();      //derive parts from stringParts
                    String name = stringParts[1].trim();
                    double module1Marks = Double.parseDouble(stringParts[2]);
                    double module2Marks = Double.parseDouble(stringParts[3]);
                    double module3Marks = Double.parseDouble(stringParts[4]);
                    String isMarksAdded = stringParts[5].trim();

                    Module module1 = new Module(module1Marks);      //create modules from loaded details
                    Module module2 = new Module(module2Marks);
                    Module module3 = new Module(module3Marks);

                    Module[] modules = new Module[]{module1, module2, module3};

                    if (savedStudentCount >= STUDENTS_ARRAY.length) {   //check the STUDENTS_ARRAY length
                        System.out.println("Maximum capacity of students has reached! Extra data will not be added!");
                        break;
                    }

                    STUDENTS_ARRAY[savedStudentCount] = new Student(id, name, modules, isMarksAdded);
                    //create student objects from loaded details and store in STUDENTS_ARRAY

                    STUDENT_INFORMATION_ARRAY[savedStudentCount][0] = id;       //update name, ID in STUDENT_INFORMATION_ARRAY
                    STUDENT_INFORMATION_ARRAY[savedStudentCount][1] = name;
                    savedStudentCount++;    //increment savedStudentCount
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format found while loading marks: " + line);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Invalid data size found while loading marks: " + line);
                }
            }
            currentStudentsCount = savedStudentCount;    //update currentStudentsCount with savedStudentCount
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error while loading student details from file");
        }
    }
}
