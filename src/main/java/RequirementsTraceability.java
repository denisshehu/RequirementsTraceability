import models.ConfusionMatrix;
import models.Dataset;
import models.Requirement;

import java.util.ArrayList;

public class RequirementsTraceability {
    private static int matchType = 0;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Match type not defined, default 0");
        } else {
            try {
                matchType = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid match type, default 0");
            }
        }

        String datasetName = "dataset-1";
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
