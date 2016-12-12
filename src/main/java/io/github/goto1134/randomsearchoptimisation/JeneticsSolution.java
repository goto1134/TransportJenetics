package io.github.goto1134.randomsearchoptimisation;

import org.jenetics.*;
import org.jenetics.engine.EvolutionStatistics;
import org.jenetics.stat.DoubleMomentStatistics;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Andrew
 * on 22.11.2016.
 */
public class JeneticsSolution {

    public static void main(final String[] args) {
        Locale.setDefault(Locale.US);
        List<String> lines = new ArrayList<>();
        lines.add("Type,Time,Generations");
//        lines.add("Type,AlterDuration,Altered,EvaluationDuration,EvolveDuration,Fitness,Invalids,Killed,PhenotypeAge,SelectionDuration");

//        for (int pSize = 10; pSize < 11; pSize += 10) {
//            System.out.println("pSize = " + pSize);
//            for (int i = 0; i < 10; i++) {
//                String result = testPopulation(pSize);
//                lines.add(result);
//            }
//        }

//        for (int tournamentSize = 5; tournamentSize < 51; tournamentSize += 5) {
//            System.out.println("tournamentSize = " + tournamentSize);
//            for (int i = 0; i < 10; i++) {
//                String result = testSelector(new TournamentSelector<>(tournamentSize));
//                lines.add(result);
//            }
//        }

//        {
//            for (int i = 0; i < 10; i++) {
//                String result = testSelector(new StochasticUniversalSelector<>());
//                lines.add(result);
//            }
//        }
//        {
//            for (int i = 0; i < 10; i++) {
//                String result = testSelector(new MonteCarloSelector<>());
//                lines.add(result);
//            }
//        }
//        {
//            for (int i = 0; i < 10; i++) {
//                String result = testSelector(new TruncationSelector<>());
//                lines.add(result);
//            }
//        }
//
//        for (double p = 0.85; p < 1; p += 0.05) {
//            System.out.println("C value = " + p);
//            for (int i = 0; i < 10; i++) {
//                String result = testSelector(new ExponentialRankSelector<>(p));
//                lines.add(result);
//            }
//        }

//        for (double p = 2; p < 5; p++) {
//            System.out.println("B value = " + p);
//            for (int i = 0; i < 10; i++) {
//                String result = testSelector(new BoltzmannSelector<>(p));
//                lines.add(result);
//            }
//        }

//        for (double p = 0; p < 0.3; p += 0.05) {
//            System.out.println("Nminus = " + p);
//            for (int i = 0; i < 10; i++) {
//                String result = testSelector(new LinearRankSelector<>(p));
//                lines.add(result);
//            }
//        }

        for (double p = 0.55; p <= 1; p += 0.05) {
            System.out.println("Probability value = " + p);
            for (int i = 0; i < 10; i++) {
                String result = testAlterer(new GaussianMutator<>(p));
                lines.add(result);
            }
        }

        writeLines(lines);
    }

    private static String testAlterer(Alterer<DoubleGene, Double> alterer) {
        System.out.println(alterer);
        TransportJeneticsSolver jeneticsSolver = TransportJeneticsSolver.newBuilder()
                .populationSize(100)
                .selector(new ExponentialRankSelector<>(0.85))
                .firstAlterer(alterer)
                .build();
        EvolutionStatistics<Double, DoubleMomentStatistics> evolutionStatistics = jeneticsSolver.solve();
        System.out.println(evolutionStatistics);
        double time = evolutionStatistics.getAlterDuration().getSum()
                + evolutionStatistics.getSelectionDuration().getSum()
                + evolutionStatistics.getEvaluationDuration().getSum()
                + evolutionStatistics.getEvolveDuration().getSum();
        return new StringBuilder("Alterer ")
                .append(alterer.toString())
                .append(",")
                .append(String.format("%f", time))
//                .append(",")
//                .append(String.format("%f", evolutionStatistics.getFitness().getMin()))
                .append(",")
                .append(evolutionStatistics.getAltered().getCount())
                .toString();
    }

    private static void writeLines(List<String> lines) {
        Path file = Paths.get("result.csv");
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
//            cat.error("", e);
        }
    }

    private static String testSelector(Selector<DoubleGene, Double> selector) {
        System.out.println(selector);
        TransportJeneticsSolver jeneticsSolver = TransportJeneticsSolver.newBuilder()
                .populationSize(100)
                .selector(selector)
                .build();
        EvolutionStatistics<Double, DoubleMomentStatistics> evolutionStatistics = jeneticsSolver.solve();
        System.out.println(evolutionStatistics);
        double time = evolutionStatistics.getAlterDuration().getSum()
                + evolutionStatistics.getSelectionDuration().getSum()
                + evolutionStatistics.getEvaluationDuration().getSum()
                + evolutionStatistics.getEvolveDuration().getSum();
        return new StringBuilder("Selector ")
                .append(selector.toString())
                .append(",")
                .append(String.format("%f", time))
                .append(",")
                .append(String.format("%f", evolutionStatistics.getFitness().getMin()))
                .append(",")
                .append(evolutionStatistics.getAltered().getCount())
                .toString();
    }

    private static String testPopulation(int populationSize) {
        TransportJeneticsSolver jeneticsSolver = TransportJeneticsSolver.newBuilder().populationSize(populationSize).build();
        EvolutionStatistics<Double, DoubleMomentStatistics> evolutionStatistics = jeneticsSolver.solve();
        System.out.println(evolutionStatistics);
        return new StringBuilder("population size ")
                .append(populationSize)
                .append(",")
                .append(String.format("%f", evolutionStatistics.getAlterDuration().getSum()))
                .append(",")
                .append(String.format("%f", evolutionStatistics.getSelectionDuration().getSum()))
                .append(",")
                .append(String.format("%f", evolutionStatistics.getEvaluationDuration().getSum()))
                .append(",")
                .append(String.format("%f", evolutionStatistics.getEvolveDuration().getSum()))
                .append(",")
                .append(String.format("%f", evolutionStatistics.getFitness().getMin()))
                .append(",")
                .append(String.format("%f", evolutionStatistics.getInvalids().getMean()))
                .append(",")
                .append(String.format("%f", evolutionStatistics.getKilled().getMean()))
                .append(",")
                .append(String.format("%f", evolutionStatistics.getAltered().getMean()))
                .append(",")
                .append(String.format("%f", evolutionStatistics.getPhenotypeAge().getMean()))
                .toString();
    }
}
