package org.mikuclub.app.javaBeans.response.baseResource;

import java.io.Serializable;

public class ImagePreview implements Serializable
{

        private int id;
        private String title;
        private String source_url;
        private boolean alreadySubmitted;

        public boolean isAlreadySubmitted()
        {
                return alreadySubmitted;
        }

        public void setAlreadySubmitted(boolean alreadySubmitted)
        {
                this.alreadySubmitted = alreadySubmitted;
        }

        public int getId()
        {
                return id;
        }

        public void setId(int id)
        {
                this.id = id;
        }

        public String getTitle()
        {
                return title;
        }

        public void setTitle(String title)
        {
                this.title = title;
        }

        public String getSource_url()
        {
                return source_url;
        }

        public void setSource_url(String source_url)
        {
                this.source_url = source_url;
        }
}

