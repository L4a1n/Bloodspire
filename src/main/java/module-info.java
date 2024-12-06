module com.l4a1n.bloodspire {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens com.l4a1n.bloodspire to javafx.fxml;
    exports com.l4a1n.bloodspire;
}