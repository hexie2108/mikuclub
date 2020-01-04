package org.mikuclub.app.javaBeans.resources;

import org.mikuclub.app.javaBeans.resources.modules.Rendered;

import java.io.Serializable;
import java.util.Date;

public class Page  implements Serializable
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
        private int parent;
        private Rendered title;
        private Rendered content;
        private int author;
        private Rendered excerpt;
        private int featured_media;
        private String comment_status;
        private String ping_status;
        private String menu_order;
        private Object meta;
        private String template;

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

        public int getParent()
        {
                return parent;
        }

        public void setParent(int parent)
        {
                this.parent = parent;
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

        public String getMenu_order()
        {
                return menu_order;
        }

        public void setMenu_order(String menu_order)
        {
                this.menu_order = menu_order;
        }

        public Object getMeta()
        {
                return meta;
        }

        public void setMeta(Object meta)
        {
                this.meta = meta;
        }

        public String getTemplate()
        {
                return template;
        }

        public void setTemplate(String template)
        {
                this.template = template;
        }
}

