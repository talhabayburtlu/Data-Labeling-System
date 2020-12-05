package com.DataLabelingSystem;

public class LabelingMechanismFactory {

    private static LabelingMechanismFactory labelingMechanismFactory;

    private LabelingMechanismFactory() {
    }

    private static LabelingMechanismFactory getInstance() {
        return new LabelingMechanismFactory();
    }

    public static LabelingMechanismFactory getLabelingMechanismFactory() {
        if (labelingMechanismFactory == null)
            labelingMechanismFactory = getInstance();
        return labelingMechanismFactory;
    }

    public LabelingMechanism getMechanism(String mechanismType) throws UnsupportedOperationException {
        //noinspection SwitchStatementWithTooFewBranches
        switch (mechanismType) {
            case "RandomBot":
                return RandomLabelingMechanism.getRandomLabelingMechanism();
            default:
                throw new UnsupportedOperationException("Mechanism \"" + mechanismType + "\" cannot be found.");
        }
    }

}
