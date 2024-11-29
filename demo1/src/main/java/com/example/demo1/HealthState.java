package com.example.demo1;

public interface HealthState {
    void update(Person person, double deltaTime);
    boolean canInfect();
    double infectionProbability();
}
