module com.example.p1aloritmos {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.p1aloritmos to javafx.fxml;
    exports com.example.p1aloritmos;
}