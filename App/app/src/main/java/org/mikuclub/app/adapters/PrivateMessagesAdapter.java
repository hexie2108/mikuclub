package org.mikuclub.app.adapters;

import android.content.Context;

import org.mikuclub.app.adapters.viewHolder.CommentViewHolder;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.resources.base.Comment;
import org.mikuclub.app.javaBeans.resources.base.PrivateMessage;
import org.mikuclub.app.ui.activity.PostActivity;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class PrivateMessagesAdapter extends CommentsAdapter
{


        /**
         * 构建函数
         *
         * @param list
         * @param context
         */
        public PrivateMessagesAdapter(List list, Context context)
        {
                super(list, context);
        }


        @Override
        protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position)
        {
                CommentViewHolder viewHolder = (CommentViewHolder) holder;
                //先从列表获取对应位置的数据
                final PrivateMessage privateMessage = (PrivateMessage) getAdapterList().get(position);

                //为视图设置数据
                viewHolder.getItemName().setText(privateMessage.getAuthor().getName());
                //生成时间格式
                String dateString = new SimpleDateFormat(GlobalConfig.DATE_FORMAT).format(privateMessage.getDate());
                viewHolder.getItemDate().setText(dateString);
                //确保给地址添加上https协议
                String avatarSrc = HttpUtils.checkAndAddHttpsProtocol(privateMessage.getAuthor().getAvatar_src());
                //加载远程图片
                GlideImageUtils.getSquareImg(getAdapterContext(), viewHolder.getItemAvatarImg(), avatarSrc);

                //获取私信内容
                String htmlContent = privateMessage.getContent();
                //移除内容外层P标签
                htmlContent = HttpUtils.removeHtmlMainTag(htmlContent, "<p>", "</p>");
                //解析 内容html
                HttpUtils.parseHtmlDefault(getAdapterContext(), htmlContent, viewHolder.getItemContent());

        }


        /**
         * 绑定评论框点击事件
         */
        protected void setItemOnClickListener(final CommentViewHolder holder)
        {
                //绑定评论框点击动作
                holder.getItem().setOnClickListener(v ->{

                });

        }




}
