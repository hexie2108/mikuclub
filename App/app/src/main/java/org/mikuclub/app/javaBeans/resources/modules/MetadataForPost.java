package org.mikuclub.app.javaBeans.resources.modules;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MetadataForPost implements Serializable
{
        private List<Integer> views;

        @SerializedName("_thumbnail_img_src")
        private List<String> thumbnail_img_src;

        public List<String> getThumbnail_img_src()
        {
                return thumbnail_img_src;
        }

        public void setThumbnail_img_src(List<String> thumbnail_img_src)
        {
                this.thumbnail_img_src = thumbnail_img_src;
        }

        public List<Integer> getViews()
        {
                return views;
        }

        public void setViews(List<Integer> views)
        {
                this.views = views;
        }
}
