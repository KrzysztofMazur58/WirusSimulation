package com.example.demo1;

public class SusceptibleState implements HealthState {
    @Override
    public void update(Person person, double deltaTime) {

    }

    @Override
    public boolean canInfect() {
        return false;
    }

    @Override
    public double infectionProbability() {
        return 0.0;
    }
}

