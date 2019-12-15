package org.mikuclub.app.javaBeans.resources;

import java.io.Serializable;

public class Category  implements Serializable
{

        private int id;
        private int count;
        private String description;
        private String link;
        private String name;
        private String slug;
        private String taxonomy;
        private int parent;
        private Object meta;




        public int getId()
        {
                return id;
        }

        public void setId(int id)
        {
                this.id = id;
        }

        public int getCount()
        {
                return count;
        }

        public void setCount(int count)
        {
                this.count = count;
        }

        public String getDescription()
        {
                return description;
        }

        public void setDescription(String description)
        {
                this.description = description;
        }

        public String getLink()
        {
                return link;
        }

        public void setLink(String link)
        {
                this.link = link;
        }

        public String getName()
        {
                return name;
        }

        public void setName(String name)
        {
                this.name = name;
        }

        public String getSlug()
        {
                return slug;
        }

        public void setSlug(String slug)
        {
                this.slug = slug;
        }

        public String getTaxonomy()
        {
                return taxonomy;
        }

        public void setTaxonomy(String taxonomy)
        {
                this.taxonomy = taxonomy;
        }

        public int getParent()
        {
                return parent;
        }

        public void setParent(int parent)
        {
                this.parent = parent;
        }

        public Object getMeta()
        {
                return meta;
        }

        public void setMeta(Object meta)
        {
                this.meta = meta;
        }
}
