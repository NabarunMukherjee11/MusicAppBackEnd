package com.example.spebackend.repository;

import com.example.spebackend.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer> {
    @Query("select c from ProfileEntity c where c.email =:email")
    ProfileEntity findByEmail(String email);
}
