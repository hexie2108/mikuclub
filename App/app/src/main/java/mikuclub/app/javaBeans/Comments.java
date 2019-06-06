package mikuclub.app.javaBeans;


import java.util.Date;

public class Comments
{

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

        public Object getAuthor_avatar_urls()
        {
                return author_avatar_urls;
        }

        public void setAuthor_avatar_urls(Object author_avatar_urls)
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

        private int id;
        private int author;
        private String author_email;
        private String author_ip;
        private String author_name;
        private String author_url;
        private String author_user_agent;
        private String content;
        private Date date;
        private Date date_gmt;
        private String link;
        private int parent;
        private int post;
        private String status;
        private String type;
        private Object author_avatar_urls;
        private Object meta;

}
