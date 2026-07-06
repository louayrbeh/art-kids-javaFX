package com.artkids.controller.auth;

import com.artkids.util.SceneManager;
import javafx.fxml.FXML;

public class HomeController {
    @FXML
    private void showLogin() {
        SceneManager.getInstance().showLogin();
    }

    @FXML
    private void showRegister() {
        SceneManager.getInstance().showRegister();
    }
}
