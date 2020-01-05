package org.mikuclub.app.javaBeans.parameters;

import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

public class CreateCommentParameters extends BaseParameters
{

        private String content;
        private String author_ip;
        private Integer parent;
        private Integer post;
        private String status;


        public Map<String, String> toMap()
        {
                Map<String, String> outputMap = new HashMap<String, String>();

                putIfNotNull(outputMap, "content", content);
                putIfNotNull(outputMap, "author_ip", author_ip);
                putIfNotNull(outputMap, "parent", parent);
                putIfNotNull(outputMap, "post", post);
                putIfNotNull(outputMap, "status", status);

                return outputMap;

        }

        public String getContent()
        {
                return content;
        }

        public void setContent(String content)
        {
                this.content = content;
        }

        public String getAuthor_ip()
        {
                return author_ip;
        }

        public void setAuthor_ip(String author_ip)
        {
                this.author_ip = author_ip;
        }

        public Integer getParent()
        {
                return parent;
        }

        public void setParent(Integer parent)
        {
                this.parent = parent;
        }

        public Integer getPost()
        {
                return post;
        }

        public void setPost(Integer post)
        {
                this.post = post;
        }

        public String getStatus()
        {
                return status;
        }

        public void setStatus(String status)
        {
                this.status = status;
        }
}