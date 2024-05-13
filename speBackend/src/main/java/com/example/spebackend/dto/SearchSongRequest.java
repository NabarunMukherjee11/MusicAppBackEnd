package com.example.spebackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SearchSongRequest {
    @JsonProperty("songName")
    String songName;
}
