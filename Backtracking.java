import java.util.Arrays;

public class Backtracking {

    private char[] assignment;
    private int[] degrees;
    private int[] mrv;
    private char[] values;

    public Backtracking(int[][] constraints, char[] values) {
        assignment = new char[constraints.length];
        Arrays.fill(assignment, 'Z');

        //find number of degrees
        degrees = new int[constraints.length];
        Arrays.fill(degrees, 0);
        for (int i = 0; i < constraints.length; i++) {
            for (int j = 0; j < constraints.length; j++) {
                if (constraints[i][j] == 1) degrees[i]++;
            }
        }

        mrv = new int[constraints.length];
        Arrays.fill(mrv, values.length);

        this.values = values;
    }

    /**
     * Starts backtracking search
     * @param constraints read from file
     */
    public void BacktrackingSearch(int[][] constraints) {
        long startTime = System.nanoTime();

        //start search and print results if solution found
        if (RecursiveBacktracking(constraints)) {
            System.out.println("Solution: ");
            for (int i = 0; i < assignment.length; i++)
                System.out.println("Variable " + (i + 1) + ": " + assignment[i]);
        }
        else System.out.println("Failure");

        long stopTime = System.nanoTime();
        System.out.println("Time: " + ((stopTime - startTime)/1000000.00));
    }

    /**
     * implements backtracking search
     * @param constraints
     * @return true/false success
     */
    private boolean RecursiveBacktracking( int[][] constraints) {

        boolean complete = true;

        //if assignment is complete, end
        for (int i = 0; i < assignment.length; i++)
            if (assignment[i] == 'Z') {
                complete = false;
                break;
            }
        if (complete) return true;

        //select unassigned variable
        int variable = SelectUnassignedVariable();

        //iterate through values
        for (int i = 0; i < values.length; i++) {

            //if value is consistent
            if (ValueIsConsistent(values[i], variable, constraints)) {

                //add value to variable in assignment
                assignment[variable] = values[i];

                System.out.println((variable + 1) + "<-" + values[i] + "  ");

                //proceed to next state using recursion, if success then return
                if (RecursiveBacktracking(constraints)) return true;

                //remove value from assignment
                assignment[variable] = 'Z';
            }
        }
        //return failure
        return false;
    }

    /**
     * selects an unassigned variable with MRV and degree heuristics
     * @return the variable
     */
    private int SelectUnassignedVariable() {

        int variable = 0;
        boolean init = true;

        //loop through variables
        for (int i = 0; i < assignment.length; i++) {
            //select only if variable is unassigned
            if (assignment[i] == 'Z') {
                //initialize to first variable processed
                if (init) {
                    variable = i;
                    init = false;
                }
                //check MRV heuristic
                if (mrv[i] < mrv[variable]) variable = i;
                else if (mrv[i] == mrv[variable])
                    //check degree heuristic
                    if (degrees[i] < degrees[variable]) variable = i;
            }
        }

        return variable;
    }

    /**
     * Checks if value is consistent with assignment
     * @param value to be assigned
     * @param variable to be assigned
     * @param constraints
     * @return
     */
    private boolean ValueIsConsistent(int value, int variable, int[][]constraints) {

        //iterate through variables
        for (int i = 0; i < constraints.length; i++) {
            //check if variable has connection with same value
            if (constraints[variable][i] == 1 && assignment[i] == value)
                return false;
        }

        return true;
    }

}
