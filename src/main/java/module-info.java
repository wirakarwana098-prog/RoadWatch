module com.mycompany.roadwatch {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.roadwatch to javafx.fxml;
    exports com.mycompany.roadwatch;
}
