package org.mikuclub.app.javaBeans.parameters;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

public class CreateMediaParameters extends BaseParameters
{

        private File file;
        private String title;
        private Integer post;


        public Map<String, Object> toMap()
        {
                Map<String, Object> outputMap = new HashMap();

                putIfNotNull(outputMap, "title", title);
                putIfNotNull(outputMap, "post", post);


                return outputMap;

        }


        public File getFile()
        {
                return file;
        }

        public void setFile(File file)
        {
                this.file = file;
        }

        public String getTitle()
        {
                return title;
        }

        public void setTitle(String title)
        {
                this.title = title;
        }

        public Integer getPost()
        {
                return post;
        }

        public void setPost(Integer post)
        {
                this.post = post;
        }
}
