package org.mikuclub.app.javaBeans.resources;


import com.google.gson.annotations.SerializedName;


import org.mikuclub.app.javaBeans.resources.modules.Rendered;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Post implements Serializable
{
        private Date date;
        private Date date_gmt;
        private Rendered guid;
        private int id;
        private String link;
        private Date modified;
        private Date modified_gmt;
        private String slug;
        private String status;
        private String type;
        private String password;
        private Rendered title;
        private Rendered content;
        private int author;
        private Rendered excerpt;
        private int featured_media;
        private String comment_status;
        private String ping_status;
        private String format;
        private MetadataForPost metadata;
        private Boolean sticky;
        private String template;
        private List<Integer> categories;
        private List<Integer> tags;

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

        public Rendered getGuid()
        {
                return guid;
        }

        public void setGuid(Rendered guid)
        {
                this.guid = guid;
        }

        public int getId()
        {
                return id;
        }

        public void setId(int id)
        {
                this.id = id;
        }

        public String getLink()
        {
                return link;
        }

        public void setLink(String link)
        {
                this.link = link;
        }

        public Date getModified()
        {
                return modified;
        }

        public void setModified(Date modified)
        {
                this.modified = modified;
        }

        public Date getModified_gmt()
        {
                return modified_gmt;
        }

        public void setModified_gmt(Date modified_gmt)
        {
                this.modified_gmt = modified_gmt;
        }

        public String getSlug()
        {
                return slug;
        }

        public void setSlug(String slug)
        {
                this.slug = slug;
        }

        public String getStatus()
        {
                return status;
        }

        public void setStatus(String status)
        {
                this.status = status;
        }

        public String getType()
        {
                return type;
        }

        public void setType(String type)
        {
                this.type = type;
        }

        public String getPassword()
        {
                return password;
        }

        public void setPassword(String password)
        {
                this.password = password;
        }

        public Rendered getTitle()
        {
                return title;
        }

        public void setTitle(Rendered title)
        {
                this.title = title;
        }

        public Rendered getContent()
        {
                return content;
        }

        public void setContent(Rendered content)
        {
                this.content = content;
        }

        public int getAuthor()
        {
                return author;
        }

        public void setAuthor(int author)
        {
                this.author = author;
        }

        public Rendered getExcerpt()
        {
                return excerpt;
        }

        public void setExcerpt(Rendered excerpt)
        {
                this.excerpt = excerpt;
        }

        public int getFeatured_media()
        {
                return featured_media;
        }

        public void setFeatured_media(int featured_media)
        {
                this.featured_media = featured_media;
        }

        public String getComment_status()
        {
                return comment_status;
        }

        public void setComment_status(String comment_status)
        {
                this.comment_status = comment_status;
        }

        public String getPing_status()
        {
                return ping_status;
        }

        public void setPing_status(String ping_status)
        {
                this.ping_status = ping_status;
        }

        public String getFormat()
        {
                return format;
        }

        public void setFormat(String format)
        {
                this.format = format;
        }

        public MetadataForPost getMetadata()
        {
                return metadata;
        }

        public void setMetadata(MetadataForPost metadata)
        {
                this.metadata = metadata;
        }

        public Boolean getSticky()
        {
                return sticky;
        }

        public void setSticky(Boolean sticky)
        {
                this.sticky = sticky;
        }

        public String getTemplate()
        {
                return template;
        }

        public void setTemplate(String template)
        {
                this.template = template;
        }

        public List<Integer> getCategories()
        {
                return categories;
        }

        public void setCategories(List<Integer> categories)
        {
                this.categories = categories;
        }

        public List<Integer> getTags()
        {
                return tags;
        }

        public void setTags(List<Integer> tags)
        {
                this.tags = tags;
        }


        @Override
        public String toString()
        {
                return "PostsDelegate{" +
                        "date=" + date +
                        ", date_gmt=" + date_gmt +
                        ", guid=" + guid +
                        ", id=" + id +
                        ", link='" + link + '\'' +
                        ", modified=" + modified +
                        ", modified_gmt=" + modified_gmt +
                        ", slug='" + slug + '\'' +
                        ", status='" + status + '\'' +
                        ", type='" + type + '\'' +
                        ", password='" + password + '\'' +
                        ", title=" + title +
                        ", content=" + content +
                        ", author=" + author +
                        ", excerpt=" + excerpt +
                        ", featured_media=" + featured_media +
                        ", comment_status='" + comment_status + '\'' +
                        ", ping_status='" + ping_status + '\'' +
                        ", format='" + format + '\'' +
                        ", metadata=" + metadata +
                        ", sticky=" + sticky +
                        ", template='" + template + '\'' +
                        ", categories=" + categories +
                        ", tags=" + tags +
                        '}';
        }


        public class MetadataForPost implements Serializable
        {

                private List<Integer> views;

                @SerializedName("contenuto")
                private List<String> content;

                @SerializedName("_thumbnail_src")
                private List<String> thumbnail_src;
                private List<String> images_src;
                private List<Integer> count_comments;
                private List<Integer> count_like;
               // private List<PostTag> tags;

                private List<PostAuthor> author;


                public List<String> getContent()
                {
                        return content;
                }

                public void setContent(List<String> content)
                {
                        this.content = content;
                }

                public List<Integer> getViews()
                {
                        return views;
                }

                public void setViews(List<Integer> views)
                {
                        this.views = views;
                }

                public List<String> getThumbnail_src()
                {
                        return thumbnail_src;
                }

                public void setThumbnail_src(List<String> thumbnail_src)
                {
                        this.thumbnail_src = thumbnail_src;
                }

                public List<String> getImages_src()
                {
                        return images_src;
                }

                public void setImages_src(List<String> images_src)
                {
                        this.images_src = images_src;
                }

                public List<Integer> getCount_comments()
                {
                        return count_comments;
                }

                public void setCount_comments(List<Integer> count_comments)
                {
                        this.count_comments = count_comments;
                }

                public List<Integer> getCount_like()
                {
                        return count_like;
                }

                public void setCount_like(List<Integer> count_like)
                {
                        this.count_like = count_like;
                }


                public List<PostAuthor> getAuthor()
                {
                        return author;
                }

                public void setAuthor(List<PostAuthor> author)
                {
                        this.author = author;
                }
        }

        public class PostTag implements Serializable{
                private int term_id;
                private String name;
                private int count;

                public int getTerm_id()
                {
                        return term_id;
                }

                public void setTerm_id(int term_id)
                {
                        this.term_id = term_id;
                }

                public String getName()
                {
                        return name;
                }

                public void setName(String name)
                {
                        this.name = name;
                }

                public int getCount()
                {
                        return count;
                }

                public void setCount(int count)
                {
                        this.count = count;
                }
        }


        public class PostAuthor implements Serializable{

                private int author_id;
                private String display_name;
                private String user_description;
                private String avatar_src;

                public int getAuthor_id()
                {
                        return author_id;
                }

                public void setAuthor_id(int author_id)
                {
                        this.author_id = author_id;
                }

                public String getDisplay_name()
                {
                        return display_name;
                }

                public void setDisplay_name(String display_name)
                {
                        this.display_name = display_name;
                }

                public String getUser_description()
                {
                        return user_description;
                }

                public void setUser_description(String user_description)
                {
                        this.user_description = user_description;
                }

                public String getAvatar_src()
                {
                        return avatar_src;
                }

                public void setAvatar_src(String avatar_src)
                {
                        this.avatar_src = avatar_src;
                }
        }

}
