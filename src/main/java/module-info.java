module com.artkids {
    requires java.net.http;
    requires javafx.controls;
    requires javafx.fxml;

    exports com.artkids;
    exports com.artkids.config;
    exports com.artkids.controller.auth;
    exports com.artkids.controller.backoffice;
    exports com.artkids.controller.frontoffice;
    exports com.artkids.enums;
    exports com.artkids.model;
    exports com.artkids.service;
    exports com.artkids.util;

    opens com.artkids to javafx.fxml;
    opens com.artkids.controller.auth to javafx.fxml;
    opens com.artkids.controller.backoffice to javafx.fxml;
    opens com.artkids.controller.frontoffice to javafx.fxml;
    opens com.artkids.model to javafx.base;
}
