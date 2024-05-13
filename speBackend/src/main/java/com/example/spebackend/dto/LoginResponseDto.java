package com.example.spebackend.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String songId;
    private String songName;
    private String songImageUrl;
}
