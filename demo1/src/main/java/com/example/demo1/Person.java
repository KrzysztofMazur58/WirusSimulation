package com.example.demo1;

import javafx.scene.paint.Color;
import java.util.List;
import java.util.Random;

public class Person {
    private Vector2D position;
    private Vector2D velocity;
    private static final double MAX_SPEED = 2.5;
    private static final double CHANGE_DIRECTION_TIME = 1.0;
    private double directionChangeTimer = 0.0;
    private Random random = new Random();
    private Color color;
    private HealthState state;
    private double infectionTimer = 0.0;

    public Person(double startX, double startY, Color color) {
        this.position = new Vector2D(startX, startY);
        this.velocity = new Vector2D(0, 0);
        this.color = color;
        this.state = new SusceptibleState();
    }

    public Person(Person other) {
        this.position = new Vector2D(other.position);
        this.velocity = new Vector2D(other.velocity);
        this.color = other.color;
        this.state = copyState(other.state);
    }

    private HealthState copyState(HealthState state) {
        if (state instanceof SusceptibleState) {
            return new SusceptibleState();
        } else if (state instanceof ImmuneState) {
            return new ImmuneState();
        } else if (state instanceof InfectedSymptomaticState) {
            InfectedSymptomaticState infected = (InfectedSymptomaticState) state;
            return new InfectedSymptomaticState(infected.getInfectionDuration());
        } else if (state instanceof InfectedAsymptomaticState) {
            InfectedAsymptomaticState infected = (InfectedAsymptomaticState) state;
            return new InfectedAsymptomaticState(infected.getInfectionDuration());
        }
        return null;
    }

    public void setState(HealthState newState) {
        this.state = newState;
        this.color = determineColor(newState);
    }

    private Color determineColor(HealthState state) {
        if (state instanceof ImmuneState) return Color.GREEN;
        if (state instanceof InfectedSymptomaticState) return Color.RED;
        if (state instanceof InfectedAsymptomaticState) return Color.ORANGE;
        return Color.BLUE;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public Vector2D randomVelocity() {
        double angle = random.nextDouble() * 2 * Math.PI;
        double speed = random.nextDouble() * MAX_SPEED;
        return new Vector2D(speed * Math.cos(angle), speed * Math.sin(angle));
    }

    public boolean handleBoundary(double width, double height) {
        boolean outOfBounds = position.getX() < 0 || position.getX() > width || position.getY() < 0 || position.getY() > height;

        if (outOfBounds) {
            if (random.nextBoolean()) {
                velocity.setX(-velocity.getX());
                velocity.setY(-velocity.getY());
                return false;
            } else {
                return true;
            }
        }

        return false;
    }

    public void updatePosition(double deltaTime) {
        directionChangeTimer += deltaTime;
        if (directionChangeTimer >= CHANGE_DIRECTION_TIME) {
            this.velocity = randomVelocity();
            directionChangeTimer = 0.0;
        }

        position.setX(position.getX() + velocity.getX() * deltaTime);
        position.setY(position.getY() + velocity.getY() * deltaTime);
    }

    public void updateState(double deltaTime) {
        state.update(this, deltaTime);
    }

    public void checkForInfection(List<Person> otherPersons) {
        for (Person other : otherPersons) {

            if (other != this && other.state.canInfect() && this.state instanceof SusceptibleState) {
                double distance = this.position.distanceTo(other.position);

                if (distance <= 12.0 && isCloseForRequiredTime(other)) {
                    double infectionChance = other.state.infectionProbability();
                    if (random.nextDouble() < infectionChance) {
                        infect();
                    }
                }
            }
        }
    }


    private boolean isCloseForRequiredTime(Person other) {
        infectionTimer += 1.0 / 25.0;
        return infectionTimer >= 3.0;
    }

    private void infect() {
        double infectionDuration = 20 + random.nextInt(11);
        boolean symptomatic = random.nextBoolean();
        setState(symptomatic ? new InfectedSymptomaticState(infectionDuration) : new InfectedAsymptomaticState(infectionDuration));
    }

    public Vector2D getPosition() {
        return position;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public Color getColor() {
        return color;
    }

    public HealthState getState()
    {
        return state;
    }
}










