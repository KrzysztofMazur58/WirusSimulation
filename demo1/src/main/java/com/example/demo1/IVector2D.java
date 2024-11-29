package com.example.demo1;

import java.util.ArrayList;

public interface IVector2D {
    double abs();
    double cdot(IVector2D param);
    ArrayList<Double> getComponents();
}

