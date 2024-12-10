package com.libraryquest.models;

import lombok.Data;

@Data
public class StepOption {
    private final int questId;
    private final int stepId;
    private final String question;
    private final int optionKey;
    private final String optionValue;

    public StepOption(int questId, int stepId, String question, int optionKey, String optionValue) {
        this.questId = questId;
        this.stepId = stepId;
        this.question = question;
        this.optionKey = optionKey;
        this.optionValue = optionValue;
    }
}
