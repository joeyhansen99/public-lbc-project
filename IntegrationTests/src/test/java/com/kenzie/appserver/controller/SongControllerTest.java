package com.kenzie.appserver.controller;

import com.kenzie.appserver.IntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.controller.model.SongCreateRequest;
import com.kenzie.appserver.controller.model.SongUpdateRequest;
import com.kenzie.appserver.service.SongService;
import com.kenzie.appserver.service.model.Song;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
class SongControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    SongService songService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getAllSongs() throws Exception {
        //GIVEN
        String title = mockNeat.strings().get();
        String artist = mockNeat.strings().get();
        String songLength = mockNeat.strings().get();

        Song song = new Song(title,artist,songLength);

        String title1 = mockNeat.strings().get();
        String artist1 = mockNeat.strings().get();
        String songLength1 = mockNeat.strings().get();

        //WHEN
        Song song1 = new Song(title1,artist1,songLength1);
        Song test = songService.addNewSong(song);
        Song test1 = songService.addNewSong(song1);

        //THEN
        mvc.perform(get("/songs/all"))
                .andExpect(status().isOk());

    }

    @Test
    public void getByTitle_Exists() throws Exception {
        //GIVEN
        String title = mockNeat.strings().get();
        String artist = mockNeat.strings().get();
        String songLength = mockNeat.strings().get();

        //WHEN
        Song song = new Song(title,artist,songLength);
        Song test = songService.addNewSong(song);

        //THEN
        mvc.perform(get("/songs/{title}", test.getTitle())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("artist")
                        .value(is(artist)))
                .andExpect(jsonPath("title")
                        .value(is(title)))
                .andExpect(jsonPath("songLength")
                        .value(is(songLength)))
                .andExpect(status().isOk());
    }

    @Test
    public void getSongByTitle_songDoesNotExist() throws Exception {
        // GIVEN
       String title = mockNeat.strings().get();
        // WHEN
        mvc.perform(get("/song/{title}", title)
                        .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isNotFound());
    }

    @Test
    public void addNewSong_AddSuccessful() throws Exception {
        //GIVEN
        String name = mockNeat.strings().valStr();

        //WHEN
        SongCreateRequest exampleCreateRequest = new SongCreateRequest();
        exampleCreateRequest.setTitle(name);

        //THEN
        mvc.perform(post("/songs/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(exampleCreateRequest)))
                .andExpect(jsonPath("title")
                        .value(is(name)))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateSong_PutSuccessful() throws Exception {
        // GIVEN
        String title = mockNeat.strings().get();
        String artist = mockNeat.strings().get();
        String songLength = mockNeat.strings().get();

        Song song = new Song(title,artist,songLength);
        Song test = songService.addNewSong(song);

        SongUpdateRequest songUpdateRequest = new SongUpdateRequest();
        songUpdateRequest.setTitle(title);
        songUpdateRequest.setArtist(artist);
        songUpdateRequest.setSongLength(songLength);

        // WHEN
        mvc.perform(put("/songs")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(songUpdateRequest)))
                // THEN
                .andExpect(jsonPath("title")
                        .value(is(title)))
                .andExpect(jsonPath("artist")
                        .value(is(artist)))
                .andExpect(jsonPath("songLength")
                        .value(is(songLength)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteSong_DeleteSuccessful() throws Exception {

        // GIVEN
        String title = mockNeat.strings().get();
        String artist = mockNeat.strings().get();
        String genre = mockNeat.strings().get();
        String songLength = mockNeat.strings().get();

        Song song = new Song(title,artist,songLength);
        Song test = songService.addNewSong(song);

        // WHEN
        mvc.perform(delete("/songs/{title}", test.getTitle())
                        .accept(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isNoContent());
        assertThat(songService.findBySongTitle(title)).isNull();

    }

    @Test
    public void getRandomSong_randomSongReturned () throws Exception {

        //Given
        //Song 1
        String title1 = mockNeat.strings().get();
        String artist1 = mockNeat.strings().get();
        String songLength1 = mockNeat.strings().get();
        String videoLength = "test";

        Song song1 = new Song(title1,artist1,songLength1);
        Song test1 = songService.addNewSong(song1);

        //song2

        String title2= mockNeat.strings().get();
        String artist2 = mockNeat.strings().get();
        String songLength2 = mockNeat.strings().get();

        Song song2 = new Song(title2,artist2,songLength2);
        Song test2 = songService.addNewSong(song2);

        //song3
        String title3 = mockNeat.strings().get();
        String artist3 = mockNeat.strings().get();
        String songLength3 = mockNeat.strings().get();


        //WHEN
        Song song3 = new Song(title3,artist3,songLength3);
        Song test3 = songService.addNewSong(song3);

        //THEN
        mvc.perform(get("/songs/random/"+videoLength))
                .andExpect(status().isOk());


    }

    @Test
    public void createSampleSong() throws Exception {

        mvc.perform(post("/songs/sampleSongs"))
                .andExpect(status().isOk());

    }


}
