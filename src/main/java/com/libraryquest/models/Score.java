package com.libraryquest.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Score {
    @Id
    int questId;
    @Column
    String questTitle;
    @Column
    int userId;
    @Column
    String userName;
    @Column
    int win;
    @Column
    int lose;
}
