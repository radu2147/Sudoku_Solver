package UI;
import Service.Service;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


class Cell extends Rectangle{
    private final int row;
    private final int col;

    public Cell(int v, int v1, int i, int j){
        super(v, v1);
        row = i;
        col = j;

    }
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
}

public class GUI {

    private final Service s;
    private final Stage window;
    private Cell cell;
    private CheckBox check_for_mistakes;

    public GUI(Service s, Stage primayStage){
        this.s = s;
        this.window = primayStage;
        check_for_mistakes = new CheckBox("Check for mistakes");
        check_for_mistakes.setSelected(true);
        initGUI();
    }
    private void reload(GridPane grid){
        grid.getChildren().clear();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                Cell tile = new Cell(45, 45, i, j);
                tile.setFill(Color.WHITE);
                tile.setStroke(Color.BLACK);
                Text t;
                if(s.get_active_sudoku().get_cell(i, j) == 0)
                    t = new Text();
                else {
                    t = new Text(String.valueOf(s.get_active_sudoku().get_cell(i, j)));
                }
                t.setFont(Font.font(27));
                if(s.get_active_sudoku().get_cell(i, j) != s.get_solved_board().get_cell(i, j) && check_for_mistakes.isSelected())
                    t.setFill(Color.RED);
                else if(s.get_original_board().get_cell(i, j) == 0 && s.get_active_sudoku().get_cell(i, j) != 0)
                    t.setFill(Color.BLUE);
                else
                    t.setFill(Color.BLACK);
                tile.setOnMouseClicked(e -> {
                    if(cell != null)
                        cell.setFill(Color.WHITE);
                    cell = tile;
                    cell.setFill(Color.BLUEVIOLET);
                });
                t.setOnMouseClicked(e -> {
                    if(cell != null)
                        cell.setFill(Color.WHITE);
                    cell = tile;
                    cell.setFill(Color.BLUEVIOLET);
                });
                grid.add(new StackPane(tile, t), j, i);

            }
        }
    }

    private VBox initButtons(GridPane grid){
        VBox butons =  new VBox();
        var digits = initDigits(grid);
        butons.setSpacing(9);
        butons.setStyle("-fx-padding: 0 20 0 0");

        Button new_game = new Button("New Game");
        Button hint = new Button("Hint");
        Button erase = new Button("Erase");
        Button solve = new Button("Solve");
        Button reset = new Button("Reset");

        new_game.setFont(Font.font("verdana", 25.9));
        hint.setFont(Font.font("verdana", 23.4));
        erase.setFont(Font.font("verdana", 23.4));
        solve.setFont(Font.font("verdana", 21.5));
        reset.setFont(Font.font("verdana", 21.5));

        butons.getChildren().addAll(new_game);
        butons.getChildren().addAll(digits);

        HBox last = new HBox();
        last.getChildren().addAll(hint, erase);


        HBox solve_reset_layout = new HBox();
        solve_reset_layout.getChildren().addAll(solve, reset);

        butons.getChildren().addAll(last);
        butons.getChildren().addAll(solve_reset_layout);
        butons.getChildren().addAll(check_for_mistakes);

        new_game.setOnAction(e -> {
            try{
                s.new_game();
                reload(grid);
            }
            catch(Exception ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(ex.toString());
                alert.show();

            }
        });
        solve.setOnAction(hn -> {
            try{
                s.solve_sudoku();
                reload(grid);
            }
            catch(Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(e.toString());
                alert.show();
            }
        });
        reset.setOnAction(hn -> {
            try{
                s.reset_board();
                reload(grid);
            }
            catch(Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(e.toString());
                alert.show();
            }
        });
        hint.setOnAction(e -> {
            try{
                s.hint();
                reload(grid);
            }
            catch(Exception ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(ex.toString());
                alert.show();
            }
        });
        erase.setOnAction(h -> {
            try{
                s.erase_choice(cell.getRow(), cell.getCol());
                reload(grid);
            }
            catch(Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(e.toString());
                alert.show();
            }
        });
        check_for_mistakes.setOnAction(hn -> reload(grid));
        return butons;
    }

    private Pane initDigits(GridPane grid){
        GridPane buttons_lay = new GridPane();
        int label = 0;
        for(int i = 0 ; i < 3; i ++){
            for(int j = 0; j < 3; j ++){
                var buttons = new Button(String.valueOf(++label));
                buttons.setFont(Font.font("verdana", 30));
                buttons.setOnAction(e ->{
                    var row = cell.getRow();
                    var col = cell.getCol();
                    if(s.get_original_board().get_cell(row, col) == 0) {
                        try {
                            s.update_board(row, col, Integer.parseInt(buttons.getText()));
                            reload(grid);
                        }
                        catch (Exception exp){
                            System.out.println(3);
                        }
                    }
                });
                buttons_lay.add(buttons, j, i);
            }
        }
        return buttons_lay;
    }

    private void niceText(BorderPane mainly){

        Text intro = new Text("Sudoku");
        intro.setFont(Font.font("Arial", 30));
        intro.setFill(Color.BLUE);
        intro.setTextAlignment(TextAlignment.CENTER);
        mainly.setTop(intro);
    }

    private void initGUI() {
        GridPane grid = new GridPane();
        BorderPane mainly = new BorderPane();


        reload(grid);
        var buttons_lay = initButtons(grid);
        grid.setAlignment(Pos.CENTER);

        buttons_lay.setAlignment(Pos.CENTER);

        mainly.setCenter(grid);
        mainly.setRight(buttons_lay);
        mainly.setBottom(new Text("© Radu Baston"));
        niceText(mainly);

        window.setScene(new Scene(mainly, 680, 580));
        window.setTitle("Sudoku");
        window.show();
    }
}
