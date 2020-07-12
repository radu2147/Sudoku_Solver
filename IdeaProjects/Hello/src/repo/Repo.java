package repo;

import java.io.*;
import java.util.Vector;
import model.Sudoku;

interface AbstractRepo{
    Vector<Sudoku> get_all_sudokus();
}

public class Repo implements AbstractRepo {
    private final Vector<Sudoku> sudoku;
    private final Vector<Sudoku> sudoku_solved;
    private final String filename;


    public Vector<Sudoku> get_all_sudokus(){
        return sudoku;
    }
    public Vector<Sudoku> get_all_sudokus_solved(){
        return sudoku_solved;
    }
    public Repo(String filename) throws Exception{
        this.sudoku = new Vector<>();
        this.sudoku_solved = new Vector<>();
        this.filename = filename;
        load();
    }

    private void load() throws Exception{
        var fis = new BufferedReader(new FileReader(filename));
        while(true){
            var sud = fis.readLine();
            if(sud == null) break;
            int[][] sudoku_board = new int[9][9];
            int[][] sudoku_board_solved = new int[9][9];
            for(int i = 0; i < 81; i ++){
                sudoku_board[i/9][i%9] = sud.charAt(i) - 48;
                sudoku_board_solved[i/9][i%9] = sud.charAt(i) - 48;
            }

            sudoku.addElement(new Sudoku(sudoku_board));
            var su = new Sudoku(sudoku_board_solved);
            su.solve_sudoku();
            sudoku_solved.addElement(su);
        }

    }

}
