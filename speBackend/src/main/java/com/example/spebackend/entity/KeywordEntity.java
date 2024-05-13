package com.example.spebackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class KeywordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String keyword;

    @ManyToOne
    @JoinColumn(name = "song")
    private SongEntity song;
}
