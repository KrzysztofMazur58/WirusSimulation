package com.example.demo1;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;

import java.util.*;

public class Simulation {
    private static final int NUM_PEOPLE = 1000;
    private static final double SPAWN_INTERVAL = 1.0;
    private static final double UPDATE_INTERVAL = 1.0 / 25.0;

    private List<Person> people;
    private Canvas canvas;
    private boolean simulationRunning;
    private Random random = new Random();
    private double spawnTimer = 0.0;

    private Originator originator = new Originator();
    private Caretaker caretaker = new Caretaker();
    private double saveTimer = 0.0;

    public Simulation(Canvas canvas) {
        this.canvas = canvas;
        this.people = new ArrayList<>();
    }

    public void initializeSimulation() {
        for (int i = 0; i < NUM_PEOPLE; i++) {
            double startX = random.nextDouble() * canvas.getWidth();
            double startY = random.nextDouble() * canvas.getHeight();
            Color color = Color.BLUE; // Default: Susceptible
            Person person = new Person(startX, startY, color);
            person.setVelocity(person.randomVelocity());

            if (HelloApplication.useImmunity && random.nextDouble() < 0.3) {
                person.setState(new ImmuneState());
            } else {
                person.setState(new SusceptibleState());
            }

            people.add(person);
        }
    }

    public void startSimulation() {
        simulationRunning = true;

        Thread simulationThread = new Thread(() -> {
            long lastUpdateTime = System.nanoTime();
            long targetInterval = 1000000000 / 25;

            while (simulationRunning) {
                long currentTime = System.nanoTime();
                long elapsedTime = currentTime - lastUpdateTime;
                double deltaTime = elapsedTime / 1_000_000_000.0;

                updateSimulation(deltaTime);

                long sleepTime = targetInterval - elapsedTime;

                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime / 1_000_000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                lastUpdateTime = System.nanoTime();
            }
        });

        simulationThread.setDaemon(true);
        simulationThread.start();

        AnimationTimer renderTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                render();
            }
        };
        renderTimer.start();
    }

    private void updateSimulation(double deltaTime) {
        Iterator<Person> iterator = people.iterator();
        spawnTimer += 0.04;
        saveTimer += 0.04;

        while (iterator.hasNext()) {
            Person person = iterator.next();
            person.updatePosition(UPDATE_INTERVAL);
            person.updateState(UPDATE_INTERVAL);

            if (person.handleBoundary(canvas.getWidth(), canvas.getHeight())) {
                iterator.remove();
            } else {
                person.checkForInfection(people);
            }
        }

        if (spawnTimer >= SPAWN_INTERVAL) {
            spawnNewPerson();
            spawnTimer = 0.0;
        }

        if (saveTimer >= 1.0) {
            saveCurrentState();
            saveTimer = 0.0;
        }
    }

    private void saveCurrentState() {
        originator.setState(new ArrayList<>(people));
        caretaker.addMemento(originator.saveToMemento());
    }

    private void spawnNewPerson() {
        double x, y;

        int wall = random.nextInt(4);

        switch (wall) {
            case 0:
                x = 10;
                y = random.nextDouble() * (canvas.getHeight() - 20) + 10;
                break;
            case 1:
                x = canvas.getWidth() - 10;
                y = random.nextDouble() * (canvas.getHeight() - 20) + 10;
                break;
            case 2:
                x = random.nextDouble() * (canvas.getWidth() - 20) + 10;
                y = 10;
                break;
            case 3:
                x = random.nextDouble() * (canvas.getWidth() - 20) + 10;
                y = canvas.getHeight() - 10;
                break;
            default:
                throw new IllegalStateException("Nieprawidłowa wartość ściany: " + wall);
        }

        boolean isInfected = random.nextDouble() < 0.1;
        Person newPerson = new Person(x, y, isInfected ? Color.RED : Color.BLUE);

        if (isInfected) {
            boolean symptomatic = random.nextBoolean();
            double infectionDuration = 20 + random.nextInt(11);
            newPerson.setState(symptomatic
                    ? new InfectedSymptomaticState(infectionDuration)
                    : new InfectedAsymptomaticState(infectionDuration));
        } else {
            newPerson.setState(new SusceptibleState());
        }

        newPerson.setVelocity(newPerson.randomVelocity());
        people.add(newPerson);
    }

    private void render() {
        Platform.runLater(() -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setStroke(Color.BLACK);
            gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());

            try {
                for (Person person : people) {
                    gc.setFill(person.getColor());
                    gc.fillOval(person.getPosition().getX(), person.getPosition().getY(), 10, 10);
                }
            } catch (Exception e) {

            }
        });
    }

    public void promptForRestore() {
        javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog();
        dialog.setTitle("Przywracanie symulacji");
        dialog.setHeaderText("Przywróć stan symulacji");
        dialog.setContentText("Podaj numer sekundy (0 - " + (caretaker.getMementoCount() - 1) + "):");

        dialog.showAndWait().ifPresent(input -> {
            try {
                int second = Integer.parseInt(input);
                if (second >= 0 && second < caretaker.getMementoCount()) {
                    restoreSimulation(second);
                } else {
                    System.out.println("Nieprawidłowa sekunda: " + second);
                }
            } catch (NumberFormatException e) {
                System.out.println("Nieprawidłowy format liczby!");
            }
        });
    }

    public void restoreLastSavedState() {
        restoreSimulation(caretaker.getMementoCount() - 1);
    }

    private void restoreSimulation(int mementoIndex) {
        if (mementoIndex >= 0 && mementoIndex < caretaker.getMementoCount()) {
            Memento memento = caretaker.getMemento(mementoIndex);
            originator.restoreFromMemento(memento);
            people = originator.getState();
            caretaker.clear();
            saveCurrentState();
        }
    }
}

