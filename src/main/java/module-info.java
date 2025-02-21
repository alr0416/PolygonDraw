module ressler.polygondraw {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens ressler.polygondraw to javafx.fxml;
    exports ressler.polygondraw;
}