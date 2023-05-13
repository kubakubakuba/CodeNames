module cz.cvut.fel.pjv.codenames {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens cz.cvut.fel.pjv.codenames to javafx.fxml;
    exports cz.cvut.fel.pjv.codenames.GUI;
    exports cz.cvut.fel.pjv.codenames.server to javafx.graphics;
}