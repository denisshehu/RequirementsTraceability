public class RequirementsTraceability {
    public static void main(String[] args) {
        String datasetName = "dataset-1";
        execute(datasetName);
    }

    private static void execute(String datasetName) {
        Dataset dataset = new Dataset(datasetName);
        Vocabulary vocabulary = new Vocabulary(dataset.getHighLevelRequirements(), dataset.getLowLevelRequirements());
    }
}
