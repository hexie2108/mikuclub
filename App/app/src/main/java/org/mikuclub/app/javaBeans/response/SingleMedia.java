package org.mikuclub.app.javaBeans.response;

import org.mikuclub.app.javaBeans.response.baseResource.Media;
import org.mikuclub.app.javaBeans.response.modules.Headers;

import java.io.Serializable;

public class SingleMedia implements Serializable
{
        private Media body;
        private Integer status;
        private Headers headers;

        public Media getBody()
        {
                return body;
        }

        public void setBody(Media body)
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