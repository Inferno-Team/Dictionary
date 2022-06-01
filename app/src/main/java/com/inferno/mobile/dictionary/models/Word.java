package com.inferno.mobile.dictionary.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Word implements Serializable, Comparable<Word> {
    private final int id;

    public void setEng(String eng) {
        this.eng = eng;
    }

    private String eng;
    @SerializedName("ara")
    private final String ar;
    private boolean isLiked = false;

    public Word(int id, String eng, String ar) {
        this.id = id;
        this.eng = eng;
        this.ar = ar;
    }

    public int getId() {
        return id;
    }

    public String getEng() {
        return eng;
    }

    public String getAr() {
        return ar;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isLiked() {
        return isLiked;
    }

    @Override
    public int compareTo(Word o) {
        return (o.eng.charAt(0) > this.eng.charAt(0)) ? -1 : 0;
    }
}
