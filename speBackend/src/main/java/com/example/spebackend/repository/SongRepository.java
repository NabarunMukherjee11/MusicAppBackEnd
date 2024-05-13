package com.example.spebackend.repository;

import com.example.spebackend.entity.SongEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<SongEntity, Integer> {

    @Query("select c from SongEntity c where c.songId= :songId")
    SongEntity finBySongId(String songId);
}
