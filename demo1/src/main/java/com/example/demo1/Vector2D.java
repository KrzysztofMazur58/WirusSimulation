package com.example.demo1;

import java.util.ArrayList;

public class Vector2D implements IVector2D {
    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D other) {
        this.x = other.x;
        this.y = other.y;
    }

    @Override
    public double abs() {
        return Math.sqrt(x * x + y * y);
    }

    @Override
    public double cdot(IVector2D param) {
        return x * param.getComponents().get(0) + y * param.getComponents().get(1);
    }

    @Override
    public ArrayList<Double> getComponents() {
        ArrayList<Double> arrayList = new ArrayList<>();
        arrayList.add(x);
        arrayList.add(y);
        return arrayList;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
    public double distanceTo(Vector2D other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}

