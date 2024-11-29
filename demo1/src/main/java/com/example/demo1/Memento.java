package com.example.demo1;

import java.util.ArrayList;
import java.util.List;

public class Memento {
    private final List<Person> state;

    public Memento(List<Person> state) {
        this.state = new ArrayList<>();
        for (Person person : state) {
            this.state.add(new Person(person));
        }
    }

    public List<Person> getState() {
        return state;
    }
}


