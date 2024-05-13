package com.example.spebackend.repository;

import com.example.spebackend.entity.KeywordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordRepository extends JpaRepository<KeywordEntity, Integer> {
    KeywordEntity findByKeyword(String songName);
}
