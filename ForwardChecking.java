import java.util.ArrayList;

public class ForwardChecking extends Backtracking {
    
    public ForwardChecking(int[][] constraints, char[] values) {
        super(constraints, values);
    }
    
    /**
     * implements backtracking search
     * @param constraints
     * @return true/false success
     */
    @Override
    protected boolean RecursiveBacktracking( int[][] constraints) {

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
        for (int i = 0; i < remainingVals.get(variable).size(); i++) {

            //if value is consistent
            if (ValueIsConsistent(remainingVals.get(variable).get(i), variable, constraints)) {

                //add value to variable in assignment
                assignment[variable] = remainingVals.get(variable).get(i);

                System.out.println((variable + 1) + "<-" + remainingVals.get(variable).get(i) + "  ");

                //do forward checking. if failure is detected, undo assignment
                ArrayList<Integer> varsAffected = ForwardCheck(variable, constraints);
                if (varsAffected != null) {
                    //early failure not detected. proceed to next state
                    //using recursion, if success then return
                    if (RecursiveBacktracking(constraints)) return true;
                    
                    //if recursion fails, undo effects of assignment
                    for (Integer var : varsAffected) {
                        remainingVals.get(var).add(remainingVals.get(variable).get(i));
                    }
                }

                //remove value from assignment
                assignment[variable] = 'Z';
            }
        }
        //return failure
        return false;
    }
    
    private ArrayList<Integer> ForwardCheck(int variable, int[][]constraints){
        
        ArrayList<Integer> varsChanged = new ArrayList<>();
        Character currChar = assignment[variable];
        
        //iterate through variables
        for (int i = 0; i < constraints.length; i++) {
            //if variable has connection, remove currChar as valid value
            if (constraints[variable][i] == 1 && remainingVals.get(i).remove(currChar)) {
                //if currChar was present, remember that we changed it
                varsChanged.add(i);
                if (remainingVals.get(i).isEmpty()) {
                    //Our assignment resulted in another variable with no possible
                    // values. Undo our changes and return failure indicator
                    for (Integer var : varsChanged) {
                        remainingVals.get(var).add(currChar);
                    }
                    return null; //indicates that failure was detected
                }
            }
        }

        return varsChanged;
    }
}
