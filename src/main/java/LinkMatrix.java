import java.io.FileWriter;
import com.opencsv.CSVWriter;
import models.Requirement;

import java.util.ArrayList;
import java.util.List;

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

    // TODO check if the format is correct
    /*
    Exports the linkMatrix to a csv file in the format defined in the assignment
     */
    public void exportLinks(ArrayList<Requirement> highLevelRequirements, ArrayList<Requirement> lowLevelRequirements) throws Exception {
        CSVWriter writer = new CSVWriter(new FileWriter(".\\output\\links.csv"));
        List list = new ArrayList();
        for (int i = 0; i < linkMatrix.length; i++) {
            ArrayList<String> line = new ArrayList<>();
            line.add(highLevelRequirements.get(i).getID());
            for (int j = 0; j < linkMatrix[0].length; j++) {
                if (linkMatrix[i][j]) {
                    line.add(lowLevelRequirements.get(j).getID());
                }
            }
            String[] lineArray = new String[line.size()];
            for (int j = 0; j < line.size(); j++) {
                lineArray[j] = line.get(j);
            }
            list.add(lineArray);
        }
        //temp
        writer.writeAll(list);
        writer.flush();
        System.out.println("Links exported");
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
