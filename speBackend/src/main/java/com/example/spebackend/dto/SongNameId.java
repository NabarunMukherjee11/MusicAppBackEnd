package com.example.spebackend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Data
public class SongNameId {
    private String songName;
    private String songId;
    private String songImageUrl;

    public void setSongIdFromFirstItem(GetSongIdFromAPIRequest.Item firstItem) {
        // Check if firstItem is not null to avoid NullPointerException
        if (firstItem != null) {
            this.songId = firstItem.getId();
            GetSongIdFromAPIRequest.Thumbnail thumbnail = null;
            if (firstItem != null && firstItem.getThumbnails() != null && firstItem.getThumbnails().length > 0) {
                thumbnail = firstItem.getThumbnails()[0];
            }
            this.songImageUrl = thumbnail.getUrl();
            this.songName = firstItem.getTitle();
        }
    }
}
