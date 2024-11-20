package model;

import java.util.ArrayList;

public abstract class Subject {
    private final ArrayList<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @SuppressWarnings("unused")
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String notification) {
        for (Observer observer : observers) {
            observer.update(notification);
        }
    }
}
