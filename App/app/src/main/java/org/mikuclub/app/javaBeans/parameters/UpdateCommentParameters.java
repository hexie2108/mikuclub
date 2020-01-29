package org.mikuclub.app.javaBeans.parameters;

import org.mikuclub.app.javaBeans.parameters.base.BaseParameters;

import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

public class UpdateCommentParameters extends BaseParameters
{

        private Integer id;
        private String content;
        private String author_ip;
        private Integer parent;
        private Integer post;
        private String status;


        public Map<String, Object> toMap()
        {
                Map<String, Object> outputMap = new HashMap();

                putIfNotNull(outputMap, "id", id);
                putIfNotNull(outputMap, "content", content);
                putIfNotNull(outputMap, "author_ip", author_ip);
                putIfNotNull(outputMap, "parent", parent);
                putIfNotNull(outputMap, "post", post);
                putIfNotNull(outputMap, "status", status);

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
