package Modules;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class CoinGameController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    TextField coinField = new TextField();
    @FXML
    TextArea textArea = new TextArea();
    @FXML
    TextArea bestTextArea = new TextArea();

    @FXML
    TextArea usedTextArea = new TextArea();
    @FXML
    Label usedLabel = new Label();
    @FXML
    Label expectedLabel = new Label();
    @FXML
    Label tableLabel = new Label();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bestTextArea.setVisible(false);
        usedTextArea.setVisible(false);
        textArea.setVisible(false);
        tableLabel.setVisible(false);
        usedLabel.setVisible(false);
        expectedLabel.setVisible(false);
    }

    public void begin() {
        try {


            String temp = coinField.getText();

            String[] tempArr = temp.split(" ");
            int[] arr = new int[tempArr.length];

            for (int i = 0; i < tempArr.length; i++) {
                arr[i] = Integer.parseInt(tempArr[i]);
            }
            CoinGame.usedCoins = new int[arr.length][2];




            bestTextArea.setVisible(true);
            expectedLabel.setVisible(true);
            bestTextArea.setText(String.valueOf(CoinGame.optimalStrategyOfGame(arr, arr.length)));

            tableLabel.setVisible(true);
            textArea.setVisible(true);
            textArea.setText(Arrays.deepToString(CoinGame.getTable())
                    .replace("],", "\n")
                    .replace(" [", "")
                    .replace("[[", "")
                    .replace("]]", ""));

            // String used = Driver.max + " " + Driver.min;
            usedLabel.setVisible(true);
            usedTextArea.setVisible(true);
            usedTextArea.setText(Arrays.deepToString(CoinGame.usedCoins)
                    .replace("],", "\n")
                    .replace(" [", "")
                    .replace("[[", "")
                    .replace("]]", ""));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);


            bestTextArea.setStyle("-fx-text-alignment: center");
            textArea.setStyle("-fx-text-alignment: center");
        }
    }

    public void exit() throws IOException {

        System.exit(1);
    }


}
