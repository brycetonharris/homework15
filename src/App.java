import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class App extends Application {

    private static final int SIZE = 5; // Board size
    private static final int TILE_SIZE = 100; // Size of each tile
    private String currentPlayer = "X"; // Current player
    private String[][] board = new String[SIZE][SIZE]; // Game board

    @Override
    public void start(Stage stage) {
        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: lightblue;");

        // Initialize the board and create the tiles
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = ""; // Empty initially
                gridPane.add(createTile(row, col), col, row);
            }
        }

        Scene scene = new Scene(gridPane, SIZE * TILE_SIZE, SIZE * TILE_SIZE);
        stage.setScene(scene);
        stage.setTitle("5x5 TicTacToe");
        stage.show();
    }

    private StackPane createTile(int row, int col) {
        Rectangle border = new Rectangle(TILE_SIZE, TILE_SIZE);
        border.setFill(Color.WHITE);
        border.setStroke(Color.BLACK);

        Text text = new Text();
        text.setFont(Font.font(36));

        StackPane tile = new StackPane(border, text);
        tile.setOnMouseClicked((MouseEvent event) -> handleMove(tile, text, row, col));
        return tile;
    }

    private void handleMove(StackPane tile, Text text, int row, int col) {
        // Check if the tile is already occupied
        if (!board[row][col].isEmpty()) {
            return;
        }

        // Update the board and the UI
        board[row][col] = currentPlayer;
        text.setText(currentPlayer);

        // Check if the current player wins
        if (checkWin(row, col)) {
            showWinner(currentPlayer);
            resetGame();
        } else {
            // Switch player
            currentPlayer = currentPlayer.equals("X") ? "O" : "X";
        }
    }

    private boolean checkWin(int row, int col) {
        String player = board[row][col];
        return checkDirection(row, col, 1, 0, player) || // Horizontal
                checkDirection(row, col, 0, 1, player) || // Vertical
                checkDirection(row, col, 1, 1, player) || // Diagonal \
                checkDirection(row, col, 1, -1, player);  // Diagonal /
    }

    private boolean checkDirection(int row, int col, int dRow, int dCol, String player) {
        int count = 1;
        count += countMatches(row, col, dRow, dCol, player);
        count += countMatches(row, col, -dRow, -dCol, player);
        return count >= 5;
    }

    private int countMatches(int row, int col, int dRow, int dCol, String player) {
        int count = 0;
        for (int i = 1; i < 5; i++) {
            int newRow = row + i * dRow;
            int newCol = col + i * dCol;

            if (newRow < 0 || newRow >= SIZE || newCol < 0 || newCol >= SIZE || !board[newRow][newCol].equals(player)) {
                break;
            }
            count++;
        }
        return count;
    }

    private void showWinner(String player) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("Player " + player + " wins!");
        alert.showAndWait();
    }

    private void resetGame() {
        board = new String[SIZE][SIZE];
        currentPlayer = "X";
        start(new Stage()); // Restart the game
    }

    public static void main(String[] args) {
        launch();
    }
}
