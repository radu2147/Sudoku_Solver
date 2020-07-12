package UI;

import Service.Service;

import java.util.Scanner;

public class UI {
    private final Service s;
    public UI(Service s){
        this.s = s;
    }

    private void ui_hint(){
        try{
            s.hint();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    private void ui_solve(){
        try{
            s.solve_sudoku();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    private void ui_reset(){
        s.reset_board();
    }

    public void run(){
        String com = new String();
        while(true){
            System.out.println("-----------------");
            s.get_active_sudoku().show();
            System.out.println("-----------------");
            System.out.print("Introduceti comanda va rog: ");
            Scanner reader = new Scanner(System.in);
            com = reader.nextLine();
            var args = com.split(" ");
            if(com.equals("exit")) break;
            switch(args[0]){
                case "hint":
                    ui_hint();
                    break;
                case "reset":
                    ui_reset();
                    break;
                case "solve":
                    ui_solve();
                    break;
                case "new":
                    ui_new();
                    break;
                case "prev":
                    ui_prev();
                    break;
                case "move":
                    if(args.length != 4) System.out.println("Numar incorect de argumente");
                    else ui_move(args[1].charAt(0) - 49, args[2].charAt(0) - 49, args[3].charAt(0) - 48);
                    break;
                default:
                    System.out.println("Comanda inexistenta");
            }
        }
    }

    private void ui_prev() {
        try{
            s.previous_game();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    private void ui_move(int line, int column, int value) {
        try{
            s.update_board(line, column, value);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    private void ui_new() {
        try{
            s.new_game();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
