module com.example.art_kids_javafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.art_kids_javafx to javafx.fxml;
    exports com.example.art_kids_javafx;
}