package com.example.diogo.projectssui;

import android.view.animation.Interpolator;

/**
 * Created by Diogo on 15/05/2018.
 */

public class BounceInterpolator implements Interpolator {
    private double _Amplitude = 1;
    private double _Frequency = 10;

    BounceInterpolator(double amplitude, double frequency) {
        _Amplitude = amplitude;
        _Frequency = frequency;
    }

    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ _Amplitude) *
                Math.cos(_Frequency * time) + 1);
    }
}
