package com.kenzie.appserver.service;

import com.amazonaws.services.dynamodbv2.xspec.NULL;
import com.kenzie.appserver.controller.model.SongResponse;
import com.kenzie.appserver.repositories.SongRepository;
import com.kenzie.appserver.repositories.model.SongRecord;
import com.kenzie.appserver.service.model.Song;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class SongServiceTest {
    private SongRepository songRepository;
    private SongService songService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    @BeforeEach
    void setup(){
        songRepository = mock(SongRepository.class);
        songService = new SongService(songRepository);
    }

    @Test
    public void getAllSongs_correctlyRequestSongs_HappyCase(){
        //GIVEN
        // --- First SongRecord Object for test
        String title = mockNeat.strings().get();
        String artist = mockNeat.strings().get();
        String songLength = mockNeat.strings().get();

        SongRecord record = new SongRecord();
        record.setTitle(title);
        record.setArtist(artist);
        record.setSongLength(songLength);

        // --- Second SongRecord Object for test
        String title1 = mockNeat.strings().get();
        String artist1 = mockNeat.strings().get();
        String songLength1 = mockNeat.strings().get();

        SongRecord record1 = new SongRecord();
        record1.setTitle(title1);
        record1.setArtist(artist1);
        record1.setSongLength(songLength1);

        List<SongRecord> records = new ArrayList<>();

        records.add(record);
        records.add(record1);

        //WHEN
        when(songRepository.findAll()).thenReturn(records);
        List<Song> songList = songService.getAllSongs();

        //THEN
        Assertions.assertNotNull(songList,"the song list is returned");
        Assertions.assertEquals(2,records.size(),"there are two songs");

        for(Song song : songList){
            if(song.getTitle().equals(record.getTitle())){

                Assertions.assertEquals(record.getArtist(), song.getArtist(), "The artists match");
                Assertions.assertEquals(record.getSongLength(), song.getSongLength(), "Song length matches");

            }else if (song.getTitle().equals(record1.getTitle())){

                Assertions.assertEquals(record1.getArtist(), song.getArtist(), "The artists match");
                Assertions.assertEquals(record1.getSongLength(), song.getSongLength(), "Song length matches");

            }else{
                Assertions.assertTrue(false,"song that was returned was not in records");
            }
        }
    }

    @Test
    public void deleteSong_correctlyDeletesSong_happyCase(){
        //GIVEN
        String title = mockNeat.strings().get();
        String artist = mockNeat.strings().get();
        String songLength = "0:50";

        Song song = new Song(title,artist,songLength);
        songService.addNewSong(song);

        //WHEN
        songService.deleteSongByTitle(song.getTitle());

        //THEN
        assertThrows(NullPointerException.class,() -> songService.findBySongTitle(song.getTitle()));

    }

    @Test
    public void findSongByTitle_validSongInput_songIsFound_happyCase(){
        //GIVEN
        String title = mockNeat.strings().get();
        String artist = mockNeat.strings().get();
        String songLength = mockNeat.strings().get();

        SongRecord songRecord = new SongRecord();
        songRecord.setTitle(title);
        songRecord.setArtist(artist);
        songRecord.setSongLength(songLength);

        ArrayList<SongRecord> list = new ArrayList<>();
        list.add(songRecord);
        Iterable<SongRecord> iterator = list;
        when(songRepository.findAll()).thenReturn(iterator);

        Song song = new Song(title,artist,songLength);

        //WHEN
        Song returnedSong = songService.findBySongTitle(title);


        //THEN
        Assertions.assertNotNull(returnedSong,"the song is returned");
        Assertions.assertEquals(returnedSong.getTitle(),song.getTitle(),"titles match");
       Assertions.assertEquals(returnedSong.getArtist(), song.getArtist(), "The artists match");
       Assertions.assertEquals(returnedSong.getSongLength(), song.getSongLength(), "Song length matches");

    }
    @Test
    public void findSongByTitle_emptyRepoSongInput_returnsNull(){
        //GIVEN
        String title = mockNeat.strings().get();
        String artist = mockNeat.strings().get();
        String songLength = mockNeat.strings().get();

        SongRecord songRecord = new SongRecord();
        songRecord.setTitle(title);
        songRecord.setArtist(artist);
        songRecord.setSongLength(songLength);

        ArrayList<SongRecord> list = new ArrayList<>();
        // Do not add anything to the list!

        Iterable<SongRecord> iterator = list;
        when(songRepository.findAll()).thenReturn(iterator);

        Song song = new Song(title,artist,songLength);

        //WHEN
        Song returnedSong = songService.findBySongTitle(title);


        //THEN
        Assertions.assertNull(returnedSong,"the song is returned");

    }

    @Test
    public void updateSong_correctlyUpdatesExistingSong_happyCase(){

        //GIVEN
        String title = mockNeat.strings().get();
        String artist = mockNeat.strings().get();
        String genre = mockNeat.strings().get();
        String songLength = mockNeat.strings().get();

        SongRecord songRecord = new SongRecord();
        songRecord.setTitle(title);
        songRecord.setArtist("dan");
        songRecord.setGenre(genre);
        songRecord.setSongLength(songLength);
        songRecord.setFavorited(true);

        ArrayList<SongRecord> list = new ArrayList<>();
        list.add(songRecord);
        Iterable<SongRecord> iterator = list;
        when(songRepository.findAll()).thenReturn(iterator);

        Song song = new Song(title,artist,songLength);

        songService.addNewSong(song);

        Song song2 = new Song(title,"dan",songLength);

        //WHEN
        songService.updateSong(song2);

        Song returnedSong = songService.findBySongTitle(song.getTitle());

        //THEN
        Assertions.assertEquals(returnedSong,song2);

    }

    @Test
    public void addNewSong_songGetAdded_happyCase(){
        //GIVEN
        String title = mockNeat.strings().get();
        String artist = mockNeat.strings().get();
        String genre = mockNeat.strings().get();
        String songLength = mockNeat.strings().get();

        Song song = new Song(title,artist,songLength);

        ArgumentCaptor<SongRecord> songRecordCaptor = ArgumentCaptor.forClass(SongRecord.class);

        //WHEN
        Song returnedSong = songService.addNewSong(song);

        // THEN
        Assertions.assertNotNull(returnedSong);

        verify(songRepository).save(songRecordCaptor.capture());


    }

}
