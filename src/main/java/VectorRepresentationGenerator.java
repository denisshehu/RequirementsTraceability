import models.Dataset;
import models.Requirement;

import java.util.ArrayList;

public class VectorRepresentationGenerator {
    private final ArrayList<String> masterVocabulary = new ArrayList<>();
    private int TOTAL_REQUIREMENTS;
    private int VOCABULARY_SIZE;

    public void generate(Dataset dataset) {
        ArrayList<Requirement> requirements = dataset.getRequirements();
        TOTAL_REQUIREMENTS = requirements.size();

        extractVocabulary(requirements);
        VOCABULARY_SIZE = masterVocabulary.size();

        computeVectorRepresentations(requirements);
    }

    private void extractVocabulary(ArrayList<Requirement> requirements) {
        for (Requirement requirement : requirements) {
            for (String word : requirement.getText()) {
                if (!(masterVocabulary.contains(word))) {
                    masterVocabulary.add(word);
                }
            }
        }
    }

    private void computeVectorRepresentations(ArrayList<Requirement> requirements) {

        for (Requirement requirement : requirements) {

            ArrayList<String> text = requirement.getText();
            double[] vectorRepresentation = new double[VOCABULARY_SIZE];

            for (String word : text) {
                for (int i = 0; i < VOCABULARY_SIZE; i++) {
                    if (masterVocabulary.get(i).equals(word)) {
                        vectorRepresentation[i]++;
                        break;
                    }
                }
            }

            requirement.setVectorRepresentation(vectorRepresentation);
        }

        double[] df = compute_df(requirements);

        for (Requirement requirement : requirements) {

            double[] vectorRepresentation = requirement.getVectorRepresentation();

            for (int i = 0; i < VOCABULARY_SIZE; i++) {
                if (vectorRepresentation[i] != 0) {
                    vectorRepresentation[i] *= df[i];
                }
            }

            requirement.setVectorRepresentation(vectorRepresentation);
        }
    }

    private double[] compute_df(ArrayList<Requirement> requirements) {
        double[] result = new double[VOCABULARY_SIZE];

        for (int i = 0; i < result.length; i++) {
            for (Requirement requirement : requirements) {

                double[] vectorRepresentation = requirement.getVectorRepresentation();

                if (vectorRepresentation[i] != 0) {
                    result[i]++;
                }
            }

            result[i] = logBaseTwo(TOTAL_REQUIREMENTS / result[i]);
        }

        return result;
    }

    private double logBaseTwo(double number) {
        return Math.log(number) / Math.log(2);
    }
}
