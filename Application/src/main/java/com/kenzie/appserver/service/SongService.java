package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.SongResponse;
import com.kenzie.appserver.repositories.SongRepository;
import com.kenzie.appserver.repositories.model.SongRecord;
import com.kenzie.appserver.service.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SongService {
    private SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        Iterable<SongRecord> songIterator = songRepository.findAll();
        for (SongRecord song : songIterator) {
            songs.add(new Song(song.getTitle(), song.getArtist(), song.getSongLength()));
        }
        return songs;
    }
    public void deleteSongByTitle(String title) {
        songRepository.deleteById(title);

    }

    public Song findBySongTitle (String title) {
        Iterable<SongRecord> songIterator = songRepository.findAll();

        for(SongRecord song : songIterator){
            if(song.getTitle().equals(title)){
                return new Song(song.getTitle(),song.getArtist(), song.getSongLength());
            }
        }
        // If it gets here, that means the repo did not have the song.
        return null;
    }

    public void updateSong(Song song) {
        if (songRepository.existsById(song.getTitle())) {
            songRepository.save(createSongRecord(song));
        }
    }

    public Song addNewSong (Song song) {
        songRepository.save(createSongRecord(song));
        return song;
    }

    private SongRecord createSongRecord(Song song) {
        SongRecord songRecord = new SongRecord();
        songRecord.setSongLength(song.getSongLength());
        songRecord.setArtist(song.getArtist());
        songRecord.setTitle(song.getTitle());
        return songRecord;
    }
    private SongResponse createSongResponse(Song song) {
        SongResponse songResponse = new SongResponse();
        songResponse.setTitle(song.getTitle());
        songResponse.setArtist(song.getArtist());
        songResponse.setSongLength(song.getSongLength());

        return songResponse;
    }
}
