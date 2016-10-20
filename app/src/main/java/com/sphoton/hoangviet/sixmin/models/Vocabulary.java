package com.sphoton.hoangviet.sixmin.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by hoangviet on 10/11/16.
 */
public class Vocabulary implements Serializable {
    @SerializedName("w")
    private String word;
    @SerializedName("m")
    private String meaning;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
