module org.example.demobanker {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens main to javafx.fxml;
    opens controller to javafx.fxml;
    exports main;
}