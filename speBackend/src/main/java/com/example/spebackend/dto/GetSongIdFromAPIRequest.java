package com.example.spebackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetSongIdFromAPIRequest {
    private boolean status;
    private String nextToken;
    private Item[] items;

    @Data
    public static class Item {
        private String type;
        private String id;
        private String title;
        private String description;
        private Channel channel;
        private boolean isLiveNow;
        private String lengthText;
        private String viewCountText;
        private String publishedTimeText;
        private Thumbnail[] thumbnails;
    }

    @Data
    public static class Channel {
        private String type;
        private String id;
        private String name;
        private boolean isVerified;
        private boolean isVerifiedArtist;
        private Avatar[] avatar;
    }

    @Data
    public static class Avatar {
        private String url;
        private int width;
        private int height;
    }

    @Data
    public static class Thumbnail {
        private String url;
        private int width;
        private int height;
        private boolean moving;
    }
}
