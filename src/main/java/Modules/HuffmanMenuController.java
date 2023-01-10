package Modules;

import Modules.Huffman.Compression;
import Modules.Huffman.Decompression;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class HuffmanMenuController implements Initializable {

    /**
     * This list will store all objects of row type to fill tableView
     */

    private Stage stage;
    private Scene scene;
    private Parent root;
    private ObservableList<Row> listView;

    @FXML
    private RadioButton encodingRadioButton;

    @FXML
    private RadioButton decodingRadioButton;

    @FXML
    private TextField sourceFilePath;

    @FXML
    private TextField destinationFilePath;

    @FXML
    private Label sourceFileInformation;


    @FXML
    private TableView<Row> huffmanTable;

    @FXML
    private TableColumn<Row, Character> characterColumn;

    @FXML
    private TableColumn<Row, String> huffmanCodeColumn;

    @FXML
    private TableColumn<Row, Integer> lengthColumn;

    @FXML
    private TableColumn<Row, Integer> frequencyColumn;

    @FXML
    private TextArea encodedOfHeader;

    @FXML
    private TextArea numberOfCharacterInOriginalFile;

    @FXML
    private TextArea extensionOfOriginalFile;

    private File source;

    private File destination;


    private Compression compression;

    private Decompression decompressTools;


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method will be used to initialize and setup some data fields
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /*
         * Adding radio Buttons to One Group , so You can choose one radio button at a
         * time
         */
//        encodingRadioButton.setToggleGroup(radioButtons);
//        decodingRadioButton.setToggleGroup(radioButtons);

        // Set Columns and add them to tableView
        characterColumn.setCellValueFactory(new PropertyValueFactory<Row, Character>("character"));
        huffmanCodeColumn.setCellValueFactory(new PropertyValueFactory<Row, String>("huffmanCode"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<Row, Integer>("length"));
        frequencyColumn.setCellValueFactory(new PropertyValueFactory<Row, Integer>("frequency"));

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method will be used to choose a file to be decompressed
     */
    @FXML
    void browseDestinationFile(ActionEvent event) {

        try {

            FileChooser chose = new FileChooser();
            destination = chose.showOpenDialog(null);
            destinationFilePath.setText(destination.getAbsolutePath());

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e.getMessage());

        }

    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method will be used to choose a file to be compressed
     */

    @FXML
    void browseSourceFile(ActionEvent event) {

        try {

            FileChooser chose = new FileChooser();
            source = chose.showOpenDialog(null);
            sourceFilePath.setText(source.getAbsolutePath());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());

        }

    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method will compress or decompress a choosen file , Depends of radio
     * button that selected
     */


    @FXML
    void compress(ActionEvent event) {
        encode();

    }

    @FXML
    void decompress(ActionEvent event) {
        decode();

    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // This method will close
    @FXML
    void cancel(ActionEvent event) {

        System.exit(1);

    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // This method will clear all input data
    @FXML
    void clearEncodeMenu(ActionEvent event) {

        this.source = null;
        this.sourceFileInformation.setText("");
        this.sourceFilePath.clear();
        this.listView.clear();
    }

    @FXML
    void clearDecodeMenu(ActionEvent event) {

        this.destination = null;
        this.destinationFilePath.clear();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // This method will invoke to compress chosen source File
    private void encode() {

        if (source != null) {


            String fullPath = source.getParent() + "\\" + source.getName();
            int dotIndex = fullPath.lastIndexOf('.');
            String shortPath = fullPath.substring(0, dotIndex);
            File dest = new File(shortPath + ".rar");

            // Start The compression Process
            compression = new Compression();
            compression.compress(source, dest);

//             printSourceInformation();

            displayEncodingInfo();

            listView = FXCollections.observableArrayList(compression.getRows());
            huffmanTable.setItems(listView);


            JOptionPane.showMessageDialog(null, "Finished Encoding");

        } else

            JOptionPane.showMessageDialog(null, "Choose Source File Please");


    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void decode() {

        if (destination != null) {


            decompressTools = new Decompression(destination);

            /*
             Save decoded file in the same directory but w/ original extension
             */
            String pathWithExtension = destination.getParent() + "\\" + destination.getName();
            int indexOfDotExtension = pathWithExtension.lastIndexOf('.');
            String pathWithOutExtension = pathWithExtension.substring(0, indexOfDotExtension);
            File initial = new File(pathWithOutExtension + decompressTools.getExtensionOfOriginalFile());

            // In case a file with the same name+extension exists =>
            if (initial.exists()) {
                initial = new File(pathWithOutExtension + "2" + decompressTools.getExtensionOfOriginalFile());
            }

            // Start The Decompression Process
            decompressTools.decompres(initial);

//            printDestinationInformation();

            displayDecodingInfo();

            listView = FXCollections.observableArrayList(decompressTools.getRows());
            huffmanTable.setItems(listView);

            JOptionPane.showMessageDialog(null, "Decoding Finished");


        } else {
            JOptionPane.showMessageDialog(null, "Choose Destination File Please");


        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void displayEncodingInfo() {

        this.numberOfCharacterInOriginalFile.setText(compression.getCountNumberOfCharInOriginalFile() + "");
        this.extensionOfOriginalFile.setText(compression.getExtension());
        this.encodedOfHeader.setText(compression.getEncodedOfHeader().toString());

    }

    private void displayDecodingInfo() {

        this.numberOfCharacterInOriginalFile.setText(decompressTools.getNumberOfCharInOriginalFile() + "");
        this.extensionOfOriginalFile.setText(decompressTools.getExtensionOfOriginalFile());
        this.encodedOfHeader.setText(decompressTools.getEncodedOfHeader().toString());

    }

    public void switchBackToMainMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainMenu/MainMenu.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This Method will print source file information on GUI
     */
//    private void printSourceInformation() {
//
//        // Fill Information about source file in GUI
//        String sourceInformation = "File Length :\t" + compression.getCountNumberOfCharInOriginalFile() + "\n\n"
//                + "# of Distinguished Characters:  " + compression.getCountDistinct();
//        this.sourceFileInformation.setText(sourceInformation);
//
//        // Fill Information about Destination/compressed file in GUI
//        int NumberOfCharInHeader = compression.getCountNumberOfCharInHeader() / 8;
//        if (NumberOfCharInHeader % 8 > 0) {
//            NumberOfCharInHeader++;
//        }
//        int NumberOfCharInEncodedMessage = compression.getNumberOfCharInEncodedMessage();
//
//        float ratioOfCompression = ((1 - (float) (NumberOfCharInHeader + NumberOfCharInEncodedMessage)
//                / compression.getCountNumberOfCharInOriginalFile()) * 100);
//
//        ratioOfCompression = (int) (ratioOfCompression * 100) / 100f;
//
//        String DestinationInformation = "File Head Length :\t" + NumberOfCharInHeader + "\n\n" + "Actual Data length:\t"
//                + NumberOfCharInEncodedMessage + "\n\nCompression Rate:\t\t" + ratioOfCompression + " %";
//
//        this.codedFileInformation.setText(DestinationInformation);
//    }
//
//    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    /**
//     * This Method will print destination file information on GUI
//     */
//    private void printDestinationInformation() {
//
//        // Fill Information about source file in GUI
//        String sourceInformation = "File Length :\t" + decompressTools.getNumberOfCharInOriginalFile() + "\n\n"
//                + "# of Distinguished Characters:  " + decompressTools.getCountDistinctInOriginal();
//        this.sourceFileInformation.setText(sourceInformation);
//
//        // Fill Information about Destination/compressed file in GUI
//        int NumberOfCharInHeader = decompressTools.getCountHeader() / 8;
//        if (NumberOfCharInHeader % 8 > 0) {
//            NumberOfCharInHeader++;
//        }
//        int NumberOfCharInEncodedMessage = decompressTools.getCountCharInCompressedFile();
//
//        float ratioOfCompression = ((1 - (float) (NumberOfCharInHeader + NumberOfCharInEncodedMessage)
//                / decompressTools.getNumberOfCharInOriginalFile()) * 100);
//
//        ratioOfCompression = (int) (ratioOfCompression * 100) / 100f;
//
//        String DestinationInformation = "File Head Length :\t" + NumberOfCharInHeader + "\n\n" + "Actual Data length:\t"
//                + NumberOfCharInEncodedMessage + "\n\nCompression Rate:\t\t" + ratioOfCompression + " %";
//
//        this.codedFileInformation.setText(DestinationInformation);
//
//    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
