package model;

import java.util.Vector;
import java.util.Stack;

public class Sudoku {
    private int[][] sud;

    boolean is_solved;
    boolean is_incorrect;

    public Sudoku(int[][] wid){
        this.sud = wid;
        is_solved = false;
        is_incorrect = false;
    }

    public void set_cell(int line, int column, int target) throws Exception{
        this.sud[line][column] = target;
    }

    public int get_cell(int line, int column){
        return sud[line][column];
    }

    public Sudoku get_sudoku_copy(){
        var copy = new int[9][9];
        for(int i = 0; i < 9; i ++){
            for(int j = 0; j < 9; j ++){
                copy[i][j] = sud[i][j];
            }
        }
        return new Sudoku(copy);
    }
    public void show(){
        for(final var array : sud) {
            for (final var array_element : array) {
                System.out.print(array_element);
                System.out.print(" ");
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }
    private boolean check_line(final int line, final int target_value){
        for(int j = 0; j < 9; j ++){
            if(sud[line][j] == target_value)
                return false;
        }
        return true;
    }
    private boolean check_column(final int column, final int target_value){
        for(int j = 0; j < 9; j ++){
            if(sud[j][column] == target_value)
                return false;
        }
        return true;
    }
    private boolean check_box(final int line, final int column, final int target_value){
        for(int i = line / 3 * 3; i < line / 3 * 3 + 3; i ++) {
            for(int j = column / 3 * 3; j < column / 3 * 3 + 3; j ++){
                if(sud[i][j] == target_value)
                    return false;
            }
        }
        return true;
    }
    private Vector<Integer> missing_values_on_line(final int line){
        var values = new Vector<Integer>();
        for(int i = 0;i < 9; i ++) values.add(i + 1);
        for(int i = 0;i < 9; i ++){
            if(sud[line][i] != 0)
                values.remove((Integer) sud[line][i]);
        }
        return values;
    }
    private Vector<Integer> missing_values_on_column(final int line){
        var values = new Vector<Integer>();
        for(int i = 0;i < 9; i ++) values.add(i + 1);
        for(int i = 0;i < 9; i ++){
            if(sud[i][line] != 0)
                values.remove((Integer) sud[i][line]);
        }
        return values;
    }
    private boolean solve_lines() throws Exception{
        boolean ok = true;
        boolean return_value = false;
        is_solved = true;
        while(ok) {
            ok = false;
            for (int i = 0; i < 9; i++) {
                var cv = missing_values_on_line(i);
                if (cv.size() > 0) {
                    for (int k = 0; k < cv.size(); k++) {
                        int spot = -1;
                        for (int j = 0; j < 9; j++) {
                            if (sud[i][j] == 0) {
                                is_solved = false;
                                if(check_column(j, cv.elementAt(k)) && check_box(i, j, cv.elementAt(k)))
                                    spot = spot == -1 ? j : -2;

                            }
                        }
                        if (spot > -1) {
                            sud[i][spot] = cv.elementAt(k);
                            ok = true;
                            return_value = true;
                        }
                        else if(spot == -1){
                            throw new Exception("Sudoku invalid");
                        }
                    }
                }
            }
        }
        return return_value;
    }
    private boolean solve_columns() throws Exception{
        boolean ok = true;
        boolean return_value = false;
        is_solved = true;
        while(ok) {
            ok = false;
            for (int i = 0; i < 9; i++) {
                var cv = missing_values_on_column(i);
                if (cv.size() > 0) {
                    for (int k = 0; k < cv.size(); k++) {
                        int spot = -1;
                        for (int j = 0; j < 9; j++) {
                            if (sud[j][i] == 0){
                                is_solved = false;
                                if(check_line(j, cv.elementAt(k)) && check_box(j, i, cv.elementAt(k)))
                                    spot = spot == -1 ? j : -2;
                            }
                        }
                        if (spot > -1) {
                            sud[spot][i] = cv.elementAt(k);
                            ok = true;
                            return_value = true;
                        }
                        else if(spot == -1){
                            throw new Exception("Invalid");
                        }
                    }
                }
            }
        }
        return return_value;
    }
    private void solve_AI() throws Exception{
        while(solve_lines() || solve_columns());
    }
    private int[][] find_value_with_2_options_on_column(){
        int[][] returned_values = new int[2][3];
        for(int i = 0; i < 9; i ++){
            var missing_values = missing_values_on_column(i);
            if(missing_values.size() > 0){
                for(int k = 0; k < missing_values.size(); k ++){
                    int counter = 0;
                    int[] js = new int[2];
                    for(int j = 0; j < 9; j ++){
                        if(sud[j][i] == 0 && check_line(j, missing_values.elementAt(k)) && check_box(j, i, missing_values.elementAt(k))){
                            if(counter > 1) {
                                counter++;
                                break;
                            }
                            else
                                js[counter++] = j;
                        }
                    }
                    if(counter == 2){
                        returned_values[0][1] = i;
                        returned_values[1][1] = i;
                        returned_values[1][0] = js[1];
                        returned_values[0][0] = js[0];
                        returned_values[1][2] = missing_values.elementAt(k);
                        returned_values[0][2] = missing_values.elementAt(k);
                        return returned_values;
                    }
                }
            }
        }

        return returned_values;
    }
    private int[][] find_value_with_2_options_on_line(){
        int[][] returned_values = new int[2][3];
        for(int i = 0;i < 9; i ++){
            var missing_values = missing_values_on_line(i);
            if(missing_values.size() > 0){
                for(int k = 0; k < missing_values.size(); k ++){
                    int counter = 0;
                    int[] js = new int[2];
                    for(int j = 0; j < 9; j ++){
                        if(sud[i][j] == 0 && check_column(j, missing_values.elementAt(k)) && check_box(i, j, missing_values.elementAt(k))){
                            if(counter > 1) {
                                counter++;
                                break;
                            }
                            else
                                js[counter++] = j;
                        }
                    }
                    if(counter == 2){
                        returned_values[0][0] = i;
                        returned_values[1][0] = i;
                        returned_values[1][1] = js[1];
                        returned_values[0][1] = js[0];
                        returned_values[1][2] = missing_values.elementAt(k);
                        returned_values[0][2] = missing_values.elementAt(k);
                        return returned_values;
                    }
                }
            }
        }

        return returned_values;
    }

    public boolean is_solved(){
        return is_solved;
    }
    private int[][] find_value_with_2_options(){
        var cv = find_value_with_2_options_on_line();
        if(cv[0][2] == 0){
            return find_value_with_2_options_on_column();
        }
        return cv;
    }

    private void solve_by_guessing(){
        Stack<int[][]> stack_of_sudoku = new Stack<>();
        boolean popped = false;
        while(!is_solved()){
            if(!popped) {
                var copy_of_sudoku = Sudoku.make_copy(sud);
                var guess_options = find_value_with_2_options();
                if(guess_options[0][2] == 0) break;
                copy_of_sudoku[guess_options[0][0]][guess_options[0][1]] = guess_options[0][2];
                stack_of_sudoku.push(copy_of_sudoku);
                sud[guess_options[1][0]][guess_options[1][1]] = guess_options[1][2];
            }
            try{
                solve_AI();
                popped = false;
            }
            catch (Exception e){
                sud = stack_of_sudoku.lastElement();
                stack_of_sudoku.pop();
                popped = true;
            }

        }

    }

    private void solve_backtracking(int[][] copy, int i, int j){
        if(is_solved) return;
        if(i > 8){
            sud = Sudoku.make_copy(copy);
            is_solved = true;
            return;
        }
        if(copy[i][j] != 0){
            if(j < 8)
                solve_backtracking(copy, i, j + 1);
            else
                solve_backtracking(copy, i + 1, 0);
        }
        else {
            var vec = missing_values_on_line(i);
            for (final var el : vec) {
                if (check_box(i, j, el) && check_column(j, el) && check_line(i, el) && !is_solved) {

                    copy[i][j] = el;
                    if(j < 8)
                        solve_backtracking(copy, i, j + 1);
                    else
                        solve_backtracking(copy, i + 1, 0);
                }
            }
            copy[i][j] = 0;
        }
    }
    private static int[][] make_copy(int[][] arr){
        int[][] copy = new int[arr.length][arr[0].length];
        for(int i = 0; i < 9; i ++)
            System.arraycopy(arr[i], 0, copy[i], 0, arr[i].length);
        return copy;
    }
    public void solve_sudoku(){
        try{
            solve_AI();
        }
        catch(Exception e){
            is_incorrect = true;
        }

        solve_by_guessing();
        solve_backtracking(sud, 0,0);
    }
}
