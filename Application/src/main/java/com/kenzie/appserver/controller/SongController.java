package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.SongCreateRequest;
import com.kenzie.appserver.controller.model.SongResponse;
import com.kenzie.appserver.controller.model.SongUpdateRequest;
import com.kenzie.appserver.controller.model.helper.HelperSongCreation;
import com.kenzie.appserver.service.SongService;
import com.kenzie.appserver.service.model.Song;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/songs")
public class SongController {
    private SongService songService;

    SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<SongResponse>> getAllSongs() {
        List<Song> songs = songService.getAllSongs();
        // If there are no songs, return a 204
        if (songs == null || songs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Otherwise convert that list of convert of objects into SongResponse objects and return it
        List<SongResponse> response = new ArrayList<>();
        for (Song song : songs) {
            response.add(this.createSongResponse(song));
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<SongResponse> updateSong(@RequestBody SongUpdateRequest songUpdateRequest) {
        Song song = new Song(songUpdateRequest.getTitle(),
                songUpdateRequest.getArtist(),
                songUpdateRequest.getSongLength());
        songService.updateSong(song);

        SongResponse songResponse = createSongResponse(song);

        return ResponseEntity.ok(songResponse);
    }

    @PostMapping
    public ResponseEntity<SongResponse> addNewSong(@RequestBody SongCreateRequest songCreateRequest) {
        Song song = new Song(songCreateRequest.getTitle(),
                songCreateRequest.getArtist(),
                songCreateRequest.getSongLength());
        songService.addNewSong(song);

        SongResponse songResponse = createSongResponse(song);

        return ResponseEntity.created(URI.create("/songs/" + songResponse.getTitle())).body(songResponse);
    }

    @DeleteMapping("/{title}")
    public ResponseEntity deleteBySongTitle(@PathVariable("title") String title) {
        songService.deleteSongByTitle(title);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{title}")
    public ResponseEntity<SongResponse> getSongByTitle(@PathVariable("title") String title) {
        Song song = songService.findBySongTitle(title);
        if (song == null) {
            return ResponseEntity.notFound().build();
        }
        SongResponse songResponse = createSongResponse(song);
        return ResponseEntity.ok(songResponse);
    }

    @GetMapping("/random/{videoLength}")
    public ResponseEntity<List<SongResponse>> grabRandomSong(@PathVariable("videoLength") String videoLength) {
        int videoSeconds = convertLengthToSeconds(videoLength);

        // Now, fill the List with SongResponses until videoSeconds is = or less than 0
        List<SongResponse> results = new ArrayList<>();
        List<Song> songs = songService.getAllSongs();
        HashMap<Integer, Integer> songMap = new HashMap<>();    // To make sure we do not give the same songs more than once
        int lower = 0;
        int upper = songs.size();

        while (videoSeconds > 0) {
            // Grab a random song and make sure it doesn't exist in the HashMap
            int songNumber = 0;
            while (true) {
                songNumber = ThreadLocalRandom.current().nextInt(lower, upper);
                if (!songMap.containsKey(songNumber)) {
                    songMap.put(songNumber, songNumber);
                    break;
                }

                // Allows the algorithm to recycle the songs, so it doesn't loop forever
                if (songMap.size() == songs.size()) {
                    songMap.clear();
                }
            }

            // If song doesn't exist in the SongResponse yet, then
            //      Grab the Song
            Song song = songs.get(songNumber);

            //      Shove the song into the results array
            SongResponse songResponse = createSongResponse(song);
            results.add(songResponse);

            //      Subtract the videoSeconds by the song songLength property
            int songTime = convertLengthToSeconds(song.getSongLength());
            videoSeconds -= songTime;
        }

        return ResponseEntity.ok(results);
    }

    // This is meant only for local testing as an endpoint
    @PostMapping("/sampleSongs")
    public ResponseEntity<List<SongResponse>> createSampleSongs() {
        // Grab the list of songs from the CSV file
        List<Song> songResponse = HelperSongCreation.createSampleSongList();

        List<SongResponse> results = new ArrayList<>();
        for (Song song : songResponse) {
            songService.addNewSong(song);
            results.add(createSongResponse(song));
        }

        // Return it
        return ResponseEntity.ok(results);
    }

    // Helper Methods
    private int convertLengthToSeconds(String length) {
        try {
            String[] array = length.split(":");
            return Integer.parseInt(array[1]) + (Integer.parseInt(array[0]) * 60);
        } catch (Exception e) {
            return 1;
            // Typically, we would want -1 because of error,
            // but returning 1 second allows the program to return one random song still
        }
    }

    private SongResponse createSongResponse(Song song) {
        SongResponse songResponse = new SongResponse();

        songResponse.setTitle(song.getTitle());
        songResponse.setArtist(song.getArtist());
        songResponse.setSongLength(song.getSongLength());

        return songResponse;
    }
}
