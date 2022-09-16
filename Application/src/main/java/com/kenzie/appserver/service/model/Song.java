package com.kenzie.appserver.service.model;

import java.util.Objects;

public class Song {
    private final String title;
    private final String artist;
    private final String songLength;

    public Song (String title, String artist, String songLength) {
        this.title = title;
        this.artist = artist;
        this.songLength = songLength;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }


    public String getSongLength() {
        return songLength;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return title.equals(song.title) && artist.equals(song.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist);
    }
}
