package com.example.spebackend.controller;

import com.example.spebackend.dto.SongResponseDto;
import com.example.spebackend.entity.SongEntity;
import com.example.spebackend.service.MusicService;
import com.example.spebackend.dto.SearchSongRequest;
import com.example.spebackend.dto.SongNameId;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@Controller
@CrossOrigin
public class MusicController {
    private final MusicService musicService;

    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    @GetMapping("/song/{id}")
    public ResponseEntity<?> songSend(@PathVariable String id){
        SongEntity song1 = musicService.searchSongId(id);
        SongResponseDto dto = new SongResponseDto();
        dto.setSongName(song1.getSongName());
        dto.setSongImageUrl(song1.getSongImageUrl());
        dto.setSongDownLoadUrl(song1.getSongDownloadLink());

        // Read song file from the file system
        byte[] songData;
        try {
            songData = Files.readAllBytes(new File(song1.getFilePath()).toPath());
            dto.setSongData(Base64.getEncoder().encodeToString(songData));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading song file");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto);
    }

    @PostMapping ("/getSong")
    public ResponseEntity<?> sendSong(@RequestBody SearchSongRequest songRequest){
        SongResponseDto dto = new SongResponseDto();

        songRequest.setSongName(songRequest.getSongName().toLowerCase());

        SongEntity key = musicService.searchKeyword(songRequest.getSongName());
        if(key!=null){

            dto.setSongName(key.getSongName());
            dto.setSongImageUrl(key.getSongImageUrl());
            dto.setSongDownLoadUrl(key.getSongDownloadLink());

            // Read song file from the file system
            byte[] songData;
            try {
                songData = Files.readAllBytes(new File(key.getFilePath()).toPath());
                dto.setSongData(Base64.getEncoder().encodeToString(songData));
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading song file");
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(dto);
        }

        SongNameId songNameId = musicService.sendRequestToSearchSong(songRequest.getSongName());
        if(songNameId==null){
            return ResponseEntity.notFound().build();
        }

        SongEntity song1 = musicService.searchSongId(songNameId.getSongId());
        if(song1!=null){
            dto.setSongName(song1.getSongName());
            dto.setSongImageUrl(song1.getSongImageUrl());
            dto.setSongDownLoadUrl(song1.getSongDownloadLink());

            // Read song file from the file system
            byte[] songData;
            try {
                songData = Files.readAllBytes(new File(song1.getFilePath()).toPath());
                dto.setSongData(Base64.getEncoder().encodeToString(songData));
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading song file");
            }

            musicService.makeKeywordEntry(song1, songRequest.getSongName());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(dto);
        }

        String downloadLink = musicService.getdownloadLink(songNameId.getSongId());
        if(downloadLink==null){
            return ResponseEntity.notFound().build();
        }

        SongEntity songEntity = musicService.saveSong(downloadLink, songNameId);
        if(songEntity==null){
            return ResponseEntity.notFound().build();
        }

        musicService.makeKeywordEntry(songEntity, songRequest.getSongName());

        dto.setSongName(songEntity.getSongName());
        dto.setSongDownLoadUrl(songEntity.getSongDownloadLink());
        dto.setSongImageUrl(songEntity.getSongImageUrl());

        // Read song file from the file system
        byte[] songData;
        try {
            songData = Files.readAllBytes(new File(songEntity.getFilePath()).toPath());
            dto.setSongData(Base64.getEncoder().encodeToString(songData));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading song file");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto);
    }
}
