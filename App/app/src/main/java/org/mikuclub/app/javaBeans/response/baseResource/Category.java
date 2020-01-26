package org.mikuclub.app.javaBeans.response.baseResource;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Category  implements Serializable
{

        @SerializedName("object_id")
        private int id;
        private String title;
        private int post_parent;
        private List<Category> children;

        public int getId()
        {
                return id;
        }

        public void setId(int id)
        {
                this.id = id;
        }

        public String getTitle()
        {
                return title;
        }

        public void setTitle(String title)
        {
                this.title = title;
        }

        public int getPost_parent()
        {
                return post_parent;
        }

        public void setPost_parent(int post_parent)
        {
                this.post_parent = post_parent;
        }

        public List<Category> getChildren()
        {
                return children;
        }

        public void setChildren(List<Category> children)
        {
                this.children = children;
        }
}
