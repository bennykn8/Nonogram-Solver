import java.util.Scanner;
import java.util.ArrayList;
public class NonogramRunner
{
    public static void main(String[]args){
        Scanner kb = new Scanner(System.in);
        Nonogram puzzle = null;
        
        System.out.println("Nonogram Solver by Benny N.");
        System.out.print("Would you like to use a preset?(y/n) ");
        String presetChoice = kb.next();
        if(presetChoice.equals("y")){
            int[][] preCol = null;
            int[][] preRow = null;
            System.out.println("Select preset:\n1) small(5x5)\n2) medium(10x10)\n3) largeA(15x15)\n4) largeB(15x15)");
            int sizeSelect = kb.nextInt();
            switch(sizeSelect){
                case 1:
                    puzzle = new Nonogram(5, 5);
                    preCol = new int[][]{{2},{4},{3, 1},{4},{2}};
                    preRow = new int[][]{{1, 1, 1},{5},{3},{1, 1},{3}};
                    break;
                case 2:
                    puzzle = new Nonogram(10, 10);
                    preCol = new int[][]{{3},{2, 1},{1, 1, 5},{1, 8},{2, 6},{3, 4},{2, 6},{1, 8},{1, 1, 5},{2, 3}};
                    preRow = new int[][]{{2, 2},{1, 1, 1, 1},{1, 5, 1},{2, 1, 2},{5},{3, 3},{1, 8},{1, 8},{1, 8},{8}};
                    break;
                case 3:
                    puzzle = new Nonogram(15, 15);
                    preCol = new int[][]{{3, 3},{2, 2, 2, 1},{1, 4, 4},{3, 1, 1, 2},{1, 1, 1, 2, 1},{2, 5, 2},{1, 2, 3, 4},{2, 5, 1},{2, 1, 2, 1},{1, 1, 1, 3},{1, 4, 5},{2, 2, 3, 1},{3, 2, 2},{1, 4},{4}};
                    preRow = new int[][]{{2, 2},{1, 5},{3, 1, 2},{2, 3, 1},{1, 2, 1, 2},{3, 1, 3},{3, 1, 1},{1, 3, 1},{9, 1},{2, 2, 2, 1},{3, 1, 1, 5},{1, 1, 3, 6},{3, 1, 3, 2},{5, 3, 2},{3, 3}};
                    break;
                case 4:
                    puzzle = new Nonogram(15, 15);
                    preCol = new int[][]{{2, 2, 3},{2, 1, 2, 1},{3, 5, 3},{1, 5, 3},{3, 7, 1},{3, 7},{1, 1, 5},{2, 4},{2, 5, 2},{3, 6, 3},{13},{2, 1, 2},{3},{3, 3},{1, 1, 1}};
                    preRow = new int[][]{{1, 2, 2, 1},{5, 3},{1, 2, 2},{2},{2, 4, 2},{6, 4},{5, 3, 2},{14},{9, 2},{11},{2, 4, 3},{2, 1},{1, 2, 2, 2},{5, 3, 1},{1, 2, 2}};
                    break;
                default:
                    System.out.println("Invalid Preset Size Choice");
            }
            for(int a = 0; a < preCol.length; a++)
                for(int b = 0; b < preCol[a].length; b++)
                    puzzle.addClue("c", a, preCol[a][b]);
            for(int a = 0; a < preRow.length; a++)
                for(int b = 0; b < preRow[a].length; b++)
                    puzzle.addClue("r", a, preRow[a][b]);
        }
        else{
            System.out.print("How many rows? ");
            int numRows = kb.nextInt();
            System.out.print("How many columns? ");
            int numCols = kb.nextInt();
            puzzle = new Nonogram(numRows, numCols);
            System.out.println("\n" + puzzle);
        
            for(int c = 0; c < numCols; c++){
                System.out.println("Clue Column " + c);
                boolean nextCol = false;
                while(nextCol == false){
                    System.out.print("Enter number(top to bottom): ");
                    puzzle.addClue("c", c, kb.nextInt());
                    System.out.println(puzzle.getColClues()[c]);
                    System.out.println("Add another clue to this column?(y, n) ");
                    if(kb.next().equals("n"))
                        nextCol = true;
                }
            }
            System.out.println("\n" + puzzle);
            for(int r = 0; r < numRows; r++){
                System.out.println("Clue Row " + r);
                boolean nextRow = false;
                while(nextRow == false){
                    System.out.print("Enter number(left to right): ");
                    puzzle.addClue("r", r, kb.nextInt());
                    System.out.println(puzzle.getRowClues()[r]);
                    System.out.println("Add another clue to this row?(y, n) ");
                    if(kb.next().equals("n"))
                        nextRow = true;
                }
            }
        }
        System.out.println("\n" + puzzle);
        int reps = 1;
        while(!puzzle.isComplete()){
            System.out.println("Filling Rows (Repetition " + reps + ")");
            for(int r = 0; r < puzzle.getRemainingRows().size(); r++)
                puzzle.fill("R", puzzle.getRemainingRows().get(r));
            puzzle.updateRemainingLines();    
            System.out.println("\n" + puzzle);
            if(!puzzle.isComplete()){
                System.out.println("Filling Columns (Repetition " + reps + ")");
                for(int c = 0; c < puzzle.getRemainingCols().size(); c++)
                    puzzle.fill("C", puzzle.getRemainingCols().get(c));
                puzzle.updateRemainingLines();
                System.out.println("\n" + puzzle);
            }
            reps++;
        }
        System.out.println("\n" + puzzle);
    }
}