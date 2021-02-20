package models;

import java.util.ArrayList;

public class Requirement {
    private final RequirementLevel level;
    private final String ID;
    private final ArrayList<String> text;
    private double[] vectorRepresentation;

    public Requirement(RequirementLevel level, String ID, ArrayList<String> text) {
        this.level = level;
        this.ID = ID;
        this.text = text;
    }

    public Requirement(RequirementLevel level, String ID) {
        this.level = level;
        this.ID = ID;
        text = new ArrayList<>();
    }

    public RequirementLevel getLevel() {
        return level;
    }

    public String getID() {
        return ID;
    }

    public ArrayList<String> getText() {
        return text;
    }

    public double[] getVectorRepresentation() {
        return vectorRepresentation;
    }

    public void setVectorRepresentation(double[] vectorRepresentation) {
        this.vectorRepresentation = vectorRepresentation;
    }
}
