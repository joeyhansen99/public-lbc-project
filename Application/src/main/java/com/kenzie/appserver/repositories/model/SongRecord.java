package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.util.Objects;

@DynamoDBTable(tableName = "songs-table")
public class SongRecord {
    private String title;
    private String artist;
    private String genre;
    private String songLength;
    private boolean isFavorited;

    // Getters & Setter methods
    @DynamoDBHashKey(attributeName = "title")
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @DynamoDBAttribute(attributeName = "artist")
    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    @DynamoDBAttribute(attributeName = "genre")
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    @DynamoDBAttribute(attributeName = "songLength")
    public String getSongLength() {
        return songLength;
    }

    public void setSongLength(String songLength) {
        this.songLength = songLength;
    }
    @DynamoDBAttribute(attributeName = "isFavorited")
    public boolean isFavorited() {
        return isFavorited;
    }

    public void setFavorited(boolean isFavorited) {
       this.isFavorited = isFavorited;
    }

    // Equal and Hash methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SongRecord)) return false;
        SongRecord that = (SongRecord) o;
        return getTitle().equals(that.getTitle()) && getArtist().equals(that.getArtist()) && getSongLength().equals(that.getSongLength());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getArtist(), getSongLength());
    }
}
