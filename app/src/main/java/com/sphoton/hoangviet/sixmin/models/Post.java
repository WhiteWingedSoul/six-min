package com.sphoton.hoangviet.sixmin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hoangviet on 10/11/16.
 */
public class Post implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;
    @SerializedName("title")
    private String title;
    @SerializedName("cover")
    private String coverLink;
    @SerializedName("content")
    private List<Sentence> content;
    @SerializedName("vocabulary")
    private List<Vocabulary> vocabularyList;
    @SerializedName("audio")
    private String audioLink;
    @SerializedName("date")
    private String date;
    private boolean isDownloaded;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverLink() {
        return coverLink;
    }

    public void setCoverLink(String coverLink) {
        this.coverLink = coverLink;
    }

    public List<Sentence> getContent() {
        return content;
    }

    public void setContent(List<Sentence> content) {
        this.content = content;
    }

    public List<Vocabulary> getVocabularyList() {
        return vocabularyList;
    }

    public void setVocabularyList(List<Vocabulary> vocabularyList) {
        this.vocabularyList = vocabularyList;
    }

    public String getAudioLink() {
        return audioLink;
    }

    public void setAudioLink(String audioLink) {
        this.audioLink = audioLink;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }
}
