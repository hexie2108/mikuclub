package org.mikuclub.app.javaBeans.resources.modules;

import com.google.gson.annotations.SerializedName;

public class LinksForPost
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
