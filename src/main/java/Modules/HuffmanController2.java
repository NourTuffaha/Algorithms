package Modules;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class HuffmanController2 {
    Stage primaryStage;
    @FXML
    TextField inputFileLocation = new TextField();
    @FXML

    TextField saveFileLocation = new TextField();

    public void browseForInputFile() {
        // Show the file chooser and update the input file field with the selected file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Input File");
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            inputFileLocation.setText(file.getPath());
        }
    }
    public void browseForOutputFile() {
        // Show the file chooser and update the output file field with the selected file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Output File");
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            saveFileLocation.setText(file.getPath());
        }
    }

    public void exit() throws IOException {

        System.exit(1);
    }
}
