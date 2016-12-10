package io.github.goto1134.randomsearchoptimisation;

import org.jenetics.*;
import org.jenetics.engine.Codec;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.engine.EvolutionStatistics;
import org.jenetics.util.Factory;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.jenetics.engine.limit.byFitnessThreshold;

/**
 * Created by Andrew
 * on 22.11.2016.
 */
public class JeneticsSolution {

    public static void main(final String[] args) {

        alter();
    }

    private static void alter() {
        final Factory<Genotype<DoubleGene>> genotypeScalarFactory = Genotype.of(DoubleChromosome.of(0, 3, 1)
                , DoubleChromosome.of(0, 6, 1)
                , DoubleChromosome.of(0, 9, 1)
                , DoubleChromosome.of(0, 6, 1)
                , DoubleChromosome.of(0, 3, 1)
        );

        Codec<double[], DoubleGene> codec = Codec.of(genotypeScalarFactory, gt -> gt.stream().mapToDouble(c -> c.getGene().doubleValue()).toArray());

        final Engine<DoubleGene, Double> geneDoubleEngine = Engine.builder(Simulation::run, codec)
                .individualCreationRetries(10000)
                .populationSize(100)
                .genotypeValidator(JeneticsSolution::isGenotypeValid)
                .optimize(Optimize.MINIMUM)
                .selector(new StochasticUniversalSelector<>())
                //Mean , Gaussian, Mutator
                .alterers(new MeanAlterer<>(), new GaussianMutator<>(), new Mutator<>())
                .build();

        Consumer<? super EvolutionResult<DoubleGene, Double>> statistics = EvolutionStatistics.ofNumber();
        final Phenotype<DoubleGene, Double> resultGenotype = geneDoubleEngine.stream()
                .limit(byFitnessThreshold(24.8))
                .limit(100000)
                .peek(statistics)
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println(statistics);

        System.out.println(" RESULT : \n\t " + Arrays.toString(codec.decode(resultGenotype.getGenotype())) + "\n\t Time = " + resultGenotype.getFitness());
    }

    private static Boolean isGenotypeValid(Genotype<DoubleGene> genotype) {
        return (genotype.length() == 5)
                && genotype.stream().allMatch(doubleGenes -> doubleGenes.getGene().doubleValue() > 0)
                && (genotype.get(0, 0).doubleValue() <= 3)
                && (genotype.get(4, 0).doubleValue() <= 3)
                && Stream.iterate(1, n -> n + 1)
                .limit(4)
                .mapToDouble(n -> Math.abs(genotype.get(n, 0).doubleValue() -
                        genotype.get(n - 1, 0).doubleValue()))
                .allMatch(d -> d <= 3);
    }
}
