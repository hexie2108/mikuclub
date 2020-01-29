package org.mikuclub.app.javaBeans.parameters;

import org.mikuclub.app.javaBeans.parameters.base.BaseParameters;

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
        private Meta meta;

        public static class Meta{
                private Integer parent_user_id;
                private Integer parent_user_read;

                public Integer getParent_user_id()
                {
                        return parent_user_id;
                }

                public void setParent_user_id(Integer parent_user_id)
                {
                        this.parent_user_id = parent_user_id;
                }

                public Integer getParent_user_read()
                {
                        return parent_user_read;
                }

                public void setParent_user_read(Integer parent_user_read)
                {
                        this.parent_user_read = parent_user_read;
                }
        }


        public Map<String, Object> toMap()
        {
                Map<String, Object> outputMap = new HashMap();

                putIfNotNull(outputMap, "content", content);
                putIfNotNull(outputMap, "author_ip", author_ip);
                putIfNotNull(outputMap, "parent", parent);
                putIfNotNull(outputMap, "post", post);
                putIfNotNull(outputMap, "status", status);
                putIfNotNull(outputMap, "meta", meta);


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

        public Meta getMeta()
        {
                return meta;
        }

        public void setMeta(Meta meta)
        {
                this.meta = meta;
        }
}
