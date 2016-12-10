package io.github.goto1134.randomsearchoptimisation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

/**
 * Solution for the original task
 * <p>
 * Created by Andrew
 * on 22.11.2016.
 */
public class IterativeSolution {
    public static void main(String[] args)
            throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            int length = Integer.parseInt(reader.readLine());
            double[] speedArray = reader.readLine()
                    .chars()
                    .filter(ch -> ' ' != ch)
                    .mapToDouble(Character::getNumericValue)
                    .toArray();

            double ethalon = Simulation.run(speedArray);
            for (int i = 0; i < length; i++) {
                double[] copy = speedArray.clone();
                String[] strings = reader.readLine().split(" ");
                copy[Integer.parseInt(strings[0])] += Double.parseDouble(strings[1]);
                if (Simulation.isValid(copy)) {
                    double newTime = Simulation.run(copy);
                    if (newTime < ethalon) {
                        ethalon = newTime;
                        speedArray = copy;
                    }
                }
            }

            System.out.println(String.format(Locale.US, "%.1f %.1f %.1f %.1f %.1f %.1f ", speedArray[0], speedArray[1], speedArray[2], speedArray[3], speedArray[4], ethalon));
        }
    }
}
