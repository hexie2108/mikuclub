package org.mikuclub.app.javaBeans.resources;


import org.mikuclub.app.javaBeans.resources.modules.Headers;

import java.io.Serializable;
import java.util.List;

public class Categories implements Serializable
{

        private List<Category> body;
        private Integer status;
        private Headers headers;

        public List<Category> getBody()
        {
                return body;
        }

        public void setBody(List<Category> body)
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
