package kindergarten;
/**
 * This class represents a Classroom, with:
 * - an SNode instance variable for students in line,
 * - an SNode instance variable for musical chairs, pointing to the last student in the list,
 * - a boolean array for seating availability (eg. can a student sit in a given seat), and
 * - a Student array parallel to seatingAvailability to show students filed into seats 
 * --- (more formally, seatingAvailability[i][j] also refers to the same seat in studentsSitting[i][j])
 * 
 * @author Ethan Chou
 * @author Kal Pandit
 * @author Maksims Kurjanovics Kravcenko
 */
public class Classroom {
    private SNode studentsInLine;         // when students are in line: references the FIRST student in the LL
    private SNode musicalChairs;          // when students are in musical chairs: references the LAST student in the CLL
    private boolean[][] seatingLocation;  // represents the classroom seats that are available to students
    private Student[][] studentsSitting;  // when students are sitting in the classroom: contains the students

    /**
     * Constructor for classrooms. Do not edit.
     * @param l passes in students in line
     * @param m passes in musical chairs
     * @param a passes in availability
     * @param s passes in students sitting
     */
    public Classroom ( SNode l, SNode m, boolean[][] a, Student[][] s ) {
		studentsInLine  = l;
        musicalChairs   = m;
		seatingLocation = a;
        studentsSitting = s;
	}
    /**
     * Default constructor starts an empty classroom. Do not edit.
     */
    public Classroom() {
        this(null, null, null, null);
    }

    /**
     * This method simulates students standing in line and coming into the classroom (not leaving the line).
     * 
     * It does this by reading students from input file and inserting these students studentsInLine singly linked list.
     * 
     * 1. Open the file using StdIn.setFile(filename);
     * 
     * 2. For each line of the input file:
     *     1. read a student from the file
     *     2. create an object of type Student with the student information
     *     3. insert the Student object at the FRONT of the linked list
     * 
     * Input file has:
     * 1) one line containing an integer representing the number of students in the file, say x
     * 2) x lines containing one student per line. Each line has the following student 
     * information separated by spaces: FirstName LastName Height
     * 
     * To read a string using StdIn use StdIn.readString()
     * 
     * The input file has Students in REVERSE alphabetical order. So, at the end of this
     * method all students are in line in alphabetical order.
     * 
     * DO NOT implement a sorting method, PRACTICE add to front.
     * 
     * @param filename the student information input file
     */
    public void enterClassroom(String filename) {
        // Open the file using StdIn.setFile(filename);
        StdIn.setFile(filename);
    
        // Read the number of students in the file
        int numStudents = StdIn.readInt();
    
        // For each student in the file:
        for (int i = 0; i < numStudents; i++) {
            // Read the student's information
            String firstName = StdIn.readString();
            String lastName = StdIn.readString();
            int height = StdIn.readInt();
    
            // Create a Student object with the information
            Student student = new Student(firstName, lastName, height);
    
            // Insert the student at the FRONT of the linked list
            SNode newNode = new SNode(student,studentsInLine);
            newNode.setNext(studentsInLine);
            studentsInLine = newNode;
        }
    }
    

    /**
     * 
     * This method creates and initializes the seatingAvailability (2D array) of 
     * available seats inside the classroom. Imagine that unavailable seats are broken and cannot be used.
     *      
     * 1. Open the file using StdIn.setFile(seatingChart);
     * 
     * 2. You will read the seating chart input file with the format:
     * An integer representing the number of rows in the classroom, say r
     * An integer representing the number of columns in the classroom, say c
     * Number of r lines, each containing c true or false values (true represents that a 
     * seat is present in that column)
     * 
     * 3. Initialize seatingLocation and studentsSitting arrays with r rows and c columns
     * 
     * 4. Update seatingLocation with the boolean values read from the input file
     * 
     * This method does not seat students on the seats.
     * 
     * @param seatingChart the seating chart input file
     */
    public void setupSeats(String seatingChart) {
        // Open the file using StdIn.setFile(seatingChart);
        StdIn.setFile(seatingChart);
    
        // Read the number of rows and columns
        int numRows = StdIn.readInt();
        int numCols = StdIn.readInt();
    
        // Initialize seatingLocation and studentsSitting arrays with r rows and c columns
        seatingLocation = new boolean[numRows][numCols];
        studentsSitting = new Student[numRows][numCols];
    
        // Update seatingLocation with the boolean values read from the input file
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                boolean seatAvailable = StdIn.readBoolean();
                seatingLocation[row][col] = seatAvailable;
            }
        }
    }  
    
    /**
     * 
     * This method simulates students standing inline and then taking their seats in the classroom.
     * 
     * 1. Starting from the front of the studentsInLine singly linked list
     * 2. Remove one student at a time from the list and insert the student into studentsSitting according to
     *    seatingLocations
     * 
     * studentsInLine will then be empty
     * 
     * If the students just played musical chairs, the winner of musical chairs is seated separately 
     * by seatMusicalChairsWinner().
     */
    public void seatStudents() {
        SNode current = studentsInLine;
        while (current != null) {
            // Remove one student from the line
            SNode removedNode = current;
            current = current.getNext();
            studentsInLine = current;
            removedNode.setNext(null);
    
            // Find an available seat for the student
            for (int i = 0; i < seatingLocation.length; i++) {
                for (int j = 0; j < seatingLocation[i].length; j++) {
                    if (seatingLocation[i][j]) {
                        // If the seat is available, seat the student there
                        seatingLocation[i][j] = false;
                        studentsSitting[i][j] = removedNode.getStudent();
                        break;
                    }
                }
            }
        }
    }
    /**
     * Traverses studentsSitting row-wise (starting at row 0) removing a seated
     * student and adding that student to the end of the musicalChairs list.
     * 
     * row-wise: starts at index [0][0] traverses the entire first row and then moves
     * into second row.
     */
    public void insertMusicalChairs() {
        // Traverse studentsSitting row-wise
        for (int row = 0; row < studentsSitting.length; row++) {
            for (int col = 0; col < studentsSitting[0].length; col++) {
                // If there is a seated student in this seat
                if (studentsSitting[row][col] != null) {
                    // Add the student to the end of the musicalChairs list
                    if (musicalChairs == null) {
                        // If the list is currently empty, set the first node to the student
                        musicalChairs = new SNode(studentsSitting[row][col], musicalChairs);
                    } else {
                        // Otherwise, traverse the list to the end and add a new node with the student
                        SNode current = musicalChairs;
                        while (current.getNext() != null) {
                            current = current.getNext();
                        }
                        current.setNext(new SNode(studentsSitting[row][col], current));
                    }
                    
                    // Remove the student from the corresponding seat
                    studentsSitting[row][col] = null;
                    seatingLocation[row][col] = false;
                }
            }
        }
    }
    

    /**
     * 
     * Removes a random student from the musicalChairs.
     * 
     * @param size represents the number of students currently sitting in musicalChairs
     * 
     * 1. Select a random student to be removed from the musicalChairs CLL.
     * 2. Searches for the selected student, deletes that student from musicalChairs
     * 3. Calls insertByHeight to insert the deleted student into studentsInLine list.
     * 
     * Requires the use of StdRandom.uniform(int b) to select a random student to remove,
     * where b is the number of students currently sitting in the musicalChairs.
     * 
     * The random value denotes the refers to the position of the student to be removed
     * in the musicalChairs. 0 is the first student
     */
    public void moveStudentFromChairsToLine(int size) {
        // select a random student position to remove
        int removePos = StdRandom.uniform(size);
    
        // find the node to remove
        SNode nodeToRemove = musicalChairs;
        for (int i = 0; i < removePos; i++) {
            nodeToRemove = nodeToRemove.getNext();
        }
    
        // remove the node from musicalChairs
        musicalChairs.setNext(nodeToRemove.getNext());
        size--;
    
        // add the removed node to studentsInLine
        SNode nodeToAdd = nodeToRemove;
        Student studentToAdd = nodeToAdd.getStudent();
        insertByHeight(studentToAdd);
    }

    /**
     * Inserts a single student, eliminated from musical chairs, to the studentsInLine list.
     * The student is inserted in ascending order by height (shortest to tallest).
     * 
     * @param studentToInsert the student eliminated from chairs to be inserted into studentsInLine
     */
    public void insertByHeight(Student studentToInsert) {
        SNode newNode = new SNode();
        SNode current = studentsInLine;
        SNode prev = null;

        // Traverse the linked list to find the position to insert the new node
        while (current != null && studentToInsert.getHeight() > current.getStudent().getHeight()) {
            prev = current;
            current = current.getNext();
        }

        // If the new node is the first node
        if (prev == null) {
            newNode.setNext(studentsInLine);
            studentsInLine = newNode;
        } else {
            newNode.setNext(current);
            prev.setNext(newNode);
        }
    }

    /**
     * 
     * Removes eliminated students from the musicalChairs and inserts those students 
     * back in studentsInLine in ascending height order (shortest to tallest).
     * 
     * At the end of this method, all students will be in studentsInLine besides 
     * the winner, who will be in musicalChairs alone.
     * 
     * 1. Find the number of students currently on musicalChairs
     * 2. While there are more than 1 student in musicalChairs, call moveRandomStudentFromChairsToLine()
     */
    public void eliminateLosingStudents() {
        int numStudentsOnChairs = 0;
        SNode current = musicalChairs;
        
        // Count the number of students currently on the musicalChairs
        while (current != null) {
            numStudentsOnChairs++;
            current = current.getNext();
            if (current == musicalChairs) {
                break;
            }
        }
        
        // Move students from the musicalChairs to studentsInLine, until there is only one student left
        while (numStudentsOnChairs > 1) {
            moveRandomStudentFromChairsToLine();
            numStudentsOnChairs--;
        }
    }
    private void moveRandomStudentFromChairsToLine() {
        // Find a random seat with a student on it
        int randomRow = -1, randomCol = -1;
        SNode currentChair = musicalChairs;
        do {
            randomRow = StdRandom.uniform(seatingLocation.length);
            randomCol = StdRandom.uniform(seatingLocation[0].length);
            currentChair = currentChair.getNext();
            if (currentChair == musicalChairs) {
                // All chairs have been checked, there are no students
                return;
            }
        } while (!seatingLocation[randomRow][randomCol] || studentsSitting[randomRow][randomCol] == null || currentChair == musicalChairs);
    
        // Remove the student from the seat and musicalChairs
        seatingLocation[randomRow][randomCol] = false;
        Student removedStudent = studentsSitting[randomRow][randomCol];
        studentsSitting[randomRow][randomCol] = null;
        SNode previousChair = musicalChairs;
        while (previousChair.getNext() != currentChair) {
            previousChair = previousChair.getNext();
        }
        previousChair.setNext(currentChair.getNext());
        musicalChairs = previousChair.getNext();
    
        // Insert the student at the end of the studentsInLine list
        SNode currentStudent = studentsInLine;
        while (currentStudent.getNext() != null) {
            currentStudent = currentStudent.getNext();
        }
        currentStudent.setNext(new SNode(removedStudent,null));
    }
    
    
    
    /* 
     * If musicalChairs (circular linked list) contains ONLY ONE student (the winner), 
     * this method removes the winner from musicalChairs and inserts that student 
     * into the first available seat in studentsSitting. musicChairs will then be empty.
     * 
     * This only happens when the musical chairs was just played.
     * 
     * This methods does nothing if there is more than one student in musicalChairs 
     * or if musicalChairs is empty.
     */
    public Student seatMusicalChairsWinner() {
        SNode currentNode = studentsInLine;
        int numSeats = seatingLocation[0].length;
        int remainingStudents = 0;
        int numEliminations = 0;
        int currentIndex = 0;
    
        // Count the number of remaining students
        while (currentNode != null) {
            remainingStudents++;
            currentNode = currentNode.getNext();
        }
    
        currentNode = studentsInLine;
    
        // Use a loop instead of a while loop
        for (currentIndex = 0; remainingStudents > 1; currentIndex++) {
            if (seatingLocation[currentIndex / numSeats][currentIndex % numSeats]) {
                studentsSitting[currentIndex / numSeats][currentIndex % numSeats] = currentNode.getStudent();
                remainingStudents--;
            } else {
                numEliminations++;
            }
            currentNode = currentNode.getNext();
        }
    
        SNode winnerNode = currentNode;
        for (int i = 0; i < numEliminations; i++) {
            winnerNode = winnerNode.getNext();
        }
    
        return winnerNode.getStudent();
    }
    
    
      

    /**
     * 
     * This method represents the game of musical chairs!
     * 
     * This method calls previously written methods to repeatedly remove students from 
     * the musicalChairs until there is only one student (the winner), seats the winner
     * and seat the students from the studentsInline.
     * 
     * *****DO NOT****** UPDATE THIS METHOD
     */
    public void playMusicalChairs() {

	/* DO NOT UPDATE THIS METHOD */
        eliminateLosingStudents();
        seatMusicalChairsWinner();
        seatStudents();
    } 

    /**
     * Used by driver to display students in line
     * DO NOT edit.
     */
    public void printStudentsInLine () {

        //Print studentsInLine
        StdOut.println ( "Students in Line:" );
        if ( studentsInLine == null ) { StdOut.println("EMPTY"); }

        for ( SNode ptr = studentsInLine; ptr != null; ptr = ptr.getNext() ) {
            StdOut.print ( ptr.getStudent().print() );
            if ( ptr.getNext() != null ) { StdOut.print ( " -> " ); }
        }
        StdOut.println();
        StdOut.println();
    }

    /**
     * Prints the seated students; can use this method to debug.
     * DO NOT edit.
     */
    public void printSeatedStudents () {

        StdOut.println("Sitting Students:");

        if ( studentsSitting != null ) {
        
            for ( int i = 0; i < studentsSitting.length; i++ ) {
                for ( int j = 0; j < studentsSitting[i].length; j++ ) {

                    String stringToPrint = "";
                    if ( studentsSitting[i][j] == null ) {

                        if (seatingLocation[i][j] == false) {stringToPrint = "X";}
                        else { stringToPrint = "EMPTY"; }

                    } else { stringToPrint = studentsSitting[i][j].print();}

                    StdOut.print ( stringToPrint );
                    
                    for ( int o = 0; o < (10 - stringToPrint.length()); o++ ) {
                        StdOut.print (" ");
                    }
                }
                StdOut.println();
            }
        } else {
            StdOut.println("EMPTY");
        }
        StdOut.println();
    }

    /**
     * Prints the musical chairs; can use this method to debug.
     * DO NOT edit.
     */
    public void printMusicalChairs () {
        StdOut.println ( "Students in Musical Chairs:" );

        if ( musicalChairs == null ) {
            StdOut.println("EMPTY");
            StdOut.println();
            return;
        }
        SNode ptr;
        for ( ptr = musicalChairs.getNext(); ptr != musicalChairs; ptr = ptr.getNext() ) {
            StdOut.print(ptr.getStudent().print() + " -> ");
        }
        if ( ptr == musicalChairs) {
            StdOut.print(musicalChairs.getStudent().print() + " - POINTS TO FRONT");
        }
        StdOut.println();
    }

    /**
     * Prints the state of the classroom; can use this method to debug.
     * DO NOT edit.
     */
    public void printClassroom() {
        printStudentsInLine();
        printSeatedStudents();
        printMusicalChairs();
    }

    /**
     * Used to get and set objects.
     * DO NOT edit.
     */

    public SNode getStudentsInLine() { return studentsInLine; }
    public void setStudentsInLine(SNode l) { studentsInLine = l; }

    public SNode getMusicalChairs() { return musicalChairs; }
    public void setMusicalChairs(SNode m) { musicalChairs = m; }

    public boolean[][] getSeatingLocation() { return seatingLocation; }
    public void setSeatingLocation(boolean[][] a) { seatingLocation = a; }

    public Student[][] getStudentsSitting() { return studentsSitting; }
    public void setStudentsSitting(Student[][] s) { studentsSitting = s; }

}
