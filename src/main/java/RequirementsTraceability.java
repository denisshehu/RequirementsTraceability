public class RequirementsTraceability {
    public static void main(String[] args) {
        int matchType = 0;
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
        Dataset dataset = new Dataset(datasetName);
        Vocabulary vocabulary = new Vocabulary(dataset.getHighLevelRequirements(), dataset.getLowLevelRequirements());
        //create similarity matrix
        SimilarityMatrix simMatrix = new SimilarityMatrix(data.getHighLevelRequirements(), data.getLowLevelRequirements());
        //generate links and output links to csv
        LinkMatrix linkMatrix = new LinkMatrix(simMatrix, matchType);
        try {
            linkMatrix.exportLinks(data.getHighLevelRequirements(), data.getLowLevelRequirements());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
