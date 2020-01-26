package org.mikuclub.app.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapters.viewHolder.PrivateMessageViewHolder;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.response.baseResource.Author;
import org.mikuclub.app.javaBeans.response.baseResource.PrivateMessage;
import org.mikuclub.app.javaBeans.response.baseResource.User;
import org.mikuclub.app.javaBeans.response.baseResource.UserLogin;
import org.mikuclub.app.ui.activity.PrivateMessageActivity;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

public class PrivateMessagesAdapter extends BaseAdapterWithFooter
{
        /*静态变量*/
        /*定义一个新 item类型代码 , 随机选5*/
        protected final static int TYPE_ITEM2 = 50;

        /*变量*/
        //当前用户
        private UserLogin user;
        //私信作者
        private Author author;

        /**
         * 构建函数
         *
         * @param list
         * @param context
         * @param  user 当前用户信息
         */
        public PrivateMessagesAdapter(List list, Context context, UserLogin user, Author author)
        {
                super(list, context);
                this.user=user;
                this.author = author;
        }

        /**
         * 绑定消息框点击事件
         */
        protected void setItemOnClickListener(final PrivateMessageViewHolder holder)
        {
                //取消点击事件
        }


        protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType)
        {

                View view;
                if(viewType == TYPE_ITEM){
                        view = getAdpterInflater().inflate(R.layout.list_item_message_left, parent, false);
                }
                else{
                        view = getAdpterInflater().inflate(R.layout.list_item_message_right, parent, false);
                }

                //创建item控制器
                PrivateMessageViewHolder holder = new PrivateMessageViewHolder(view);
                //绑定动作
                setItemOnClickListener(holder);
                return holder;
        }


        protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position, int viewType)
        {
                String name;
                String avatarSrc;
                //如果是别人发给用户的私信
                if (viewType ==TYPE_ITEM){
                        name = author.getName();
                        avatarSrc=author.getAvatar_src();
                }
                //如果是用户自己就是私信作者
                else{
                        name = user.getUser_display_name();
                        avatarSrc = user.getAvatar_urls();
                }

                PrivateMessageViewHolder viewHolder = (PrivateMessageViewHolder) holder;
                //先从列表获取对应位置的数据
                final PrivateMessage privateMessage = (PrivateMessage) getAdapterList().get(position);
                //为视图设置数据
                viewHolder.getItemName().setText(name);
                //生成时间格式
                String dateString = new SimpleDateFormat(GlobalConfig.DATE_FORMAT).format(privateMessage.getDate());
                viewHolder.getItemDate().setText(dateString);
                //确保给地址添加上https协议
                avatarSrc = HttpUtils.checkAndAddHttpsProtocol(avatarSrc);
                //加载远程图片
                GlideImageUtils.getSquareImg(getAdapterContext(), viewHolder.getItemAvatarImg(), avatarSrc);

                //获取私信内容
                String htmlContent = privateMessage.getContent();
                //移除内容外层P标签
                htmlContent = HttpUtils.removeHtmlMainTag(htmlContent, "<p>", "</p>");

                //解析 内容html
                HttpUtils.parseHtmlDefault(getAdapterContext(), htmlContent, viewHolder.getItemContent());


        }

        @Override
        public int getItemViewType(int position)
        {
                int type;
                //如果是头部行范围内
                if (position < getHeaderRow())
                {
                        type = TYPE_HEADER;
                }
                //如果是尾部 ( 位置 = 列表位置+头部位置+尾部位置)
                else if (position == (getAdapterList().size() - 1) + getHeaderRow() + getFooterRow())
                {
                        type = TYPE_FOOTER;
                }
                //如果是默认数据
                else
                {
                        //获取私信数据
                        PrivateMessage privateMessage = (PrivateMessage) getAdapterList().get(position-getHeaderRow());
                        //根据私信作者id 来决定 布局类型
                        //如果私信作者是当前用户自己
                        if(privateMessage.getSender_id() == user.getId()){
                                //设置第二种布局类型
                                type = TYPE_ITEM2;
                        }
                        else
                        {
                                type = TYPE_ITEM;
                        }

                }
                return type;

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
                //声明控制器变量
                RecyclerView.ViewHolder holder;

                //如果是普通数据类型
                if (viewType == TYPE_ITEM || viewType == TYPE_ITEM2)
                {
                        holder = onCreateItemViewHolder(parent, viewType);
                }

                //如果是头部类型
                else if (viewType == TYPE_HEADER)
                {
                        holder = onCreateHeaderViewHolder(parent);
                }
                //如果是尾部类型
                else
                {
                        holder = onCreateFooterViewHolder(parent);
                }
                return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
        {
                int viewType = holder.getItemViewType();
                //如果是普通数据类型
                if (viewType == TYPE_ITEM || viewType == TYPE_ITEM2)
                {
                        onBindItemViewHolder(holder, position, viewType);
                }
                //如果是头部类型
                else if (viewType == TYPE_HEADER)
                {
                        onBindHeaderViewHolder(holder, position);
                }
                //如果是尾部类型
                else
                {
                        onBindFooterViewHolder(holder, isLoading(), isNotMoreError(), isInternetError(), getInternetErrorListener());
                }
        }

        /*不会被调用到, 创建了只是为了实现 abstract方法*/
        @Override
        protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent)
        {
                return null;
        }

        @Override
        protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position)
        {
        }
}
