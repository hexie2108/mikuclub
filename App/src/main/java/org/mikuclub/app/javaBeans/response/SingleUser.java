package org.mikuclub.app.javaBeans.response;

import org.mikuclub.app.javaBeans.response.baseResource.User;
import org.mikuclub.app.javaBeans.response.modules.Headers;

import java.io.Serializable;

public class SingleUser  implements Serializable
{
        private User body;
        private Integer status;
        private Headers headers;

        public User getBody()
        {
                return body;
        }

        public void setBody(User body)
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