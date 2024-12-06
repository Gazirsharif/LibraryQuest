package com.libraryquest.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

/**
 * Класс, представляющий квест, содержащий уникальный ID, название, описание, список шагов и возможные переходы.
 */

@Data
@Entity
@Table(name = "quests")
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int questId;

    private String title;
    private String description;

    @OneToMany(mappedBy = "quest", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Step> steps;

    public Quest() {
    }

    public Quest(int questId, String title, String description, List<Step> steps) {
        this.questId = questId;
        this.title = title;
        this.description = description;
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "Quest{" +
                "questId=" + questId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", steps=" + steps +
                '}';
    }
}
