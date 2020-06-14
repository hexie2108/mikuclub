package org.mikuclub.app.javaBeans.response;

import org.mikuclub.app.javaBeans.response.baseResource.Author;
import org.mikuclub.app.javaBeans.response.modules.Headers;

import java.io.Serializable;

public class SingleAuthor implements Serializable
{
        private Author body;
        private Integer status;
        private Headers headers;

        public Author getBody()
        {
                return body;
        }

        public void setBody(Author body)
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