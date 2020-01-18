package org.mikuclub.app.javaBeans.resources;


import org.mikuclub.app.javaBeans.resources.base.Comment;

import java.io.Serializable;

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
