package com.example.spebackend.service;

import com.example.spebackend.dto.GetDownloadLinkFromAPIRequest;
import com.example.spebackend.dto.GetSongIdFromAPIRequest;
import com.example.spebackend.dto.SongNameId;
import com.example.spebackend.entity.KeywordEntity;
import com.example.spebackend.entity.SongEntity;
import com.example.spebackend.repository.KeywordRepository;
import com.example.spebackend.repository.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MusicServiceTest {

    @InjectMocks
    private MusicService musicService;

    @Mock
    private KeywordRepository keywordRepository;

    @Mock
    private SongRepository songRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendRequestToSearchSong() {
        String keyword = "test song";
        String searchURL = "https://youtube-media-downloader.p.rapidapi.com/v2/search/videos";

        GetSongIdFromAPIRequest apiResponse = new GetSongIdFromAPIRequest();
        GetSongIdFromAPIRequest.Item item = new GetSongIdFromAPIRequest.Item();
        item.setId(UUID.randomUUID().toString());
        apiResponse.setItems(new GetSongIdFromAPIRequest.Item[]{item});

        ResponseEntity<GetSongIdFromAPIRequest> responseEntity = ResponseEntity.ok(apiResponse);

        when(restTemplate.exchange(any(RequestEntity.class), eq(GetSongIdFromAPIRequest.class)))
                .thenReturn(responseEntity);

        SongNameId songNameId = musicService.sendRequestToSearchSong(keyword);

        assertNotNull(songNameId);
        assertEquals(item.getId(), songNameId.getSongId());
    }

    @Test
    public void testGetDownloadLink() {
        String songId = "testSongId";
        String downloadURL = "https://youtube-mp36.p.rapidapi.com/dl";

        GetDownloadLinkFromAPIRequest apiResponse = new GetDownloadLinkFromAPIRequest();
        apiResponse.setLink("downloadLink");

        ResponseEntity<GetDownloadLinkFromAPIRequest> responseEntity = ResponseEntity.ok(apiResponse);

        when(restTemplate.exchange(any(RequestEntity.class), eq(GetDownloadLinkFromAPIRequest.class)))
                .thenReturn(responseEntity);

        String downloadLink = musicService.getdownloadLink(songId);

        assertNotNull(downloadLink);
        assertEquals("downloadLink", downloadLink);
    }

    @Test
    public void testSaveSong() {
        String downloadLink = "downloadLink";
        SongNameId songNameId = new SongNameId();
        songNameId.setSongName("SongName");
        songNameId.setSongId("SongId");
        songNameId.setSongImageUrl("imageUrl");

        byte[] audioData = new byte[]{1, 2, 3, 4};

        when(restTemplate.getForEntity(downloadLink, byte[].class))
                .thenReturn(ResponseEntity.ok(audioData));

        SongEntity songEntity = musicService.saveSong(downloadLink, songNameId);

        assertNotNull(songEntity);
        assertEquals("SongName", songEntity.getSongName());
        assertEquals("SongId", songEntity.getSongId());
        assertEquals("imageUrl", songEntity.getSongImageUrl());
        assertNotNull(songEntity.getFilePath());

        verify(songRepository, times(1)).save(songEntity);
    }

    @Test
    public void testSearchKeyword() {
        String songName = "testSong";

        KeywordEntity keywordEntity = new KeywordEntity();
        keywordEntity.setKeyword(songName);

        SongEntity songEntity = new SongEntity();
        keywordEntity.setSong(songEntity);

        when(keywordRepository.findByKeyword(songName)).thenReturn(keywordEntity);

        SongEntity result = musicService.searchKeyword(songName);

        assertNotNull(result);
        assertEquals(songEntity, result);
    }

    @Test
    public void testSearchSongId() {
        String songId = "testSongId";

        SongEntity songEntity = new SongEntity();
        songEntity.setSongId(songId);

        when(songRepository.finBySongId(songId)).thenReturn(songEntity);

        SongEntity result = musicService.searchSongId(songId);

        assertNotNull(result);
        assertEquals(songEntity, result);
    }
}
