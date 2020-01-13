package org.mikuclub.app.javaBeans.resources;


import org.mikuclub.app.javaBeans.resources.modules.Headers;

import java.io.Serializable;
import java.util.List;

public class CreateComment implements Serializable
{

        private Comment body;

        public Comment getBody()
        {
                return body;
        }

        public void setBody(Comment body)
        {
                this.body = body;
        }
}
