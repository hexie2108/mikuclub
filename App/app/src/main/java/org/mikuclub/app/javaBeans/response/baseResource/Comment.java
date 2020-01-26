package org.mikuclub.app.javaBeans.response.baseResource;


import org.mikuclub.app.javaBeans.response.modules.Avatar_urls;
import org.mikuclub.app.javaBeans.response.modules.Rendered;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Comment  implements Serializable
{


        private int id;
        private int author;
        private String author_email;
        private String author_ip;
        private String author_name;
        private String author_url;
        private String author_user_agent;
        private Rendered content;
        private Date date;
        private Date date_gmt;
        private String link;
        private int parent;
        private int post;
        private String status;
        private String type;
        private Avatar_urls author_avatar_urls;
        private Object meta;
        private Metadata metadata;


        public class Metadata  implements  Serializable{
              private List<Integer> comment_reply_ids;
              private String parent_user_name;

                public String getParent_user_name()
                {
                        return parent_user_name;
                }

                public void setParent_user_name(String parent_user_name)
                {
                        this.parent_user_name = parent_user_name;
                }

                public List<Integer> getComment_reply_ids()
                {
                        return comment_reply_ids;
                }

                public void setComment_reply_ids(List<Integer> comment_reply_ids)
                {
                        this.comment_reply_ids = comment_reply_ids;
                }
        }




        public int getId()
        {
                return id;
        }

        public void setId(int id)
        {
                this.id = id;
        }

        public int getAuthor()
        {
                return author;
        }

        public void setAuthor(int author)
        {
                this.author = author;
        }

        public String getAuthor_email()
        {
                return author_email;
        }

        public void setAuthor_email(String author_email)
        {
                this.author_email = author_email;
        }

        public String getAuthor_ip()
        {
                return author_ip;
        }

        public void setAuthor_ip(String author_ip)
        {
                this.author_ip = author_ip;
        }

        public String getAuthor_name()
        {
                return author_name;
        }

        public void setAuthor_name(String author_name)
        {
                this.author_name = author_name;
        }

        public String getAuthor_url()
        {
                return author_url;
        }

        public void setAuthor_url(String author_url)
        {
                this.author_url = author_url;
        }

        public String getAuthor_user_agent()
        {
                return author_user_agent;
        }

        public void setAuthor_user_agent(String author_user_agent)
        {
                this.author_user_agent = author_user_agent;
        }

        public Rendered getContent()
        {
                return content;
        }

        public void setContent(Rendered content)
        {
                this.content = content;
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

        public String getLink()
        {
                return link;
        }

        public void setLink(String link)
        {
                this.link = link;
        }

        public int getParent()
        {
                return parent;
        }

        public void setParent(int parent)
        {
                this.parent = parent;
        }

        public int getPost()
        {
                return post;
        }

        public void setPost(int post)
        {
                this.post = post;
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

        public Avatar_urls getAuthor_avatar_urls()
        {
                return author_avatar_urls;
        }

        public void setAuthor_avatar_urls(Avatar_urls author_avatar_urls)
        {
                this.author_avatar_urls = author_avatar_urls;
        }

        public Object getMeta()
        {
                return meta;
        }

        public void setMeta(Object meta)
        {
                this.meta = meta;
        }

        public Metadata getMetadata()
        {
                return metadata;
        }

        public void setMetadata(Metadata metadata)
        {
                this.metadata = metadata;
        }
}
