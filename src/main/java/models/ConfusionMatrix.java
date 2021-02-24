package models;

public class ConfusionMatrix {
    private int truePositives;
    private int falseNegatives;
    private int falsePositives;
    private int trueNegatives;

    public ConfusionMatrix() {
        truePositives = 0;
        falseNegatives = 0;
        falsePositives = 0;
        trueNegatives = 0;
    }

    public int getTruePositives() {
        return truePositives;
    }

    public int getFalseNegatives() {
        return falseNegatives;
    }

    public int getFalsePositives() {
        return falsePositives;
    }

    public int getTrueNegatives() {
        return trueNegatives;
    }

    public void incrementTP() {
        truePositives++;
    }

    public void incrementFN() {
        falseNegatives++;
    }

    public void incrementFP() {
        falsePositives++;
    }

    public void incrementTN() {
        trueNegatives++;
    }

    public double getRecall() {
        return (double) truePositives / (truePositives + falseNegatives);
    }

    public double getPrecision() {
        return (double) truePositives / (truePositives + falsePositives);
    }

    public double getFMeasure() {
        double recall = getRecall();
        double precision = getPrecision();

        return 2 * (precision * recall) / (precision + recall);
    }

    public void printMatrix() {
        System.out.println("TP, FN: " + truePositives + ", " + falseNegatives);
        System.out.println("FP, TN: " + falsePositives + ", " + trueNegatives);
        System.out.println();
        System.out.println("Recall: " + getRecall());
        System.out.println("Precision: " + getPrecision());
        System.out.println("F-Measure: " + getFMeasure());
    }
}
