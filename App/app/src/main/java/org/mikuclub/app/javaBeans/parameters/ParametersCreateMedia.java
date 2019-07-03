package org.mikuclub.app.javaBeans.parameters;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.dataStructure.MapUtils.putIfnotNull;

public class ParametersCreateMedia
{

        private File file;
        private String title;
        private Integer post;


        public Map<String, String> toMap()
        {
                Map<String, String> outputMap = new HashMap<String, String>();

                putIfnotNull(outputMap, "title", title);
                putIfnotNull(outputMap, "post", post);


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
