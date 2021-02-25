import models.ConfusionMatrix;
import models.Dataset;
import models.Requirement;

import java.util.ArrayList;
import java.util.Scanner;

public class RequirementsTraceability {
    private static int matchType = 0;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose trace-link generation technique (0, 1, 2, or 3):");
        String input = scanner.nextLine();

        if (input.length() == 0) {
            System.out.println("Trace-link generation technique not defined, set to 0 by default.");
        } else {
            try {
                matchType = Integer.parseInt(input);
                if (matchType > 3) {
                    System.out.println("Not a valid trace-link generation technique, set to 0 by default.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Not a valid trace-link generation technique, set to 0 by default.");
            }
        }

        String datasetName = "input";
        execute(datasetName);
    }

    private static void execute(String datasetName) {
        Dataset dataset = new Dataset();
        DatasetProcessor processor = new DatasetProcessor();
        processor.process(dataset, datasetName);
        VectorRepresentationGenerator generator = new VectorRepresentationGenerator();
        generator.generate(dataset);

        ArrayList<Requirement> highLevelRequirements = dataset.getHighLevelRequirements();
        ArrayList<Requirement> lowLevelRequirements = dataset.getLowLevelRequirements();

        //create similarity matrix
        SimilarityMatrix simMatrix = new SimilarityMatrix(highLevelRequirements, lowLevelRequirements);

        //generate links and output links to csv
        LinkMatrix linkMatrix = new LinkMatrix(simMatrix, matchType);
        linkMatrix.exportLinks(highLevelRequirements, lowLevelRequirements);
        
        // TODO: Compute the matrix after links are exported, now we get the matrix of the old links.
        ConfusionMatrix matrix = new ConfusionMatrix();
        ConfusionMatrixGenerator matrixGenerator = new ConfusionMatrixGenerator();
        matrixGenerator.generate(matrix, datasetName);

        matrix.printMatrix();
    }
}
