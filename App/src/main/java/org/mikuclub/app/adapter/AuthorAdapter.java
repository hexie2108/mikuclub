package org.mikuclub.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapter.viewHolder.AuthorHeaderViewHolder;
import org.mikuclub.app.javaBeans.response.baseResource.Author;
import org.mikuclub.app.javaBeans.response.baseResource.Post;
import org.mikuclub.app.storage.UserPreferencesUtils;
import org.mikuclub.app.ui.activity.PrivateMessageActivity;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 作者页面的列表适配器
 * 头部显示作者信息
 * 主体显示作者相关的文章列表
 * author page: recyclerView adapter
 * header displays author information
 * the main body displays a list of post related to the author
 */
public class AuthorAdapter extends PostAdapter
{

        //默认提供的作者信息
        private Author author;
        //判断是否已获取了 更加详细的作者信息
        private boolean moreAuthorInfo = false;

        /**
         * 构建函数 default constructor
         *
         * @param context
         * @param list  文章列表
         */
        public AuthorAdapter(Context context, List<Post> list)
        {
                super(context, list);
                setHeaderRow(1);
        }

        /**
         * 创建头部控制器 (作者信息)
         * Create Head view holder (Author Information)
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

        /**
         * 获取头部控制器并加载数据
         * get head view holder and display data
         * @param holder 元素控制器
         * @param position
         */
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
                        GlideImageUtils.getSquareImg(getAdapterContext(),  viewHolder.getAuthorImg(), author.getUser_image());
                        //设置用户名
                        viewHolder.getAuthorName().setText(author.getDisplay_name());
                        viewHolder.getAuthorDescription().setText(author.getDescription());
                }
                //如果只有基本信息
                else{
                        //显示头部组件
                        viewHolder.getContainer().setVisibility(View.VISIBLE);
                        //加载头像
                        GlideImageUtils.getSquareImg(getAdapterContext(),  viewHolder.getAuthorImg(), author.getUser_image());
                        //设置用户名
                        viewHolder.getAuthorName().setText(author.getDisplay_name());
                        viewHolder.getAuthorDescription().setText(author.getDescription());
                }
                //如果用户未登陆
                if(!UserPreferencesUtils.isLogin()){
                        //隐藏私信按钮
                        viewHolder.getButtonSendMessage().setVisibility(View.GONE);
                }
        }

        /**
         * 给头部组件设置点击事件监听器
         * Set the click event listener for the header
         * @param holder 元素控制器
         */
        private void setHeaderOnClickListener(AuthorHeaderViewHolder holder){

                //绑定发送私信按钮监听
                holder.getButtonSendMessage().setOnClickListener(v -> {
                        //创建作者信息
                        Author author =new Author();
                        author.setId(this.author.getId());
                        author.setUser_image(this.author.getUser_image());
                        author.setDisplay_name(this.author.getDisplay_name());
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
