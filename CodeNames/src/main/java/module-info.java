module cz.cvut.fel.pjv.codenames {
    requires javafx.controls;
    requires javafx.fxml;


    opens cz.cvut.fel.pjv.codenames to javafx.fxml;
    exports cz.cvut.fel.pjv.codenames.GUI;
}