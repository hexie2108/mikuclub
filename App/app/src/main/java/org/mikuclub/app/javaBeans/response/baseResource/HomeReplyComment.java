package org.mikuclub.app.javaBeans.response.baseResource;


import org.mikuclub.app.javaBeans.response.modules.Avatar_urls;
import org.mikuclub.app.javaBeans.response.modules.Rendered;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class HomeReplyComment implements Serializable
{


        private int id;
        private Author author;
        private String content;
        private Date date;
        private int parent;
        private int post;
        //识别文章是否已读, 0为未读, 1为已读
        private int status;
        //相关文章标题
        private String post_title;

        public int getId()
        {
                return id;
        }

        public void setId(int id)
        {
                this.id = id;
        }

        public Author getAuthor()
        {
                return author;
        }

        public void setAuthor(Author author)
        {
                this.author = author;
        }

        public String getContent()
        {
                return content;
        }

        public void setContent(String content)
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

        public int getStatus()
        {
                return status;
        }

        public void setStatus(int status)
        {
                this.status = status;
        }

        public String getPost_title()
        {
                return post_title;
        }

        public void setPost_title(String post_title)
        {
                this.post_title = post_title;
        }
}
