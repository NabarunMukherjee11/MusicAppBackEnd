package com.example.spebackend.entity;

import com.example.spebackend.entity.KeywordEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class SongEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String songName;

    private String songId;

    @Column(columnDefinition = "TEXT")
    private String songDownloadLink;

    private String songImageUrl;

    private String filePath;

    @OneToMany(mappedBy = "song")
    private List<KeywordEntity> keywordEntities = new ArrayList<>();
}
