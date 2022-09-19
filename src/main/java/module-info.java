module fr.kyo.crkf {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;

    opens fr.kyo.crkf to javafx.fxml;
    exports fr.kyo.crkf;

    opens fr.kyo.crkf.Entity to javafx.fxml;
    exports fr.kyo.crkf.Entity;

    opens fr.kyo.crkf.controller to javafx.fxml;
    exports fr.kyo.crkf.controller;
    exports fr.kyo.crkf.controller.instrument;
    opens fr.kyo.crkf.controller.instrument to javafx.fxml;
    exports fr.kyo.crkf.controller.ecole;
    opens fr.kyo.crkf.controller.ecole to javafx.fxml;
    exports fr.kyo.crkf.controller.departement;
    opens fr.kyo.crkf.controller.departement to javafx.fxml;
    exports fr.kyo.crkf.controller.ville;
    opens fr.kyo.crkf.controller.ville to javafx.fxml;


    requires com.jfoenix;
}