package org.mikuclub.app.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapters.viewHolder.CommentViewHolder;
import org.mikuclub.app.adapters.viewHolder.FooterViewHolder;
import org.mikuclub.app.adapters.viewHolder.PostViewHolder;
import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.ui.activity.PostActivity;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

public class PostsAdapter extends BaseAdapterWithFooter
{


        /**
         * 构建函数
         *
         * @param list
         * @param context
         */
        public PostsAdapter(List<Post> list, Context context)
        {
                super(list, context);
        }

        @Override
        protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent)
        {
                View view = getAdpterInflater().inflate(R.layout.list_item_post, parent, false);
                //创建item控制器
                PostViewHolder holder = new PostViewHolder(view);
                //绑定动作
                setItemOnClickListener(holder);
                return holder;
        }

        @Override
        protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position)
        {
                PostViewHolder postViewHolder = (PostViewHolder) holder;
                //检查和修复position偏移
                position = getListPosition(holder.getAdapterPosition());
                //先从列表获取对应位置的数据
                Post post = (Post)getAdapterList().get(position);
                //为视图设置各项数据
                postViewHolder.getItemText().setText(post.getTitle().getRendered());
                String imgUrl = post.getMetadata().getThumbnail_src().get(0);
                //加载远程图片
                GlideImageUtils.get(getAdapterContext(), postViewHolder.getItemImage(), imgUrl);
        }


        private void setItemOnClickListener(final PostViewHolder holder)
        {
                holder.getItem().setOnClickListener(v -> {
                        //检查和修复position偏移
                        int position = getListPosition(holder.getAdapterPosition());
                        //获取数据
                        Post post = (Post) getAdapterList().get(position);
                        //启动 文章页
                        PostActivity.startAction(getAdapterContext(), post);
                });
        }




}
