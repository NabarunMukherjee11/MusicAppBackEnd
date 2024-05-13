package com.example.spebackend.dto;

import lombok.Data;

@Data
public class VerifyEmailRequest {
    private String email;
    private String otp;
}
