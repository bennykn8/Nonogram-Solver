import java.util.ArrayList;
public class Nonogram
{
    /*board is the master 2D array for the nonogram (arranged rows by columns); 
    inverseBoard is board but arranged columns by rows (makes traversing by columns easier in solving algorithms)*/
    private String[][] board, inverseBoard;
    //stores clues for each row and column
    private ArrayList<Integer>[] rowClues, colClues;
    //stores integers of rows and columns that aren't complete
    private ArrayList<Integer> remainingRows, remainingCols;
    
    //constructor
    public Nonogram(int numR, int numC){
        board = new String[numR][numC];
        inverseBoard = new String[numC][numR];
        for(int r = 0; r < board.length; r++)
            for(int c = 0; c < board[r].length; c++)
                board[r][c] = " ";
        updateInverseBoard();
        
        colClues = new ArrayList[numC];
        remainingCols = new ArrayList<Integer>();
        for(int i = 0; i < numC; i++){
            colClues[i] = new ArrayList<Integer>();
            remainingCols.add(i);
        }
        rowClues = new ArrayList[numR];
        remainingRows = new ArrayList<Integer>();
        for(int i = 0; i < numR; i++){
            rowClues[i] = new ArrayList<Integer>();
            remainingRows.add(i);
        }
    }
    
    //ACCESSORS AND MODIFIERS
    //appends respective clue list with a new value
    public void addClue(String b, int l, int val){
        ArrayList<Integer>[] list = null;
        if(b.equals("c"))
            list = colClues;
        if(b.equals("r"))
            list = rowClues;
        list[l].add(val);
    }
    
    //iterates through remainingRows and remainingCols and removes a value if that line is complete
    public void updateRemainingLines(){
        boolean isComp;
        for(int i = remainingRows.size() - 1; i >= 0; i--){
            isComp = true;
            for(int j = 0; j < board[remainingRows.get(i)].length; j++) 
                //checks if line has an unverified index (contains " ")
                if(board[remainingRows.get(i)][j].equals(" ")){
                    isComp = false;
                    break;
                }
            if(isComp)
                remainingRows.remove(i);
        }
        
        for(int i = remainingCols.size() - 1; i >= 0; i--){
            isComp = true;
            for(int j = 0; j < inverseBoard[remainingCols.get(i)].length; j++)
                if(inverseBoard[remainingCols.get(i)][j].equals(" ")){
                    isComp = false;
                    break;
                }
            if(isComp)
                remainingCols.remove(i);
        }
    }
    //accessors to be used in runner
    public String[][] getBoard(){
      return board;
    }
    public ArrayList<Integer>[] getColClues(){
      return colClues;
    }
    public ArrayList<Integer>[] getRowClues(){
      return rowClues;
    }
    public ArrayList<Integer> getRemainingRows(){
        return remainingRows;
    }
    public ArrayList<Integer> getRemainingCols(){
        return remainingCols;
    }
    
    //updates the values in respective board
    private void updateBoard(){
        for(int c = 0; c < inverseBoard.length; c++)
            for(int r = 0; r < inverseBoard[0].length; r++)
                board[r][c] = inverseBoard[c][r];
    } 
    private void updateInverseBoard(){
        for(int r = 0; r < board.length; r++)
            for(int c = 0; c < board[0].length; c++)
                inverseBoard[c][r] = board[r][c];
    }
    
    //SOLVING ALGORITHMS
    //fills out undetermined spots of a line if logical
    public void fill(String b, int L){
        String[][] brd = null;
        ArrayList<Integer>[] clueList = null;
        //sets variables based on if we're working with rows or columns
        if(b.equals("C")){
            brd = inverseBoard;
            clueList = colClues;
        }    
        if(b.equals("R")){
            brd = board;
            clueList = rowClues;
        }
        String[] line = brd[L];
        ArrayList<Integer> clues = clueList[L]; 
        
        //optional: prints current values in the line we're working with
        System.out.print("Line " + b + (L+1) + " Current State: [");
        for(int i = 0; i < line.length - 1; i++)
            System.out.print(line[i] + ", "); 
        System.out.println(line[line.length-1] + "]");
        
        //initializing arrays and variables to be passed into recursive method
        //stores indexes that will be incremented while loops repeat
        int[] loopInds = new int[clues.size()];
        //stores indexes that will initialize the values in loopInds
        int[] loopStarts = new int[clues.size() - 1];
        /*stores the first index of a filled group of blocks according to its leftmost possible position
        ex: if clues = {1, 3, 2} in a line length of 10, then the leftmost position of line = {#, ,#,#,#, ,#,#, , } and baseArrInds would  = {0, 2, 6}*/
        int[] baseArrInds = new int[clues.size()];
        //fills in baseArrInds
        int minCap = 0;
        for(int i = 0; i < clues.size(); i++){
            baseArrInds[i] = minCap;
            minCap += clues.get(i);
            if(i < clues.size() - 1)
                minCap++;
        }
        //amount of "wiggle room" for the groups of filled blocks to shift around in a line
        int availSpaces = line.length - minCap;
        
        //will contain all possible combinations/sequences for the line after calling recursive method
        ArrayList<String[]> possSeqs = new ArrayList<String[]>();
        possSeqs = getPossibleSeqs(line, clues, availSpaces, baseArrInds, loopStarts, loopInds, possSeqs);
        
        //loops through possSeqs and tallies how many times each index is filled
        int[] occurs = new int[line.length];
        for(int i = 0; i < possSeqs.size(); i++)
            for(int j = 0; j < line.length; j++)
                if(possSeqs.get(i)[j].equals("#"))
                    occurs[j]++;
        
        /*loops through occurs and sets a filled block("#") to the line if it's filled in all possible sequences; 
        sets an empty block("_") if it's not filled in all possible sequences*/
        for(int i = 0; i < occurs.length; i++)
            if(line[i].equals(" ")){
                if(occurs[i] == possSeqs.size())
                    line[i] = "#";
                if(occurs[i] == 0)
                    line[i] = "_";
            }
            
        //updates the board that wasn't worked on    
        if(b.equals("C"))
            updateBoard();
        else
            updateInverseBoard();
        
        //optional: prints out sequences
        System.out.println("Possible Sequences:");
        for(String[] i : possSeqs){
            System.out.print("[");
            for(int j = 0; j < line.length - 1; j++)
                System.out.print(i[j] + ", ");
            System.out.println(i[line.length-1] + "]");
        }    
        
        //optional: prints out occurances of each filled block
        System.out.println("Filled Occurances per Index: ");
        for(int i = 0; i < occurs.length - 1; i++)
            System.out.print(occurs[i] + ", "); 
        System.out.println(occurs[occurs.length-1]);
            
        //optional: prints out finished line state
        System.out.print("Line " + b + (L+1) + " Updated State: [");
        for(int i = 0; i < line.length - 1; i++)
            System.out.print(line[i] + ", "); 
        System.out.println(line[line.length-1] + "]\n");
    }
    //recursive method that returns all possible sequences/combinations of the line
    private ArrayList<String[]> getPossibleSeqs(String[] line, ArrayList<Integer> clues, int availSpaces, int[] arrInds, int[] starts, int[] inds, ArrayList<String[]> out){
        //initializes a new combination
        String[] combo = new String[line.length];
        for(int a = 0; a < combo.length; a++)
            combo[a] = "_";
        //sets indexes to filled based on values of passed parameters
        for(int x = 0; x < clues.size(); x++)
            for(int y = 0; y < clues.get(x); y++)
                combo[arrInds[x] + inds[x] + y] = "#";
        
        //compares combination and line and only adds combination to output if it doesn't conflict with the current state of the line
        boolean valid = true;
        for(int b = 0; b < line.length; b++)
            if((line[b].equals("#") && combo[b].equals("_")) || (line[b].equals("_") && combo[b].equals("#"))){
                valid = false;
                break;
            }
        if(valid)
            out.add(combo);
        
        /*base/end case to terminate method;
        checks if all indexes in inds equals availSpaces*/
        boolean endCase = true;
        for(int q : inds)
            if(q != availSpaces){
                endCase = false;
                break;
            }
        if(endCase)
            return out;
        
        //looping algorithm that increments/modifies parameters and calls method again
        for(int i = 1; i <= clues.size(); i++){
            if(inds[inds.length - i] < availSpaces){
                inds[inds.length - i]++;
                return getPossibleSeqs(line, clues, availSpaces, arrInds, starts, inds, out);
            }
            else{
                starts[starts.length - i]++;
                inds[inds.length - i] = starts[starts.length - i];
                for(int j = starts.length - i; j < starts.length; j++){
                    starts[j] = starts[starts.length - i];
                    inds[j+1] = starts[starts.length - i];
                }
            }
        }
        return getPossibleSeqs(line, clues, availSpaces, arrInds, starts, inds, out);
    }
    //checks if nonogram puzzle is complete(has no more remaining rows or columns)
    public boolean isComplete(){
        if(remainingRows.size() == 0 && remainingCols.size() == 0)
            return true;
        return false;
    }
    
    //toString method
    public String toString(){
        String out = "";
        int largestColClue = 0, largestRowClue = 0;
        for(ArrayList<Integer> c : colClues)
            if(c.size() > largestColClue)
                largestColClue = c.size();
        for(ArrayList<Integer> r : rowClues)
            if(r.size() > largestRowClue)
                largestRowClue = r.size();
        for(int c = largestColClue; c > 0; c--){
            for(int i = 0; i < largestRowClue; i++)
                out += "   ";
            out += " ";
            for(int r = 0; r < board[0].length; r++){
                if(c > colClues[r].size())
                    out += "   "; 
                else if(colClues[r].get(colClues[r].size() - c) < 10)
                    out += colClues[r].get(colClues[r].size() - c) + "  ";
                else
                    out += colClues[r].get(colClues[r].size() - c) + " ";
            }
            out += "\n";
        }
        for(int r = 0; r < board.length; r++){
            for(int b = largestRowClue; b > 0; b--){
                if(b > rowClues[r].size())
                    out += "   ";
                else if(rowClues[r].get(rowClues[r].size() - b) < 10)
                    out += rowClues[r].get(rowClues[r].size() - b) + "  ";
                else
                    out += rowClues[r].get(rowClues[r].size() - b) + " ";
            }
            out += "[ ";
            for(int c = 0; c < board[r].length - 1; c++)
                out += board[r][c] + ", ";
            out += board[r][board[r].length - 1] + "]\n";
        }
        return out;
    }
}