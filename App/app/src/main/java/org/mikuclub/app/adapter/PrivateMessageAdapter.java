package org.mikuclub.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapter.base.BaseAdapterWithFooter;
import org.mikuclub.app.adapter.viewHolder.PrivateMessageViewHolder;
import org.mikuclub.app.javaBeans.response.baseResource.Author;
import org.mikuclub.app.javaBeans.response.baseResource.PrivateMessage;
import org.mikuclub.app.javaBeans.response.baseResource.UserLogin;
import org.mikuclub.app.ui.activity.AuthorActivity;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 私信页面 来往私信列表适配器
 * 主体显示 发送和收到的私信列表
 * private message page, message recyclerView adapter
 * the main body displays a list of private message sent and received by user
 */
public class PrivateMessageAdapter extends BaseAdapterWithFooter
{
        /* 静态变量 Static variable */
        /*定义一个新 主体元素类型, 随机选50*/
        protected final static int TYPE_ITEM2 = 50;

        /* 变量 local variable */
        //当前用户
        private UserLogin user;
        //私信作者
        private Author author;

        /**
         * 构建函数 default constructor
         *
         * @param list
         * @param context
         * @param user    当前用户信息
         */
        public PrivateMessageAdapter(List list, Context context, UserLogin user, Author author)
        {
                super(list, context);
                this.user = user;
                this.author = author;
        }


        /**
         * 设置主体元素的点击事件监听器
         *
         * @param holder 元素控制器
         */
        protected void setItemOnClickListener(final PrivateMessageViewHolder holder)
        {
                //绑定头像的点击事件
                holder.getItemAvatarImg().setOnClickListener(v -> {
                        //启动私信页
                        AuthorActivity.startAction(getAdapterContext(), author);
                });

        }

        /**
         * 重写 创建主体元素控制器, 根据主体元素类型, 创建不同布局
         * 消息列表 有2个布局, 收到的私信和发出的私信用2个不同布局
         *
         * @param parent
         * @param viewType
         * @return
         */
        protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType)
        {

                View view;
                if (viewType == TYPE_ITEM)
                {
                        view = getAdpterInflater().inflate(R.layout.list_item_message_left, parent, false);
                }
                else
                {
                        view = getAdpterInflater().inflate(R.layout.list_item_message_right, parent, false);
                }

                //创建item控制器
                PrivateMessageViewHolder holder = new PrivateMessageViewHolder(view);
                //绑定动作
                setItemOnClickListener(holder);
                return holder;
        }


        /**
         * 重写 获取主体元素控制器和设置数据
         * 根据主体元素的类型, 加载不同的数据源
         *
         * @param holder
         * @param position
         * @param viewType
         */
        protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position, int viewType)
        {
                String name;
                String avatarSrc;
                //如果是别人发给用户的私信
                if (viewType == TYPE_ITEM)
                {
                        name = author.getName();
                        avatarSrc = author.getAvatar_src();
                }
                //如果是用户自己就是私信作者
                else
                {
                        name = user.getUser_display_name();
                        avatarSrc = user.getAvatar_urls();
                }

                PrivateMessageViewHolder viewHolder = (PrivateMessageViewHolder) holder;
                //先从列表获取对应位置的数据
                final PrivateMessage privateMessage = (PrivateMessage) getAdapterList().get(position);
                //为视图设置数据
                viewHolder.getItemName().setText(name);
                //生成时间格式
                String dateString = GeneralUtils.DateToString(privateMessage.getDate());
                viewHolder.getItemDate().setText(dateString);
                //加载远程图片
                GlideImageUtils.getSquareImg(getAdapterContext(), viewHolder.getItemAvatarImg(), avatarSrc);

                //显示解析过html的内容
                HttpUtils.parseHtmlDefault(getAdapterContext(), privateMessage.getContent(), viewHolder.getItemContent());
        }

        /**
         * 重写 获取元素类型
         *
         * @param position
         * @return
         */
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
                        PrivateMessage privateMessage = (PrivateMessage) getAdapterList().get(position - getHeaderRow());
                        //根据私信作者id 来决定 布局类型
                        //如果私信作者是当前用户自己
                        if (privateMessage.getSender_id() == user.getId())
                        {
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

        /**
         * 重写 元素创建器, 增加了 元素类型识别码 的传递
         *
         * @param parent
         * @param viewType
         * @return
         */
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

        /**
         * 重写 获取控制器 和设置数据, 增加了 元素类型识别码 的传递
         *
         * @param holder   元素控制器
         * @param position
         */
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

        /*不会被调用到, 创建了只是为了实现 abstract方法*/
        @Override
        protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position)
        {
        }
}
