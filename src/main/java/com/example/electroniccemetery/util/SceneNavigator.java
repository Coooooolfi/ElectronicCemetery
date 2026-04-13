package com.example.electroniccemetery.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneNavigator {
    private static Stage mainStage;

    public static void init(Stage stage) {
        mainStage = stage;
    }

    public static void switchTo(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(
                            SceneNavigator.class.getResource("/fxml/" + fxml)
                    )
            );
            Parent root = loader.load();
            Scene scene = new Scene(root);

            try {
                scene.getStylesheets().add(
                        Objects.requireNonNull(
                                SceneNavigator.class.getResource("/css/dark-theme.css")
                        ).toExternalForm()
                );
            } catch (Exception e) {
            }

            mainStage.setTitle(title);
            mainStage.setScene(scene);


            mainStage.hide();
            mainStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}