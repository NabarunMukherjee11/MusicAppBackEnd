package com.example.spebackend.controller;

import com.example.spebackend.dto.SearchSongRequest;
import com.example.spebackend.dto.SongResponseDto;
import com.example.spebackend.entity.SongEntity;
import com.example.spebackend.service.MusicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class MusicControllerTest {

    @InjectMocks
    private MusicController musicController;

    @Mock
    private MusicService musicService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSongSend() throws IOException {
        String songId = "1";

        SongEntity songEntity = new SongEntity();
        songEntity.setSongId(songId);
        songEntity.setSongName("Song1");
        songEntity.setSongImageUrl("imageUrl");
        songEntity.setFilePath("path/to/song.mp3");

        when(musicService.searchSongId(songId)).thenReturn(songEntity);

        byte[] songData = Files.readAllBytes(new File(songEntity.getFilePath()).toPath());
        String encodedSongData = java.util.Base64.getEncoder().encodeToString(songData);

        ResponseEntity<?> response = musicController.songSend(songId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        SongResponseDto responseDto = (SongResponseDto) response.getBody();
        assertEquals("Song1", responseDto.getSongName());
        assertEquals("imageUrl", responseDto.getSongImageUrl());
        assertEquals(encodedSongData, responseDto.getSongData());
    }

    @Test
    public void testSendSong() throws IOException {
        SearchSongRequest request = new SearchSongRequest();
        request.setSongName("song1");

        SongEntity songEntity = new SongEntity();
        songEntity.setSongName("Song1");
        songEntity.setSongImageUrl("imageUrl");
        songEntity.setSongDownloadLink("downloadLink");
        songEntity.setFilePath("path/to/song.mp3");

        when(musicService.searchKeyword(request.getSongName().toLowerCase())).thenReturn(songEntity);

        byte[] songData = Files.readAllBytes(new File(songEntity.getFilePath()).toPath());
        String encodedSongData = java.util.Base64.getEncoder().encodeToString(songData);

        ResponseEntity<?> response = musicController.sendSong(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        SongResponseDto responseDto = (SongResponseDto) response.getBody();
        assertEquals("Song1", responseDto.getSongName());
        assertEquals("imageUrl", responseDto.getSongImageUrl());
        assertEquals(encodedSongData, responseDto.getSongData());
    }

    @Test
    public void testSendSongNotFound() {
        SearchSongRequest request = new SearchSongRequest();
        request.setSongName("unknownSong");

        when(musicService.searchKeyword(request.getSongName().toLowerCase())).thenReturn(null);

        ResponseEntity<?> response = musicController.sendSong(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
