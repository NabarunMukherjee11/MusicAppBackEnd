package com.example.spebackend.controller;

import com.example.spebackend.dto.ErrorMessage;
import com.example.spebackend.dto.GetOtpRequest;
import com.example.spebackend.dto.LoginResponseDto;
import com.example.spebackend.dto.VerifyEmailRequest;
import com.example.spebackend.entity.ProfileEntity;
import com.example.spebackend.entity.SongEntity;
import com.example.spebackend.service.LoginService;
import com.example.spebackend.service.MusicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Controller
@RestController
@CrossOrigin
public class LoginController {

    private final LoginService loginService;
    private final MusicService musicService;

    public LoginController(LoginService loginService, MusicService musicService) {
        this.loginService = loginService;
        this.musicService = musicService;
    }

    @PostMapping("/generate-otp")
    ResponseEntity<?> generateOtp(@RequestBody GetOtpRequest body){
        ProfileEntity profile1 = loginService.getProfile(body.getEmail());
        if(profile1==null){
            ProfileEntity profile2 = loginService.putProfile(body.getEmail());
        }
        ProfileEntity profile3 = loginService.generateOtpSendEmail(body.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-otp")
    ResponseEntity<?> validateOtp(@RequestBody VerifyEmailRequest body){
        ProfileEntity profile1 = loginService.getProfile(body.getEmail());
        if(profile1==null){
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setErrorMessage("Please enter valid Email ID");
            return ResponseEntity.status(HttpStatus.OK).body(errorMessage);
        }
        ProfileEntity profile2 = loginService.checkOtpTime(profile1.getEmail());
        if(profile2==null){
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setErrorMessage("Otp is Invalid after 3 minutes. Please Try Again");
            return ResponseEntity.status(HttpStatus.OK).body(errorMessage);
        }
        ProfileEntity profile3 = loginService.matchOtp(profile1.getEmail(), body.getOtp());
        if(profile3==null){
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setErrorMessage("Wrong Otp Try again");
            return ResponseEntity.status(HttpStatus.OK).body(errorMessage);
        }
        List<LoginResponseDto> loginResponseDtos = new ArrayList<>();
        List<SongEntity> songEntities = musicService.getAllSongs();
        for (SongEntity songEntity : songEntities) {
            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setSongId(songEntity.getSongId());
            loginResponseDto.setSongName(songEntity.getSongName());
            loginResponseDto.setSongImageUrl(songEntity.getSongImageUrl());
            loginResponseDtos.add(loginResponseDto);
        }
        return ResponseEntity.ok().body(loginResponseDtos);
    }
}
