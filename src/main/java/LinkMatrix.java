import com.opencsv.CSVWriter;
import models.Requirement;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class LinkMatrix {
    private final SimilarityMatrix simMatrix;
    private final boolean[][] linkMatrix;

    public LinkMatrix(SimilarityMatrix simMatrix, int matchType) {
        this.simMatrix = simMatrix;
        linkMatrix = new boolean[simMatrix.getRows()][simMatrix.getColumns()];

        // TODO add case 3 for custom link detection if applicable
        switch (matchType) {
            case 1 -> minSimScoreLinks(0.25);
            case 2 -> relativeSimLinks(0.67);
            case 3 -> advancedLinks();
            default -> minSimScoreLinks(0);
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

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    Creates the link matrix for the given similarity matrix.
    There exists a link if the similarity is > 0 and >= simScore
     */
    private void minSimScoreLinks(double simScore) {
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
    private void relativeSimLinks(double relSimScore) {
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

    private void advancedLinks() {
        double medianMax = getMedian(getMaximumValues());

        for (int i = 0; i < simMatrix.getRows(); i++) {
            double[] row = simMatrix.getRow(i);
            double SD = getSD(row);

            for (int j = 0; j < simMatrix.getColumns(); j++) {
                linkMatrix[i][j] = simMatrix.getSimilarity(i, j) >= medianMax + 0.1 * SD;
            }
        }
    }

    private double[] getMaximumValues() {
        double[] result = new double[simMatrix.getRows()];

        for (int i = 0; i < result.length; i++) {

            double[] row = simMatrix.getRow(i);
            double maximumValue = 0;

            for (int j = 0; j < row.length; j++) {
                if (row[j] > maximumValue) {
                    maximumValue = row[j];
                }
            }

            result[i] = maximumValue;
        }

        return result;
    }

    private double getMedian(double[] array) {
        double median;

        Arrays.sort(array);

        int length = array.length;

        if (length % 2 == 1) {
            median = array[(length + 1) / 2 - 1];
        } else {
            median = (array[length / 2 - 1] + array[length / 2]) / 2;
        }

        return median;
    }

    private double getMean(double[] array) {
        double sum = 0.0;
        for (double element : array) {
            sum += element;
        }

        return sum / array.length;
    }

    private double getSD(double[] array) {

        double mean = getMean(array);
        double squaredDifference = 0.0;

        for (double element : array) {
            squaredDifference += Math.pow(element - mean, 2);
        }

        return Math.sqrt(squaredDifference / array.length);
    }

    private void printArray(double[] array) {
        System.out.print("[ ");
        for (int i = 0; i < array.length; i++) {
            if (i == array.length - 1) {
                System.out.println(array[i] + " ]");
            } else {
                System.out.print(array[i] + ", ");
            }
        }
    }
}
