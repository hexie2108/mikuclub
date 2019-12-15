package org.mikuclub.app.javaBeans.resources;



import org.mikuclub.app.javaBeans.resources.modules.Headers;
import org.mikuclub.app.javaBeans.resources.modules.MetadataForPost;
import org.mikuclub.app.javaBeans.resources.modules.Rendered;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Posts  implements Serializable
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
