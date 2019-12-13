package org.mikuclub.app.javaBeans.parameters;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.data.MapUtils.putIfnotNull;

public class ParametersUpdateMedia
{
        private Integer id;
        private File file;
        private String title;
        private Integer post;


        public Map<String, String> toMap()
        {
                Map<String, String> outputMap = new HashMap<String, String>();
                putIfnotNull(outputMap, "id", id);
                putIfnotNull(outputMap, "title", title);
                putIfnotNull(outputMap, "post", post);


                return outputMap;

        }


        public Integer getId()
        {
                return id;
        }

        public void setId(Integer id)
        {
                this.id = id;
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
