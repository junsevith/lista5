package com.example.lista5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ShapeApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ShapeApp.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Shaper!");
        stage.setScene(scene);
        stage.show();

        ShapeSave shapeSave = fxmlLoader.getController();
        shapeSave.setStage(stage);
    }

    public static void main(String[] args) {
        launch();
    }


}