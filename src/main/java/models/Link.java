package models;

import java.util.ArrayList;

public class Link {
    private final String highLevelRequirement;
    private final ArrayList<String> lowLevelRequirements;

    public Link(String highLevelRequirement, ArrayList<String> lowLevelRequirements) {
        this.highLevelRequirement = highLevelRequirement;
        this.lowLevelRequirements = lowLevelRequirements;
    }

    public String getHighLevelRequirement() {
        return highLevelRequirement;
    }

    public ArrayList<String> getLowLevelRequirements() {
        return lowLevelRequirements;
    }
}
