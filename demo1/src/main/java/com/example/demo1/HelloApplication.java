package com.example.demo1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

import java.util.Scanner;

public class HelloApplication extends Application {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private Canvas canvas;
    private Simulation simulationManager;

    public static boolean useImmunity = false;

    @Override
    public void start(Stage primaryStage) {
        canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        Scene scene = new Scene(new javafx.scene.layout.StackPane(canvas), WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Symulacja z Wieloma Osobnikami");
        primaryStage.show();

        simulationManager = new Simulation(canvas);

        simulationManager.initializeSimulation();
        simulationManager.startSimulation();

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case T -> simulationManager.promptForRestore();
            }
        });
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Czy chcesz uruchomić symulację z odpornością? (tak/nie):");
        String input = scanner.nextLine().trim().toLowerCase();
        useImmunity = input.equals("tak");
        launch(args);
    }
}





















