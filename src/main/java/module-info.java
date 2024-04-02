module org.example.assignment_3_localized_db {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires sqlite.jdbc;


    opens org.example.assignment_3_localized_db to javafx.fxml;
    exports org.example.assignment_3_localized_db;
}