module Titanic {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires org.apache.commons.lang3;

    opens titanic to javafx.fxml;
    exports titanic;
    exports titanic.enums;
    opens titanic.enums to javafx.fxml;
}