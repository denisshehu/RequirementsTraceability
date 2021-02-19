import models.Requirement;

import java.util.ArrayList;

public class SimilarityMatrix {
    private final double[][] simMatrix;

    public SimilarityMatrix(ArrayList<Requirement> highLevelReqs, ArrayList<Requirement> lowLevelReqs) {

        simMatrix = new double[highLevelReqs.size()][lowLevelReqs.size()];

        for (int i = 0; i < highLevelReqs.size(); i++) {
            for (int j = 0; j < lowLevelReqs.size(); j++) {
                simMatrix[i][j] = cossim(highLevelReqs.get(i).getVectorRepresentation(),
                        lowLevelReqs.get(j).getVectorRepresentation());
            }
        }
    }

    /*
    Returns the similarity between high level requirement H and low level requirement L
     */
    public double getSimilarity(int H, int L) {
        return simMatrix[H][L];
    }

    /*
    Returns the number of rows in the similarity matrix
     */
    public int getRows() {
        return simMatrix.length;
    }

    /*
    Returns all similarities associated with high level requirement H
     */
    public double[] getRow(int H) {
        return simMatrix[H];
    }

    /*
    Returns the number of columns in the similarity matrix
     */
    public int getColumns() {
        return simMatrix[0].length;
    }

    /*
    Computes the dot product of two vectors of equal length
     */
    private double dot(double[] a, double[] b) {
        double total = 0;
        for (int i = 0; i < a.length; i++) {
            total += a[i] * b[i];
        }
        return total;
    }

    /*
    Computes the 2-norm of a vector, used for cosine similarity
     */
    private double norm(double[] a) {
        double sum = 0;
        for (double i : a) {
            sum += i * i;
        }
        return Math.sqrt(sum);
    }

    /*
    Computes the cosine similarity between two vectors of equal length
     */
    private double cossim(double[] a, double[] b) {
        return dot(a, b) / (norm(a) * norm(b));
    }
}
