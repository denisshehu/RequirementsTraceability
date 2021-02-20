package models;

import java.util.ArrayList;

public class Dataset {
    private ArrayList<Requirement> requirements;

    public ArrayList<Requirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(ArrayList<Requirement> requirements) {
        this.requirements = requirements;
    }

    public ArrayList<Requirement> getHighLevelRequirements() {
        return getCustomRequirements(RequirementLevel.HIGH);
    }

    public ArrayList<Requirement> getLowLevelRequirements() {
        return getCustomRequirements(RequirementLevel.LOW);
    }

    private ArrayList<Requirement> getCustomRequirements(RequirementLevel requirementLevel) {
        ArrayList<Requirement> result = new ArrayList<>();

        for (Requirement requirement : requirements) {
            if (requirement.getLevel() == requirementLevel) {
                result.add(requirement);
            }
        }

        return result;
    }
}
