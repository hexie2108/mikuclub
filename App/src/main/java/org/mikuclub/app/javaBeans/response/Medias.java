package org.mikuclub.app.javaBeans.response;


import org.mikuclub.app.javaBeans.response.baseResource.Media;
import org.mikuclub.app.javaBeans.response.modules.Headers;

import java.io.Serializable;
import java.util.List;

public class Medias   implements Serializable
{

        private List<Media> body;
        private Integer status;
        private Headers headers;

        public List<Media> getBody()
        {
                return body;
        }

        public void setBody(List<Media> body)
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
