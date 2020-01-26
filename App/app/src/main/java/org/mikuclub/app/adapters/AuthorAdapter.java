package org.mikuclub.app.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapters.viewHolder.AuthorHeaderViewHolder;
import org.mikuclub.app.javaBeans.response.baseResource.Post;
import org.mikuclub.app.javaBeans.response.baseResource.User;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

public class AuthorAdapter extends PostsAdapter
{

        private User user;
        private boolean headerVisible = false;

        /**
         * 构建函数
         *
         * @param list           列表文章
         * @param context
         */
        public AuthorAdapter(List<Post> list, Context context)
        {
                super(list, context);
                setHeaderRow(1);
        }

        /**
         * 头部控制器 (作者信息)
         *
         * @param parent
         * @return
         */
        @Override
        protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent)
        {
                //加载布局
                View view = getAdpterInflater().inflate(R.layout.author_info, parent, false);
                AuthorHeaderViewHolder holder = new AuthorHeaderViewHolder(view);
                return holder;
        }

        @Override
        protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position)
        {
                AuthorHeaderViewHolder viewHolder = (AuthorHeaderViewHolder)holder;
                //只有在用户信息不是null 并且要显示头部的情况
                if(user !=null && headerVisible){
                        //显示头部组件
                        viewHolder.getContainer().setVisibility(View.VISIBLE);
                        //加载头像
                        GlideImageUtils.getSquareImg(getAdapterContext(),  viewHolder.getAuthorImg(), user.getMetadata().getAvatar_src());
                        //设置用户名
                        viewHolder.getAuthorName().setText(user.getName());
                        viewHolder.getAuthorDescription().setText(user.getDescription());
                }
                else{
                        //隐藏头部组件
                        viewHolder.getContainer().setVisibility(View.INVISIBLE);
                }
        }

        public void setUser(User user)
        {
                this.user = user;
        }

        public void setHeaderVisible(boolean headerVisible)
        {
                this.headerVisible = headerVisible;
        }
}
