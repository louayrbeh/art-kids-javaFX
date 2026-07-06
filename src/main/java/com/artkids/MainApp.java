package com.artkids;

import com.artkids.config.AppConfig;
import com.artkids.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        AppConfig.getInstance().initialize();

        primaryStage.setTitle("ArtKids");
        primaryStage.setMinWidth(1180);
        primaryStage.setMinHeight(760);

        SceneManager.getInstance().initialize(primaryStage);
        SceneManager.getInstance().showHome();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
