package io.github.goto1134.randomsearchoptimisation;

/**
 * Created by Andrew
 * on 22.11.2016.
 */
class Mover extends SpaceObject {
    private final double[] speedArray;

    Mover(double[] speedArray) {
        this.speedArray = speedArray;
    }

    @Override
    double getSpeed() {
        if (getCoordinate() >= 100) {
            return 0;
        }
        return speedArray[(int) getCoordinate() / 20];
    }
}
