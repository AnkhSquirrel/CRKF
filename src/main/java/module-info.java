module fr.kyo.crkf {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;

    opens fr.kyo.crkf to javafx.fxml;
    exports fr.kyo.crkf;
    exports fr.kyo.crkf.dao;
    opens fr.kyo.crkf.dao to javafx.fxml;
}