package com.example.spebackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SongResponseDto {
    private String songName;
    private String songDownLoadUrl;
    private String songImageUrl;
    private String songData;
}

