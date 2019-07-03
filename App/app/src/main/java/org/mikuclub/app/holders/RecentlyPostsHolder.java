package org.mikuclub.app.holders;

import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.utils.httpUtils.Connection;

import mikuclub.app.R;

//传入数据的model为泛型
public class RecentlyPostsHolder extends BaseViewHolder<Post>
{
        private NetworkImageView img;
        private TextView title;
        private TextView description;

        private String[] imgs = {
                "https://static.mikuclub.org/wp-content/uploads/2019/07/20190701173824.jpg",
                "https://www.mikuclub.org/wp-content/uploads/2019/07/20190701173809.jpg",
                "https://www.mikuclub.org/wp-content/uploads/2019/07/20190701173815.jpg",
                "https://www.mikuclub.org/wp-content/uploads/2019/07/20190701173805.jpg",
                "https://www.mikuclub.org/wp-content/uploads/2019/07/20190701131039.jpg",
                "https://www.mikuclub.org/wp-content/uploads/2019/07/20190701131036.jpg",
                "https://www.mikuclub.org/wp-content/uploads/2019/07/20190701131030.jpg",
                "https://www.mikuclub.org/wp-content/uploads/2019/07/20190701131022.jpg",
                "https://www.mikuclub.org/wp-content/uploads/2019/07/20190701131006.jpg",
                "https://www.mikuclub.org/wp-content/uploads/2019/07/20190701131004.jpg",
                "https://www.mikuclub.org/wp-content/uploads/2019/07/20190701131001.jpg",
        };

        public RecentlyPostsHolder(ViewGroup parent)
        {
                super(parent, R.layout.recycler_view_item);
                img = $(R.id.img);
                title = $(R.id.title);
                description = $(R.id.description);


        }

        @Override
        public void setData(Post post)
        {
                title.setText(post.getTitle().getRendered());
                description.setText(post.getExcerpt().getRendered());
                Connection.getImg(post.getMetadata().getThumbnail_img_src().get(0), img);


        }
}
