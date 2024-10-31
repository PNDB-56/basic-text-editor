module com.pndb {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.desktop;
    // opens com.pndb to javafx.fxml;
    exports com.pndb;
}
