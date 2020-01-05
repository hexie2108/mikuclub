package org.mikuclub.app.javaBeans.parameters;

import org.mikuclub.app.utils.DataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

public class CreatePostParameters extends BaseParameters
{

        private String status;
        private String title;
        private String content;
        private Integer author;
        private Integer featured_media;
        private Boolean sticky;
        private ArrayList<Integer> categories;
        private ArrayList<Integer> tags;



        public Map<String, String> toMap()
        {
                Map<String, String> outputMap = new HashMap<String, String>();

                putIfNotNull(outputMap, "status", status);
                putIfNotNull(outputMap, "title", title);
                putIfNotNull(outputMap, "content", content);
                putIfNotNull(outputMap, "author", author);
                putIfNotNull(outputMap, "featured_media", featured_media);
                putIfNotNull(outputMap, "sticky", sticky);
                putIfNotNull(outputMap, "categories", DataUtils.arrayListToString(categories, "", ","));
                putIfNotNull(outputMap, "tags", DataUtils.arrayListToString(tags, "", ","));

                return outputMap;

        }





        public String getStatus()
        {
                return status;
        }

        public void setStatus(String status)
        {
                this.status = status;
        }

        public String getTitle()
        {
                return title;
        }

        public void setTitle(String title)
        {
                this.title = title;
        }

        public String getContent()
        {
                return content;
        }

        public void setContent(String content)
        {
                this.content = content;
        }

        public Integer getAuthor()
        {
                return author;
        }

        public void setAuthor(Integer author)
        {
                this.author = author;
        }

        public Integer getFeatured_media()
        {
                return featured_media;
        }

        public void setFeatured_media(Integer featured_media)
        {
                this.featured_media = featured_media;
        }

        public Boolean getSticky()
        {
                return sticky;
        }

        public void setSticky(Boolean sticky)
        {
                this.sticky = sticky;
        }

        public ArrayList<Integer> getCategories()
        {
                return categories;
        }

        public void setCategories(ArrayList<Integer> categories)
        {
                this.categories = categories;
        }

        public ArrayList<Integer> getTags()
        {
                return tags;
        }

        public void setTags(ArrayList<Integer> tags)
        {
                this.tags = tags;
        }
}
