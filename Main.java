import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        //ensure argument contains both file names
        if (args.length != 1) {
            System.out.println("Incorrect number of arguments.");
            return;
        }

        //contains matrix of constraints between variables
        int[][] constraints;

        try {
            //get data from files
            constraints = readFile(args[0]);

            //for problem one, add 'd' for problem two
            char[] values = {'a', 'b', 'c', 'd'};
            Backtracking BTS = new Backtracking(constraints, values);

            //implement backtracking search
            BTS.BacktrackingSearch(constraints);
            
            //implement forward checking
            ForwardChecking FC = new ForwardChecking(constraints, values);
            FC.BacktrackingSearch(constraints);
            
            //implement AC3
            AC3 ac3 = new AC3(constraints, values);
            ac3.BacktrackingSearch(constraints);

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Reads content of files
     * @param file - name of file to read from
     * @return matrix containing contents of file
     * @throws IOException
     */
    private static int[][] readFile( String file) throws IOException {
        BufferedReader reader = new BufferedReader( new FileReader(file));
        String line = null;
        ArrayList<String> lines = new ArrayList<String>();

        //traverse through file until end
        while( ( line = reader.readLine() ) != null ) {
            lines.add(line);
        }

        //process contents of lines then return it
        return processFile(lines);
    }

    /**
     * Processes content of files
     * @param lines - string array list of lines from file
     * @return matrix containing contents of lines
     */
    private static int[][] processFile(ArrayList<String> lines) {

        //set number of columns
        int columns = lines.get(0).split("\\t").length;

        int[][] matrix = new int[lines.size()][columns];
        String[] line;

        //traverse through matrix and assign int value
        for (int i = 0; i < lines.size(); i++) {
            //get an array of values from the line
            line = lines.get(i).split("\\t");
            for (int j= 0; j < columns; j++) {
                //convert to int and assign to matrix
                matrix[i][j] = Integer.parseInt(line[j].trim());
            }
        }

        return matrix;
    }

}



