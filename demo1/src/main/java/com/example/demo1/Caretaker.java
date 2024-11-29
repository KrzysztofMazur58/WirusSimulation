package com.example.demo1;

import java.util.ArrayList;
import java.util.List;

public class Caretaker {
    private List<Memento> mementos = new ArrayList<>();

    public void addMemento(Memento memento) {
        mementos.add(memento);
    }

    public Memento getMemento(int index) {
        return mementos.get(index);
    }

    public int getMementoCount() {
        return mementos.size();
    }

    public void clear() {
        mementos.clear();
    }

    public void removeMementosAfter(int index) {

        for (int i = mementos.size() - 1; i > index; i--) {
            mementos.remove(i);
        }
    }
}


