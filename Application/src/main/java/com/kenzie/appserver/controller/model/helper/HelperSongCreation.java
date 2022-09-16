package com.kenzie.appserver.controller.model.helper;

import com.kenzie.appserver.controller.model.SongResponse;
import com.kenzie.appserver.service.model.Song;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelperSongCreation {
    public static void main(String[] args){
        List<Song> results = createSampleSongList();
        System.out.println(results );
    }
    // Static method returns a List of SongResponses for the sample
    public static List<Song> createSampleSongList() {
        // Get the parsed out song data
        ArrayList<String[]> songData = getSongLinesFromCSV();

        // Turn it into SongResponses
        List<Song> results = new ArrayList<>();

        int i = 0;

        for(String[] entry : songData){
            if(i != 0){
                String title = entry[0];
                title = title.replaceAll("^\"|\"$", "");

                String artist = entry[1];
                artist = artist.replaceAll("^\"|\"$", "");

                String songLength = entry[4];
                songLength = songLength.replaceAll("^\"|\"$", "");

                Song song = new Song(title, artist, songLength);
                results.add(song);
            }
            i++;
        }

        // Return it
        return results;
    }

    // Helper methods
    private static ArrayList<String[]> getSongLinesFromCSV() {
        ArrayList<String[]> results = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("Application/src/main/java/com/kenzie/appserver/controller/model/helper/results.csv"));

            String line;
            while ((line = reader.readLine()) != null){
                results.add(line.split(","));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
}
