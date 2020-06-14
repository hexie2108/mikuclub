package org.mikuclub.app.javaBeans.response.baseResource;

import java.io.Serializable;

public class Author implements Serializable
{

        private int author_id;
        private String name;
        private String description;
        private String avatar_src;

        public int getAuthor_id()
        {
                return author_id;
        }

        public void setAuthor_id(int author_id)
        {
                this.author_id = author_id;
        }

        public String getName()
        {
                return name;
        }

        public void setName(String name)
        {
                this.name = name;
        }

        public String getDescription()
        {
                return description;
        }

        public void setDescription(String description)
        {
                this.description = description;
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
