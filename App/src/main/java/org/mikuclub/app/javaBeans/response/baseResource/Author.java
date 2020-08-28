package org.mikuclub.app.javaBeans.response.baseResource;

import java.io.Serializable;

public class Author implements Serializable
{

        private int id;
        private String display_name;
        private String description;
        private String user_image;


        public int getId()
        {
                return id;
        }

        public void setId(int id)
        {
                this.id = id;
        }

        public String getDisplay_name()
        {
                return display_name;
        }

        public void setDisplay_name(String display_name)
        {
                this.display_name = display_name;
        }

        public String getDescription()
        {
                return description;
        }

        public void setDescription(String description)
        {
                this.description = description;
        }

        public String getUser_image()
        {
                return user_image;
        }

        public void setUser_image(String user_image)
        {
                this.user_image = user_image;
        }

}
