package io.github.goto1134.randomsearchoptimisation;

/**
 * Created by Andrew
 * on 22.11.2016.
 */
abstract class SpaceObject {
    private double coordinate = 0;
    private double speed = 0;

    double getCoordinate() {
        return coordinate;
    }

    void move(double distance) {
        this.coordinate += distance;
    }

    double getSpeed() {
        return speed;
    }

    void accelerate(double acceleration) {
        this.speed += acceleration;
    }
}
