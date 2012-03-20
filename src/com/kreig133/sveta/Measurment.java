package com.kreig133.sveta;

/**
 * @author kreig133
 * @version 1.0
 */
public class Measurment {

    double time;
    double temp;
    boolean withNoise;

    public Measurment( double time, double temp, boolean withNoise ) {
        this.time = time;
        this.temp = temp;
        this.withNoise = withNoise;
    }

    @Override
    public String toString() {
        return "Measurment{" +
                "time=" + time +
                ", temp=" + temp +
                ", withNoise=" + withNoise +
                '}';
    }
}
