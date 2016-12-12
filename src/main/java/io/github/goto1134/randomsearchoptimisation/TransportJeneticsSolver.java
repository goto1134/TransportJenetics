package io.github.goto1134.randomsearchoptimisation;

import org.jenetics.*;
import org.jenetics.engine.Codec;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.engine.EvolutionStatistics;
import org.jenetics.stat.DoubleMomentStatistics;
import org.jenetics.util.Factory;

import java.util.stream.Stream;

import static org.jenetics.engine.limit.byFitnessThreshold;

/**
 * Created by Andrew
 * on 10.12.2016.
 */
class TransportJeneticsSolver {

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(TransportJeneticsSolver copy) {
        Builder builder = new Builder();
        builder.populationSize = copy.populationSize;
        builder.selector = copy.selector;
        builder.firstAlterer = copy.firstAlterer;
        builder.restAlterers = copy.restAlterers;
        return builder;
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

    private final Factory<Genotype<DoubleGene>> genotypeScalarFactory = Genotype.of(DoubleChromosome.of(0, 3, 1)
            , DoubleChromosome.of(0, 6, 1)
            , DoubleChromosome.of(0, 9, 1)
            , DoubleChromosome.of(0, 6, 1)
            , DoubleChromosome.of(0, 3, 1)
    );

    private final Codec<double[], DoubleGene> codec = Codec.of(genotypeScalarFactory, gt -> gt.stream().mapToDouble(c -> c.getGene().doubleValue()).toArray());
    private int populationSize = 100;
    private Selector<DoubleGene, Double> selector = new StochasticUniversalSelector<>();
    private Alterer<DoubleGene, Double> firstAlterer = new MeanAlterer<>();
    private Alterer<DoubleGene, Double>[] restAlterers = new Alterer[]{new GaussianMutator<>(), new Mutator<>()};

    private TransportJeneticsSolver(Builder builder) {
        populationSize = builder.populationSize;
        selector = builder.selector;
        firstAlterer = builder.firstAlterer;
        restAlterers = builder.restAlterers;
    }

    public EvolutionStatistics<Double, DoubleMomentStatistics> solve() {

        Engine.Builder<DoubleGene, Double> builder = Engine.builder(Simulation::run, codec)
                .individualCreationRetries(10000)
                .populationSize(populationSize)
                .genotypeValidator(TransportJeneticsSolver::isGenotypeValid)
                .optimize(Optimize.MINIMUM);

        if (selector != null) {
            builder = builder.selector(selector);
        }
        if (firstAlterer != null) {
            if (restAlterers != null) {
                builder = builder.alterers(firstAlterer, restAlterers);
            } else {
                builder = builder.alterers(firstAlterer);
            }
        }

        final Engine<DoubleGene, Double> geneDoubleEngine = builder.build();

        EvolutionStatistics<Double, DoubleMomentStatistics> statistics = EvolutionStatistics.ofNumber();

        final Phenotype<DoubleGene, Double> resultGenotype = geneDoubleEngine.stream()
                .limit(byFitnessThreshold(24.8))
//                .limit(100000)
                .peek(statistics)
                .collect(EvolutionResult.toBestPhenotype());

        return statistics;
    }

    public static final class Builder {
        private int populationSize;
        private Selector<DoubleGene, Double> selector;
        private Alterer<DoubleGene, Double> firstAlterer;
        private Alterer<DoubleGene, Double>[] restAlterers;

        private Builder() {
        }

        public Builder populationSize(int populationSize) {
            this.populationSize = populationSize;
            return this;
        }

        public Builder selector(Selector<DoubleGene, Double> selector) {
            this.selector = selector;
            return this;
        }

        public Builder firstAlterer(Alterer<DoubleGene, Double> firstAlterer) {
            this.firstAlterer = firstAlterer;
            return this;
        }

        public Builder restAlterers(Alterer<DoubleGene, Double>[] restAlterers) {
            this.restAlterers = restAlterers;
            return this;
        }

        public TransportJeneticsSolver build() {
            return new TransportJeneticsSolver(this);
        }
    }
}
