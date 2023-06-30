module community.sample {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.media;
    requires com.google.gson;
    
    exports org.example.view to javafx.graphics;
    exports org.example.model to com.google.gson;
    
    opens org.example.view to javafx.fxml;
}