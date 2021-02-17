import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import models.Requirement;
import models.RequirementLevel;
import smile.nlp.dictionary.EnglishStopWords;
import smile.nlp.stemmer.LancasterStemmer;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Data {
    private final ArrayList<Requirement> highLevelRequirements;
    private final ArrayList<Requirement> lowLevelRequirements;

    public Data(String datasetName) {
        // TODO Change used directory (for IO)  to the /input and /output folder (those are used by the assignment)
        this.highLevelRequirements = new ArrayList<>();
        this.lowLevelRequirements = new ArrayList<>();

        String datasetDirectory = System.getProperty("user.dir") + "\\src\\main\\resources\\" + datasetName + "\\";
        processDataset(datasetDirectory + "high.csv", true);
        processDataset(datasetDirectory + "low.csv", false);
    }

    private void processDataset(String fileDirectory, boolean isHighLevel) {
        try (CSVReader reader = new CSVReader(new FileReader(fileDirectory))) {

            List<String[]> dataset = reader.readAll();
            dataset.remove(0);

            if (isHighLevel) {
                dataset.forEach(x -> {
                    ArrayList<String> text = preprocess(x[1]);
                    highLevelRequirements.add(new Requirement(RequirementLevel.HIGH, x[0], text));
                });
            } else {
                dataset.forEach(x -> {
                    ArrayList<String> text = preprocess(x[1]);
                    lowLevelRequirements.add(new Requirement(RequirementLevel.LOW, x[0], text));
                });
            }
        } catch (IOException | CsvException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private ArrayList<String> preprocess(String text) {
        ArrayList<String> result = tokenize(text);
        result.removeIf(EnglishStopWords.DEFAULT::contains);
        LancasterStemmer stemmer = new LancasterStemmer();
        for (int i = 0; i < result.size(); i++) {
            result.set(i, stemmer.stem(result.get(i)));
        }
        return result;
    }

    private ArrayList<String> tokenize(String text) {
        ArrayList<String> result = new ArrayList<>();

        StringTokenizer tokenizer = new StringTokenizer(text, " ,.");
        while (tokenizer.hasMoreTokens()) {
            result.add(tokenizer.nextToken().toLowerCase());
        }

        return result;
    }

    public ArrayList<Requirement> getHighLevelRequirements() {
        return highLevelRequirements;
    }

    public ArrayList<Requirement> getLowLevelRequirements() {
        return lowLevelRequirements;
    }
}
