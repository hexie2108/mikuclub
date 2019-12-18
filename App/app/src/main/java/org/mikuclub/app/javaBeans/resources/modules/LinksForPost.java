package org.mikuclub.app.javaBeans.resources.modules;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LinksForPost implements Serializable

{
        private Href self;
        private Href collection;
        private Href about;
        private Href author;
        private Href replies;
        @SerializedName("wp:featuredmedia")
        private Href featuredmedia;

        @SerializedName("wp:attachment")
        private Href attachment;

        @SerializedName("wp:term")
        private Href term;


}
