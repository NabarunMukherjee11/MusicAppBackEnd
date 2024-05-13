package com.example.spebackend.dto;

import lombok.Data;

@Data
public class GetDownloadLinkFromAPIRequest {
    private String link;
    private String title;
    private long filesize;
    private double progress;
    private double duration;
    private String status;
    private String msg;
}
