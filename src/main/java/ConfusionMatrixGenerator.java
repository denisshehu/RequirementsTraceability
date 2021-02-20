import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import models.ConfusionMatrix;
import models.Link;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ConfusionMatrixGenerator {
    private ArrayList<String> lowLevelRequirements;
    private ArrayList<Link> manuallyComputedLinks;
    private ArrayList<Link> automaticallyComputedLinks;

    public void generate(ConfusionMatrix matrix, String datasetName) {
        lowLevelRequirements = new ArrayList<>();
        manuallyComputedLinks = loadLinks(datasetName + "/links.csv");
        automaticallyComputedLinks = loadLinks("output/links.csv");

        computeConfusionMatrix(matrix);
    }

    private ArrayList<Link> loadLinks(String fileDirectory) {

        ArrayList<Link> result = new ArrayList<>();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream(fileDirectory);

        try {
            assert stream != null;
            try (CSVReader reader = new CSVReader(new InputStreamReader(stream))) {

                List<String[]> file = reader.readAll();
                file.remove(0);

                file.forEach(x -> {
                    ArrayList<String> lowLevelRequirements = getLowLevelRequirements(x[1]);
                    result.add(new Link(x[0], lowLevelRequirements));
                });
            }
        } catch (IOException | CsvException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        return result;
    }

    private ArrayList<String> getLowLevelRequirements(String requirements) {
        ArrayList<String> result = new ArrayList<>();

        StringTokenizer tokenizer = new StringTokenizer(requirements, ",");
        while (tokenizer.hasMoreTokens()) {
            String ID = tokenizer.nextToken();
            result.add(ID);
            if (! lowLevelRequirements.contains(ID)) {
                lowLevelRequirements.add(ID);
            }
        }

        return result;
    }

    private void computeConfusionMatrix(ConfusionMatrix matrix) {
        for (int i = 0; i < manuallyComputedLinks.size(); i++) {
            Link manuallyComputedLink = manuallyComputedLinks.get(i);
            Link automaticallyComputedLink = automaticallyComputedLinks.get(i);

            for (String lowLevelRequirement : lowLevelRequirements) {
                boolean isInManual = manuallyComputedLink.getLowLevelRequirements().contains(lowLevelRequirement);
                boolean isInAutomatic = automaticallyComputedLink.getLowLevelRequirements()
                        .contains(lowLevelRequirement);

                if (isInManual) {
                    if (isInAutomatic) {
                        matrix.incrementTP();
                    } else {
                        matrix.incrementFN();
                    }
                } else {
                    if (isInAutomatic) {
                        matrix.incrementFP();
                    } else {
                        matrix.incrementTN();
                    }
                }
            }
        }
    }
}
