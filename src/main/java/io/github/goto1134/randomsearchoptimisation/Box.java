package io.github.goto1134.randomsearchoptimisation;

/**
 * Created by Andrew
 * on 22.11.2016.
 */
class Box extends SpaceObject {
    private final double mass;
    private final double friction;

    Box() {
        mass = 5;
        friction = -0.5;
    }

    Box(double mass, double friction) {
        this.mass = mass;
        this.friction = friction;
    }

    double getMass() {
        return mass;
    }

    double getFriction() {
        return friction;
    }
}
