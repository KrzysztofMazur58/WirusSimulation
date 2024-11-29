package com.example.demo1;

public class InfectedAsymptomaticState implements HealthState {
    private double infectionDuration;
    private double timer = 0.0;

    public InfectedAsymptomaticState(double duration) {
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
        return 0.5;
    }

    public double getInfectionDuration() {
        return infectionDuration;
    }
}

