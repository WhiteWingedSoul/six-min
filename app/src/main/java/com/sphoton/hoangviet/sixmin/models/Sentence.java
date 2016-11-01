package com.sphoton.hoangviet.sixmin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by hoangviet on 10/30/16.
 */

public class Sentence implements Serializable{
    @SerializedName("p")
    private String speaker;
    @SerializedName("s")
    private String sentence;

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
}
