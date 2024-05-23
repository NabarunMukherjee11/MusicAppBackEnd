package com.example.spebackend.service;

import com.example.spebackend.entity.ProfileEntity;
import com.example.spebackend.repository.ProfileRepository;
import com.example.spebackend.util.EmailSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private ProfileRepository profileRepo;

    @Mock
    private EmailSender emailSender;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetProfile() {
        String email = "test@example.com";
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setEmail(email);

        when(profileRepo.findByEmail(email)).thenReturn(profileEntity);

        ProfileEntity result = loginService.getProfile(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    public void testPutProfile() {
        String email = "test@example.com";
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setEmail(email);

        when(profileRepo.save(any(ProfileEntity.class))).thenReturn(profileEntity);

        ProfileEntity result = loginService.putProfile(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    public void testGenerateOtpSendEmail() {
        String email = "test@example.com";
        String otp = "123456";

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setEmail(email);

        when(profileRepo.findByEmail(email)).thenReturn(profileEntity);
        when(emailSender.sendOtpEmail(email)).thenReturn(otp);
        when(profileRepo.save(any(ProfileEntity.class))).thenReturn(profileEntity);

        ProfileEntity result = loginService.generateOtpSendEmail(email);

        assertNotNull(result);
        assertEquals(otp, result.getOtp());
    }

    @Test
    public void testCheckOtpTime() {
        String email = "test@example.com";

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setEmail(email);
        profileEntity.setOtpGeneratedTime(LocalDateTime.now().minusMinutes(2));

        when(profileRepo.findByEmail(email)).thenReturn(profileEntity);

        ProfileEntity result = loginService.checkOtpTime(email);

        assertNotNull(result);
    }

    @Test
    public void testCheckOtpTimeExpired() {
        String email = "test@example.com";

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setEmail(email);
        profileEntity.setOtpGeneratedTime(LocalDateTime.now().minusMinutes(4));

        when(profileRepo.findByEmail(email)).thenReturn(profileEntity);

        ProfileEntity result = loginService.checkOtpTime(email);

        assertNull(result);
    }

    @Test
    public void testMatchOtp() {
        String email = "test@example.com";
        String otp = "123456";

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setEmail(email);
        profileEntity.setOtp(otp);

        when(profileRepo.findByEmail(email)).thenReturn(profileEntity);

        ProfileEntity result = loginService.matchOtp(email, otp);

        assertNotNull(result);
        assertEquals(otp, result.getOtp());
    }

    @Test
    public void testMatchOtpWrongOtp() {
        String email = "test@example.com";
        String otp = "123456";

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setEmail(email);
        profileEntity.setOtp("654321");

        when(profileRepo.findByEmail(email)).thenReturn(profileEntity);

        ProfileEntity result = loginService.matchOtp(email, otp);

        assertNull(result);
    }
}
