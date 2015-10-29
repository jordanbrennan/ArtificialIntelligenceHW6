import java.util.*;

public class AC3 extends Backtracking{
    
    public AC3(int[][] constraints, char[] values) {
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

                //do arc consistency. if failure is detected, undo assignment
                List<Map.Entry<Integer, Character>> changesMade = ACPropagate(constraints);
                if (changesMade != null) {
                    //early failure not detected. proceed to next state
                    //using recursion, if success then return
                    if (RecursiveBacktracking(constraints)) return true;
                    
                    //if recursion fails, undo effects of assignment
                    UndoChanges(changesMade);
                }

                //remove value from assignment
                assignment[variable] = 'Z';
            }
        }
        //return failure
        return false;
    }
    
    private List<Map.Entry<Integer, Character>> ACPropagate (int[][]constraints){
        
        List<Map.Entry<Integer, Character>> changesMade = new ArrayList<>();
        List<Map.Entry<Integer, Character>> changes;
        Integer[] currArc;

        //itialize queue with all existing arcs
        LinkedList<Integer[]> queue = new LinkedList();
        for (int i=0; i<constraints.length; i++) {
            for (int j=0; i<constraints.length; i++) {
                if (constraints[i][j] == 1) {
                    Integer[] arc = {i,j};
                    queue.add(arc);
                }
            }
        }
        
        while (! queue.isEmpty()) {
            currArc = queue.removeFirst();
            changes = RemoveInconsistentValues(currArc[0], currArc[1]);
            if (changes == null) {
                //detect early failure
                UndoChanges(changesMade);
                return null;
            }
            if (! changes.isEmpty()) {
                changesMade.addAll(changes);
                for (int i=0; i<constraints.length; i++) {
                    if (constraints[currArc[0]][i] == 1) {
                        Integer[] arc = {i, currArc[0]};
                        queue.add(arc);
                    }
                }
            }
        }
        
        return changesMade;
    }
    
    private List<Map.Entry<Integer, Character>> RemoveInconsistentValues(int xi, int xj) {
        List<Map.Entry<Integer, Character>> changesMade = new ArrayList<>();
        boolean foundOne;
        
        for (Character x : remainingVals.get(xi)) {
            foundOne = false;
            for (Character y: remainingVals.get(xj)) {
                if (! x.equals(y)) foundOne = true;
            }
            if (! foundOne) {
                remainingVals.get(xi).remove(x);
                changesMade.add(new AbstractMap.SimpleEntry<>(xi, x));
                if (remainingVals.get(xi).isEmpty()) {
                    UndoChanges(changesMade);
                    return null; //indicate failure
                }
            }
        }
        return changesMade;
    }
    
    private void UndoChanges(List<Map.Entry<Integer, Character>> changes) {
        for (Map.Entry<Integer, Character> change : changes) {
            remainingVals.get(change.getKey()).add(change.getValue());
        }
    }
}
