package com.example.electroniccemetery;

import com.example.electroniccemetery.util.SceneNavigator;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.util.Objects;


public class Main extends Application {

    @Override
    public void start(Stage stage) {
        SceneNavigator.init(stage);

        stage.getIcons().add(new Image(
                Objects.requireNonNull(Main.class.getResource("/img/appIcon.ico")).toExternalForm()
        ));

        stage.setWidth(850);
        stage.setHeight(600);
        stage.setMinWidth(650);
        stage.setMinHeight(700);

        SceneNavigator.switchTo("start.fxml", "Информационная система учёта захоронений");
    }
    public static void main(String[] args) {
        launch(args);
    }
}
