package org.mikuclub.app.holders;

import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.utils.httpUtils.Connection;

import mikuclub.app.R;

//传入数据的model为泛型
public class PostListHolder extends BaseViewHolder<Post>
{
        private NetworkImageView img;
        private TextView title;
        private TextView description;


        public PostListHolder(ViewGroup parent)
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
