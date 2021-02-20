import com.opencsv.CSVWriter;
import models.Requirement;

import java.io.*;
import java.util.ArrayList;

public class LinkMatrix {
    private final boolean[][] linkMatrix;

    public LinkMatrix(SimilarityMatrix simMatrix, int matchType) {
        linkMatrix = new boolean[simMatrix.getRows()][simMatrix.getColumns()];

        // TODO add case 3 for custom link detection if applicable
        switch (matchType) {
            case 1 -> minSimScoreLinks(simMatrix, 0.25);
            case 2 -> relativeSimLinks(simMatrix, 0.67);
            default -> minSimScoreLinks(simMatrix, 0);
        }
    }

    /*
    Exports the linkMatrix to a csv file in the format defined in the assignment
     */
    public void exportLinks(ArrayList<Requirement> highLevelRequirements, ArrayList<Requirement> lowLevelRequirements) {

        try {
            // TODO: Use OutputStreamWriter (instead of FileWriter) because now this code does not work on docker!
            // TODO: Only use double quotes for the list of low level requirements, just like the given links.csv file.
            CSVWriter writer = new CSVWriter(new FileWriter("src/main/resources/output/links.csv"));

            ArrayList<String[]> links = new ArrayList<>();
            links.add(new String[] {"id", "links"});

            for (int i = 0; i < linkMatrix.length; i++) {

                String[] link = new String[2];
                link[0] = highLevelRequirements.get(i).getID();

                link[1] = "";
                for (int j = 0; j < linkMatrix[0].length; j++) {
                    if (linkMatrix[i][j]) {
                        link[1] += lowLevelRequirements.get(j).getID() + ",";
                    }
                }

                if (link[1].length() != 0) {
                    link[1] = link[1].substring(0, link[1].length() - 1);
                }

                links.add(link);
            }

            writer.writeAll(links);
            writer.flush();
            System.out.println("Links exported");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    Creates the link matrix for the given similarity matrix.
    There exists a link if the similarity is > 0 and >= simScore
     */
    private void minSimScoreLinks(SimilarityMatrix simMatrix, double simScore) {
        for (int i = 0; i < linkMatrix.length; i++) {
            for (int j = 0; j < linkMatrix[0].length; j++) {
                linkMatrix[i][j] = simMatrix.getSimilarity(i, j) >= simScore && simMatrix.getSimilarity(i, j) > 0;
            }
        }
    }

    /*
    Creates the link matrix for the given similarity matrix.
    There exists a link between h and l' if the similarity sim(h, l') >= relSimScore * sim(h, l)
    where l has the highest similarity of any l for h
     */
    private void relativeSimLinks(SimilarityMatrix simMatrix, double relSimScore) {
        for (int i = 0; i < linkMatrix.length; i++) {
            // get the maximum similarity for the current H
            double[] currentH = simMatrix.getRow(i);
            double maxSim = 0;
            for (double value : currentH) {
                if (value > maxSim) {
                    maxSim = value;
                }
            }

            for (int j = 0; j < linkMatrix[0].length; j++) {
                linkMatrix[i][j] = simMatrix.getSimilarity(i, j) >= relSimScore * maxSim;
            }
        }
    }
}
