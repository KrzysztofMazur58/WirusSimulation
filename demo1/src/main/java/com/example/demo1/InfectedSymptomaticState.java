package com.example.demo1;

public class InfectedSymptomaticState implements HealthState {
    private double infectionDuration;
    private double timer = 0.0;

    public InfectedSymptomaticState(double duration) {
        this.infectionDuration = duration;
    }

    @Override
    public void update(Person person, double deltaTime) {
        timer += deltaTime;
        if (timer >= infectionDuration) {
            person.setState(new ImmuneState());
        }
    }

    @Override
    public boolean canInfect() {
        return true;
    }

    @Override
    public double infectionProbability() {
        return 1.0;
    }

    public double getInfectionDuration() {
        return infectionDuration;
    }
}

