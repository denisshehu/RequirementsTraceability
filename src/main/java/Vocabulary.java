import models.Requirement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Vocabulary {
    private final ArrayList<String> masterVocabulary;
    private final int VOCABULARY_SIZE;
    private final int TOTAL_REQUIREMENTS;
    private final double[][] matrixRepresentation;

    public Vocabulary(ArrayList<Requirement> highLevelRequirements, ArrayList<Requirement> lowLevelRequirements) {

        List<Requirement> requirements = Stream.of(highLevelRequirements, lowLevelRequirements)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        this.masterVocabulary = extractVocabulary(requirements);
        this.VOCABULARY_SIZE = this.masterVocabulary.size();
        this.TOTAL_REQUIREMENTS = requirements.size();
        this.matrixRepresentation = new double[this.TOTAL_REQUIREMENTS][this.VOCABULARY_SIZE];

        computeMatrixRepresentation(requirements);
        setVectorRepresentations(requirements);
    }

    private ArrayList<String> extractVocabulary(List<Requirement> requirements) {
        ArrayList<String> vocabulary = new ArrayList<>();

        for (Requirement requirement : requirements) {
            for (String word : requirement.getText()) {
                if (!(vocabulary.contains(word))) {
                    vocabulary.add(word);
                }
            }
        }

        return vocabulary;
    }

    private void computeMatrixRepresentation(List<Requirement> requirements) {

        for (int i = 0; i < this.matrixRepresentation.length; i++) {

            ArrayList<String> requirementText = requirements.get(i).getText();

            for (String word : requirementText) {
                for (int j = 0; j < this.matrixRepresentation[i].length; j++) {
                    if (masterVocabulary.get(j).equals(word)) {
                        this.matrixRepresentation[i][j]++;
                        break;
                    }
                }
            }
        }

        double[] df = compute_df();

        for (int i = 0; i < this.matrixRepresentation.length; i++) {
            for (int j = 0; j < this.matrixRepresentation[i].length; j++) {
                if (this.matrixRepresentation[i][j] != 0) {
                    this.matrixRepresentation[i][j] *= df[j];
                }
            }
        }
    }

    private double[] compute_df() {
        double[] df = new double[this.VOCABULARY_SIZE];

        for (int i = 0; i < df.length; i++) {
            for (int j = 0; j < this.matrixRepresentation.length; j++) {
                if (matrixRepresentation[j][i] != 0) {
                    df[i]++;
                }
            }

            df[i] = logBaseTwo(this.TOTAL_REQUIREMENTS / df[i]);
        }

        return df;
    }

    private double logBaseTwo(double number) {
        return Math.log(number) / Math.log(2);
    }

    private void setVectorRepresentations(List<Requirement> requirements) {
        for (int i = 0; i < requirements.size(); i++) {
            requirements.get(i).setVectorRepresentation(this.matrixRepresentation[i]);
        }
    }
}
