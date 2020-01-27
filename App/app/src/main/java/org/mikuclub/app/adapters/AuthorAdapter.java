package org.mikuclub.app.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapters.viewHolder.AuthorHeaderViewHolder;
import org.mikuclub.app.javaBeans.response.baseResource.Author;
import org.mikuclub.app.javaBeans.response.baseResource.Post;
import org.mikuclub.app.ui.activity.PrivateMessageActivity;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

public class AuthorAdapter extends PostsAdapter
{

        private Author author;
        private boolean moreAuthorInfo = false;

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
                View view = getAdpterInflater().inflate(R.layout.author_info_box, parent, false);
                AuthorHeaderViewHolder holder = new AuthorHeaderViewHolder(view);
                //绑定点击监听
                setHeaderOnClickListener(holder);
                return holder;
        }

        @Override
        protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position)
        {
                AuthorHeaderViewHolder viewHolder = (AuthorHeaderViewHolder)holder;

                //目前2个没有区别, 以后可以设置区别
                //如果已经获取作者详细信息
                if(moreAuthorInfo){
                        //显示头部组件
                        viewHolder.getContainer().setVisibility(View.VISIBLE);
                        //加载头像
                        GlideImageUtils.getSquareImg(getAdapterContext(),  viewHolder.getAuthorImg(), author.getAvatar_src());
                        //设置用户名
                        viewHolder.getAuthorName().setText(author.getName());
                        viewHolder.getAuthorDescription().setText(author.getDescription());
                }
                //如果只有基本信息
                else{
                        //显示头部组件
                        viewHolder.getContainer().setVisibility(View.VISIBLE);
                        //加载头像
                        GlideImageUtils.getSquareImg(getAdapterContext(),  viewHolder.getAuthorImg(), author.getAvatar_src());
                        //设置用户名
                        viewHolder.getAuthorName().setText(author.getName());
                        viewHolder.getAuthorDescription().setText(author.getDescription());
                }
        }

        /**
         * 给头部配置点击监听
         * @param holder
         */
        private void setHeaderOnClickListener(AuthorHeaderViewHolder holder){

                //绑定发送私信按钮监听
                holder.getButtonSendMessage().setOnClickListener(v -> {
                        //创建作者信息
                        Author author =new Author();
                        author.setAuthor_id(this.author.getAuthor_id());
                        author.setAvatar_src(this.author.getAvatar_src());
                        author.setName(this.author.getName());
                        author.setDescription(this.author.getDescription());
                        //启动私信页
                        PrivateMessageActivity.startAction(getAdapterContext(),author );
                });

        }


        public void setAuthor(Author author)
        {
                this.author = author;
        }

        public void setMoreAuthorInfo(boolean moreAuthorInfo)
        {
                this.moreAuthorInfo = moreAuthorInfo;
        }
}
