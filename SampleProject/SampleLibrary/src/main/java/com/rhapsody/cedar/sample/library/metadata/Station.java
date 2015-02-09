package com.rhapsody.cedar.sample.library.metadata;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Station implements Serializable {

    @SerializedName("id")
    public final String id;
    @SerializedName("name")
    public final String name;
    @SerializedName("summary")
    public final String summary;
    @SerializedName("type")
    public final String type;
    @SerializedName("artists")
    public final String artists;
    @SerializedName("description")
    public final String description;
    @SerializedName("images") public final ImageUrls imageUrls;

    public Station(String id, String name, String type, String artists, String description, String summary, ImageUrls imageUrls) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.type = type;
        this.artists = artists;
        this.description = description;
        this.imageUrls = imageUrls;
    }

    public static class ImageUrls implements Serializable {

        @SerializedName("medium")
        public final String medium;
        @SerializedName("large")
        public final String large;

        public ImageUrls(String medium, String large) {
            this.medium = medium;
            this.large = large;
        }

    }
}
