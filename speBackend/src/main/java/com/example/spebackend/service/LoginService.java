package com.example.spebackend.service;

import com.example.spebackend.entity.ProfileEntity;
import com.example.spebackend.repository.ProfileRepository;
import com.example.spebackend.util.EmailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class LoginService {
    private final ProfileRepository profileRepo;

    private final EmailSender emailSender;

    public LoginService(ProfileRepository profileRepo, EmailSender emailSender) {
        this.profileRepo = profileRepo;
        this.emailSender = emailSender;
    }

    public ProfileEntity getProfile(String email){
        return profileRepo.findByEmail(email);
    }

    public ProfileEntity putProfile(String email){
        ProfileEntity profile = new ProfileEntity();
        profile.setEmail(email);
        return profileRepo.save(profile);
    }

    public ProfileEntity generateOtpSendEmail(String email){
        ProfileEntity profile = profileRepo.findByEmail(email);
        String otp = emailSender.sendOtpEmail(email);
        profile.setOtp(otp);
        profile.setOtpGeneratedTime(LocalDateTime.now());
        return profileRepo.save(profile);
    }

    public ProfileEntity checkOtpTime(String email){
        ProfileEntity profile = profileRepo.findByEmail(email);
        if(profile.getOtpGeneratedTime().plusMinutes(3).isBefore(LocalDateTime.now())){
            return null;
        }
        return profile;
    }

    public ProfileEntity matchOtp(String email, String otp){
        ProfileEntity profile = profileRepo.findByEmail(email);
        if(!Objects.equals(profile.getOtp(), otp)){
            return null;
        }
        return profile;
    }
}
