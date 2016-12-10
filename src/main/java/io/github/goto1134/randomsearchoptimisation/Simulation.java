package io.github.goto1134.randomsearchoptimisation;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by Andrew
 * on 22.11.2016.
 */
class Simulation {
    static double run(double[] speedArray) {
        Box box = new Box();
        Mover mover = new Mover(speedArray);

        double time = 0;     // initial time
        double dt = 0.2; // time step of simulation
        double springElasticity = 0.5;  // coefficient of elasticity of the spring (as in Hooke's Law)

        while (mover.getCoordinate() <= 100
                || Math.abs(mover.getCoordinate() - box.getCoordinate()) > 0.1
                || Math.abs(box.getSpeed()) > 0.1) {
            double force_spring = springElasticity * (mover.getCoordinate() - box.getCoordinate());
            double force_friction = box.getFriction() * box.getSpeed();
            double force_total = force_spring + force_friction;
            double acceleration = force_total / box.getMass();

            box.move(box.getSpeed() * dt);
            box.accelerate(acceleration * dt);
            time += dt;
            mover.move(mover.getSpeed() * dt);
        }
        return time;

    }

    static boolean isValid(double[] speedArray) {
        return speedArray.length == 5
                && Arrays.stream(speedArray).allMatch(s -> s > 0)
                && speedArray[0] <= 3
                && speedArray[4] <= 3
                && Stream.iterate(1, n -> n + 1)
                .limit(4)
                .map(n -> speedArray[n] - speedArray[n - 1])
                .map(Math::abs).allMatch(n -> n < 3);
    }
}
