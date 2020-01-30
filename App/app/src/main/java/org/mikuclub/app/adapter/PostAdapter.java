package org.mikuclub.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapter.base.BaseAdapterWithFooter;
import org.mikuclub.app.adapter.viewHolder.PostViewHolder;
import org.mikuclub.app.javaBeans.response.baseResource.Post;
import org.mikuclub.app.ui.activity.PostActivity;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;


/**
 * 文章列表适配器
 * 主体显示 文章列表
 * post recyclerView adapter
 * the main body displays a list of post
 */
public class PostAdapter extends BaseAdapterWithFooter
{


        /**
         * 构建函数 default constructor
         *
         * @param list
         * @param context
         */
        public PostAdapter(List<Post> list, Context context)
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
                //先从列表获取对应位置的数据
                Post post = (Post)getAdapterListElementWithHeaderRowFix(holder.getAdapterPosition());
                //为视图设置各项数据
                //修复标题中可能存在的被html转义的特殊符号
                String title = GeneralUtils.unescapeHtml(post.getTitle().getRendered());
                postViewHolder.getItemText().setText(title);
                String imgUrl = post.getMetadata().getThumbnail_src().get(0);
                //加载远程图片
                GlideImageUtils.get(getAdapterContext(), postViewHolder.getItemImage(), imgUrl);
        }

        /**
         * 设置主体元素的点击事件监听器
         *
         * @param holder 元素控制器
         */
        private void setItemOnClickListener(final PostViewHolder holder)
        {
                holder.getItem().setOnClickListener(v -> {

                        //获取数据, 修复可能的position偏移
                        Post post = (Post) getAdapterListElementWithHeaderRowFix(holder.getAdapterPosition());
                        //启动 文章页
                        PostActivity.startAction(getAdapterContext(), post);
                });
        }




}
