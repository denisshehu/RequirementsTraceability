import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import models.Dataset;
import models.Requirement;
import models.RequirementLevel;
import smile.nlp.dictionary.EnglishStopWords;
import smile.nlp.stemmer.LancasterStemmer;
import smile.nlp.stemmer.PorterStemmer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DatasetProcessor {

    public void process(Dataset dataset, String datasetName) {
        ArrayList<Requirement> requirements = process(datasetName + "/high.csv", RequirementLevel.HIGH);
        requirements.addAll(process(datasetName + "/low.csv", RequirementLevel.LOW));
        dataset.setRequirements(requirements);
    }

    private ArrayList<Requirement> process(String fileDirectory, RequirementLevel requirementLevel) {

        ArrayList<Requirement> result = new ArrayList<>();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream(fileDirectory);

        try {
            assert stream != null;
            try (CSVReader reader = new CSVReader(new InputStreamReader(stream))) {

                List<String[]> dataset = reader.readAll();
                dataset.remove(0);

                dataset.forEach(x -> {
                    ArrayList<String> text = preprocess(x[1]);
                    result.add(new Requirement(requirementLevel, x[0], text));
                });
            }
        } catch (IOException | CsvException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        return result;
    }

    private ArrayList<String> preprocess(String text) {
        ArrayList<String> result = tokenize(text);
        result.removeIf(EnglishStopWords.GOOGLE::contains);

        LancasterStemmer stemmer = new LancasterStemmer();

        for (int i = 0; i < result.size(); i++) {
            String stemmed = stemmer.stem(result.get(i));

            if (stemmed.length() == 0) {
                result.remove(i);
                i--;
            } else {
                result.set(i, stemmed);
            }
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
}
