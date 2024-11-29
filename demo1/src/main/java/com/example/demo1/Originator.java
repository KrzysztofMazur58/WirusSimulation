package com.example.demo1;

import java.util.List;

public class Originator {
    private List<Person> state;

    public void setState(List<Person> state) {
        this.state = state;
    }

    public List<Person> getState() {
        return state;
    }

    public Memento saveToMemento() {
        return new Memento(state);
    }

    public void restoreFromMemento(Memento memento) {
        this.state = memento.getState();
    }
}

