package com.DataLabelingSystem.labelingMechanism;

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
        switch (mechanismType) {
            case "RandomBot":
                return RandomLabelingMechanism.getRandomLabelingMechanism();
            case "KeywordBot":
                return KeywordBasedLabelingMechanism.getKeywordBasedLabelingMechanism();
            case "AutoBot":
                return AutoLabelingMechanism.getAutoLabelingMechanism();
            case "Human":
                return ManualLabelingMechanism.getManualLabelingMechanism();
            default:
                throw new UnsupportedOperationException("Mechanism \"" + mechanismType + "\" cannot be found.");
        }
    }

}
