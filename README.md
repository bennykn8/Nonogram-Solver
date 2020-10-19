# Nonogram-Solver

A recursive approach to solve nonogram puzzles given the clues.

About nonograms:
The board starts out empty, filled with undetermined/unverified blocks (" ")
In a nonogram puzzle, numeric clues are given on each row and column.
In each respective row or column, the set of clues represents the groupings of the filled blocks ("#") in the row/col
Each grouping must be separated by at least one crossed-out block ("_") 

For example, a size 10 row with the clues 3, 1, 2 could look like {#,#,#,_,_,#,_,#,#,_} or {_,#,#,#,_,#,_,_,#,#} and etc.
The challenge of the puzzle of to validly fill the each row/column without making a move that would be inconsistant with the clues in other rows/columns


About the program:
User Input:
The program will prompt you the either use and select a preset puzzle or manually enter in the specifications and clues for a puzzle

Algorithm:
The recursive method compiles a complete list of all of the possible combinations of the respective row/column
After a loop runs through the list and tallies how many times a filled block is present at each index.
If the index has 0 possible occurances, then there must be a crossed-out block there ("_") 
If the index has a filled block in every occurance, then there must be a filled block there("#")

This process is repeated for all rows and columns until the puzzle is solved and left without an undetermined block.
