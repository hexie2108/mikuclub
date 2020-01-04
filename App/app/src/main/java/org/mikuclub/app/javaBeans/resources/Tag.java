package org.mikuclub.app.javaBeans.resources;

import java.io.Serializable;

public class  Tag  implements Serializable
{

        private Integer id;
        private Integer count;
        private String description;
        private String link;
        private String name;
        private String slug;
        private String taxonomy;
        private Object meta;


        public Integer getId()
        {
                return id;
        }

        public void setId(Integer id)
        {
                this.id = id;
        }

        public Integer getCount()
        {
                return count;
        }

        public void setCount(Integer count)
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

        public Object getMeta()
        {
                return meta;
        }

        public void setMeta(Object meta)
        {
                this.meta = meta;
        }
}
