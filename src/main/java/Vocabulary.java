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
    private final int[][] frequencyMatrix;

    public Vocabulary(ArrayList<Requirement> highLevelRequirements, ArrayList<Requirement> lowLevelRequirements) {

        List<Requirement> requirements = Stream.of(highLevelRequirements, lowLevelRequirements)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        this.masterVocabulary = extractVocabulary(requirements);
        this.VOCABULARY_SIZE = this.masterVocabulary.size();
        this.TOTAL_REQUIREMENTS = requirements.size();
        this.frequencyMatrix = computeFrequencyMatrix(requirements);
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

    private int[][] computeFrequencyMatrix(List<Requirement> requirements) {
        int[][] matrix = new int[this.VOCABULARY_SIZE][this.TOTAL_REQUIREMENTS];
//        for (int i = 0; i < matrix.length; i++) {
//            for (int j = 0; j < matrix[i].length; j++) {
//                String vocabulary = this.masterVocabulary.get(i);
//                String
//            }
//        }

        return matrix;
    }
}
