package org.mikuclub.app.javaBeans.parameters;

import org.mikuclub.app.javaBeans.parameters.base.BaseParameters;
import org.mikuclub.app.utils.DataUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

public class UpdatePostParameters extends BaseParameters
{
        private Integer id;
        private Date date;
        private Date date_gmt;
        private String status;
        private String title;
        private String content;
        private Integer author;
        private Integer featured_media;
        private Boolean sticky;
        private ArrayList<Integer> categories;
        private ArrayList<Integer> tags;



        public Map<String, Object> toMap()
        {
                Map<String, Object> outputMap = new HashMap();

                putIfNotNull(outputMap, "id", id);
                putIfNotNull(outputMap, "date", DataUtils.dateToString(date));
                putIfNotNull(outputMap, "date_gmt", DataUtils.dateToString(date_gmt));
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

        public Integer getId()
        {
                return id;
        }

        public void setId(Integer id)
        {
                this.id = id;
        }

        public Date getDate()
        {
                return date;
        }

        public void setDate(Date date)
        {
                this.date = date;
        }

        public Date getDate_gmt()
        {
                return date_gmt;
        }

        public void setDate_gmt(Date date_gmt)
        {
                this.date_gmt = date_gmt;
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
