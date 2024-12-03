package com.libraryquest.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Map;

/**
 * Класс, представляющий шаг квеста с текстом вопроса, возможными ответами и ссылками на следующий шаг
 */

@Data
@Entity
@Table(name = "steps")
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stepId;

    @ManyToOne
    @JoinColumn(name = "quest_id", nullable = false)
    private Quest quest;

    private String question;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "step_options", joinColumns = @JoinColumn(name = "step_id"))
    @MapKeyColumn(name = "option_key")
    @Column(name = "option_value")
    private Map<Integer, String> options;

    public Step() {
    }

    public Step(Quest quest, int stepId, String question, Map<Integer, String> options) {
        this.quest = quest;
        this.stepId = stepId;
        this.question = question;
        this.options = options;
    }
}
