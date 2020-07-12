package Service;

import model.Sudoku;
import repo.Repo;

interface Interface_Service{
    void hint();
    void reset_board();
    void update_board(int line, int column, int target) throws Exception;
    void new_game() throws Exception;
    Sudoku get_active_sudoku();
}

public class Service implements Interface_Service {
    private final Repo r;
    private Sudoku active_board;
    private Sudoku solved_board;
    private int game_index;

    public Service(Repo r){
        this.r = r;
        game_index = 0;
        active_board = r.get_all_sudokus().elementAt(game_index).get_sudoku_copy();
        solved_board = r.get_all_sudokus_solved().elementAt(game_index);
    }

    public void hint(){
        for(int i = 0; i < 9; i ++){
            for(int j = 0; j < 9; j ++){
                if(active_board.get_cell(i, j) == 0) {
                    try {
                        update_board(i, j, r.get_all_sudokus_solved().elementAt(game_index).get_cell(i, j));
                        return;
                    }
                    catch(Exception e){

                    }
                }
            }
        }
    }

    public Sudoku get_original_board(){
        return r.get_all_sudokus().elementAt(game_index);
    }

    public void reset_board(){
        active_board = r.get_all_sudokus().elementAt(game_index).get_sudoku_copy();
    }
    public void update_board(int line , int column, int target) throws Exception{
        if(target > 9 || target < 0 || line < 0 || line > 9 || column < 0 || column > 9) throw new Exception("Valoare incorecta");
        if(r.get_all_sudokus().elementAt(game_index).get_cell(line, column) != 0) throw new Exception("Nu se poate modifica");
        active_board.set_cell(line, column, target);
    }

    public void new_game() throws Exception{

        try {
            active_board = r.get_all_sudokus().elementAt(++game_index).get_sudoku_copy();
            solved_board = r.get_all_sudokus_solved().elementAt(game_index);
        }
        catch (Exception e){
            game_index--;
            throw new Exception("Nu mai exista jocuri disponibile");
        }
    }

    public Sudoku get_solved_board(){
        return solved_board;
    }

    public Sudoku get_active_sudoku(){
        return active_board;
    }
    public void solve_sudoku(){
        active_board = solved_board.get_sudoku_copy();
    }

    public void erase_choice(int line, int col) throws Exception{
        if(get_original_board().get_cell(line, col) != 0) throw new Exception("Not ok");
        active_board.set_cell(line, col, 0);
    }

    public void previous_game() throws Exception{
        try {
            active_board = r.get_all_sudokus().elementAt(--game_index).get_sudoku_copy();
        }
        catch (Exception e){
            game_index++;
            throw new Exception("Acesta este primul joc");
        }
    }
}
