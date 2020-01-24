package org.mikuclub.app.javaBeans.parameters;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

public class UpdateMediaParameters extends BaseParameters
{
        private Integer id;
        private File file;
        private String title;
        private Integer post;


        public Map<String, Object> toMap()
        {
                Map<String, Object> outputMap = new HashMap();
                putIfNotNull(outputMap, "id", id);
                putIfNotNull(outputMap, "title", title);
                putIfNotNull(outputMap, "post", post);


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
