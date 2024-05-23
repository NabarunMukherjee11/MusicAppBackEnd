package com.example.spebackend.controller;

import com.example.spebackend.dto.ErrorMessage;
import com.example.spebackend.dto.GetOtpRequest;
import com.example.spebackend.dto.LoginResponseDto;
import com.example.spebackend.dto.VerifyEmailRequest;
import com.example.spebackend.entity.ProfileEntity;
import com.example.spebackend.entity.SongEntity;
import com.example.spebackend.service.LoginService;
import com.example.spebackend.service.MusicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private LoginService loginService;

    @Mock
    private MusicService musicService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateOtp() {
        GetOtpRequest request = new GetOtpRequest();
        request.setEmail("test@example.com");

        when(loginService.getProfile(request.getEmail())).thenReturn(null);
        when(loginService.putProfile(request.getEmail())).thenReturn(new ProfileEntity());
        when(loginService.generateOtpSendEmail(request.getEmail())).thenReturn(new ProfileEntity());

        ResponseEntity<?> response = loginController.generateOtp(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testValidateOtp() {
        VerifyEmailRequest request = new VerifyEmailRequest();
        request.setEmail("test@example.com");
        request.setOtp("123456");

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setEmail("test@example.com");
        profileEntity.setOtp("123456");

        when(loginService.getProfile(request.getEmail())).thenReturn(profileEntity);
        when(loginService.checkOtpTime(profileEntity.getEmail())).thenReturn(profileEntity);
        when(loginService.matchOtp(profileEntity.getEmail(), request.getOtp())).thenReturn(profileEntity);

        SongEntity songEntity = new SongEntity();
        songEntity.setSongId("1");
        songEntity.setSongName("Song1");
        songEntity.setSongImageUrl("imageUrl");

        List<SongEntity> songEntities = Arrays.asList(songEntity);
        when(musicService.getAllSongs()).thenReturn(songEntities);

        ResponseEntity<?> response = loginController.validateOtp(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, ((List<LoginResponseDto>) response.getBody()).size());
    }

    @Test
    public void testValidateOtpInvalidEmail() {
        VerifyEmailRequest request = new VerifyEmailRequest();
        request.setEmail("test@example.com");
        request.setOtp("123456");

        when(loginService.getProfile(request.getEmail())).thenReturn(null);

        ResponseEntity<?> response = loginController.validateOtp(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Please enter valid Email ID", ((ErrorMessage) response.getBody()).getErrorMessage());
    }

    @Test
    public void testValidateOtpExpiredOtp() {
        VerifyEmailRequest request = new VerifyEmailRequest();
        request.setEmail("test@example.com");
        request.setOtp("123456");

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setEmail("test@example.com");

        when(loginService.getProfile(request.getEmail())).thenReturn(profileEntity);
        when(loginService.checkOtpTime(profileEntity.getEmail())).thenReturn(null);

        ResponseEntity<?> response = loginController.validateOtp(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Otp is Invalid after 3 minutes. Please Try Again", ((ErrorMessage) response.getBody()).getErrorMessage());
    }

    @Test
    public void testValidateOtpWrongOtp() {
        VerifyEmailRequest request = new VerifyEmailRequest();
        request.setEmail("test@example.com");
        request.setOtp("123456");

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setEmail("test@example.com");

        when(loginService.getProfile(request.getEmail())).thenReturn(profileEntity);
        when(loginService.checkOtpTime(profileEntity.getEmail())).thenReturn(profileEntity);
        when(loginService.matchOtp(profileEntity.getEmail(), request.getOtp())).thenReturn(null);

        ResponseEntity<?> response = loginController.validateOtp(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Wrong Otp Try again", ((ErrorMessage) response.getBody()).getErrorMessage());
    }
}
