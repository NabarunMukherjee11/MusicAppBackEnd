package com.example.spebackend.service;

import com.example.spebackend.dto.GetDownloadLinkFromAPIRequest;
import com.example.spebackend.dto.GetSongIdFromAPIRequest;
import com.example.spebackend.repository.KeywordRepository;
import com.example.spebackend.repository.SongRepository;
import com.example.spebackend.dto.SongNameId;
import com.example.spebackend.entity.KeywordEntity;
import com.example.spebackend.entity.SongEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@CrossOrigin
public class MusicService {
    private final KeywordRepository keywordRepository;
     private final SongRepository songRepository;

    public MusicService(KeywordRepository keywordRepository, SongRepository songRepository) {
        this.keywordRepository = keywordRepository;
        this.songRepository = songRepository;
    }


    public SongNameId sendRequestToSearchSong(String keyword){
        RestTemplate restTemplate = new RestTemplate();
        String searchURL = "https://youtube-media-downloader.p.rapidapi.com/v2/search/videos";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(searchURL)
                .queryParam("keyword", keyword);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-RapidAPI-Key", "ce7040a7a8mshabb604f617248b8p130edejsn252944e8fe22");
        headers.add("X-RapidAPI-Host", "youtube-media-downloader.p.rapidapi.com");
        headers.set("Accept", "application/json");

        RequestEntity<Void> requestEntity = RequestEntity.get(builder.build().toUri())
                .headers(headers)
                .build();

        ResponseEntity<GetSongIdFromAPIRequest> responseEntity = restTemplate.exchange(
                requestEntity,
                GetSongIdFromAPIRequest.class);

        GetSongIdFromAPIRequest responseBody = responseEntity.getBody();

        SongNameId songNameId = new SongNameId();

        GetSongIdFromAPIRequest.Item firstItem = null;
        if (responseBody != null && responseBody.getItems() != null && responseBody.getItems().length > 0) {
            firstItem = responseBody.getItems()[0];
        }

        songNameId.setSongIdFromFirstItem(firstItem);
        return songNameId;
    }

    public String getdownloadLink(String id){
        RestTemplate restTemplate = new RestTemplate();
        String downloadURL = "https://youtube-mp36.p.rapidapi.com/dl";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(downloadURL).
                queryParam("id",id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-RapidAPI-Key", "ce7040a7a8mshabb604f617248b8p130edejsn252944e8fe22");
        headers.add("X-RapidAPI-Host", "youtube-mp36.p.rapidapi.com");
        headers.set("Accept", "application/json");

        RequestEntity<Void> requestEntity = RequestEntity.get(builder.build().toUri())
                .headers(headers)
                .build();

        ResponseEntity<GetDownloadLinkFromAPIRequest> responseEntity = restTemplate.exchange(
                requestEntity,
                GetDownloadLinkFromAPIRequest.class);

        GetDownloadLinkFromAPIRequest responseBody = responseEntity.getBody();
        assert responseBody != null;
        return responseBody.getLink();
    }

    public SongEntity saveSong(String downloadLink, SongNameId songNameId) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(downloadLink, byte[].class);
        byte[] audioData = responseEntity.getBody();

        if (audioData == null || audioData.length == 0){
            return null;
        }

        String fileName = "song_" + UUID.randomUUID().toString() + ".mp3";
        String filePath = "E:/songs/" + fileName;

        try {
            // Save the audio data to the filesystem
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(audioData);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Handle the error appropriately
        }

        // Create a new SongEntity and set the file path instead of the audio data
        SongEntity newSong = new SongEntity();
        newSong.setSongName(songNameId.getSongName());
        newSong.setSongId(songNameId.getSongId());
        newSong.setSongDownloadLink(downloadLink);
        newSong.setSongImageUrl(songNameId.getSongImageUrl());
        newSong.setFilePath(filePath); // Set the file path instead of the audio data

        songRepository.save(newSong);
        return newSong;
    }

    public SongEntity searchKeyword(String songName) {
        KeywordEntity keyword = keywordRepository.findByKeyword(songName);
        if(keyword==null){
            return null;
        }
        return keyword.getSong();
    }

    public SongEntity searchSongId(String songId) {
        return songRepository.finBySongId(songId);
    }

    public void makeKeywordEntry(SongEntity song1, String songName) {
        KeywordEntity keyword = new KeywordEntity();
        keyword.setKeyword(songName);
        keyword.setSong(song1);
    }

    public List<SongEntity> getAllSongs() {
        return songRepository.findAll();
    }
}
