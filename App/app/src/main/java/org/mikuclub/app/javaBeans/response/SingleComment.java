package org.mikuclub.app.javaBeans.response;


import org.mikuclub.app.javaBeans.response.baseResource.Comment;
import org.mikuclub.app.javaBeans.response.modules.Headers;

import java.io.Serializable;

public class SingleComment implements Serializable
{

        private Comment body;
        private Integer status;
        private Headers headers;

        public Comment getBody()
        {
                return body;
        }

        public void setBody(Comment body)
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
