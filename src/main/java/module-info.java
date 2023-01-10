module modules.algorithms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens Modules to javafx.fxml;
    exports Modules;
}