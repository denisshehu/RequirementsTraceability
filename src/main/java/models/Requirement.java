package models;

import java.util.ArrayList;

public class Requirement {
    private RequirementLevel level;
    private String ID;
    private ArrayList<String> text;

    public Requirement(RequirementLevel level, String ID, ArrayList<String> text) {
        this.level = level;
        this.ID = ID;
        this.text = text;
    }

    public RequirementLevel getLevel() {
        return level;
    }

    public void setLevel(RequirementLevel level) {
        this.level = level;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public ArrayList<String> getText() {
        return text;
    }

    public void setText(ArrayList<String> text) {
        this.text = text;
    }

    // TODO implement vector representation
    public double[] getVectorRepresentation() {
        double[] temp = new double[1];
        temp[0] = 1;
        return temp;
    }
}
