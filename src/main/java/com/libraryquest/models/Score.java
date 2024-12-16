package com.libraryquest.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@IdClass(CompositeKey.class)
@Entity
public class Score {
    @Id
    int questId;
    @Id
    int userId;
    @Column
    String questTitle;
    @Column
    String userName;
    @Column
    int win;
    @Column
    int lose;
}
