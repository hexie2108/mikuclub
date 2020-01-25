package org.mikuclub.app.javaBeans.resources;


import org.mikuclub.app.javaBeans.resources.base.Post;
import org.mikuclub.app.javaBeans.resources.modules.Headers;

import java.io.Serializable;
import java.util.ArrayList;


public class SinglePost implements Serializable
{
        private Post body;
        private Integer status;
        private Headers headers;


        public Post getBody()
        {
                return body;
        }

        public void setBody(Post body)
        {
                this.body = body;
        }

        public Integer getStatus()
        {
                return status;
        }

        public void setStatus(Integer status)
        {
                this.status = status;
        }

        public Headers getHeaders()
        {
                return headers;
        }

        public void setHeaders(Headers headers)
        {
                this.headers = headers;
        }
}
