package org.mikuclub.app.javaBeans.response;






import org.mikuclub.app.javaBeans.response.baseResource.Post;
import org.mikuclub.app.javaBeans.response.modules.Headers;

import java.io.Serializable;
import java.util.ArrayList;


public class Posts   implements Serializable
{
        private ArrayList<Post> body;
        private Integer status;
        private Headers headers;

        public ArrayList<Post> getBody()
        {
                return body;
        }

        public void setBody(ArrayList<Post> body)
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
