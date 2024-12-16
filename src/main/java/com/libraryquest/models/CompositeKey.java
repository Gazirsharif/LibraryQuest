package com.libraryquest.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class CompositeKey implements Serializable {
    private int questId;
    private int userId;

    public CompositeKey() {
    }

    public CompositeKey(int questId, int userId) {
        this.questId = questId;
        this.userId = userId;
    }
}