package org.mikuclub.app.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapters.viewHolder.PrivateMessageViewHolder;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.response.baseResource.PrivateMessage;
import org.mikuclub.app.ui.activity.PrivateMessageActivity;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

public class HomePrivateMessagesAdapter extends BaseAdapterWithFooter
{


        /**
         * 构建函数
         *
         * @param list
         * @param context
         */
        public HomePrivateMessagesAdapter(List list, Context context)
        {
                super(list, context);
        }


        @Override
        protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent)
        {
                View view = getAdpterInflater().inflate(R.layout.list_item_message, parent, false);
                //创建item控制器
                PrivateMessageViewHolder holder = new PrivateMessageViewHolder(view);
                //绑定动作
                setItemOnClickListener(holder);
                return holder;
        }


        @Override
        protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position)
        {
                PrivateMessageViewHolder viewHolder = (PrivateMessageViewHolder) holder;
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

                //如果是未读消息
                if (privateMessage.getStatus() == 0)
                {
                        //显示未读提示
                        viewHolder.getItemUnread().setVisibility(View.VISIBLE);
                }

        }


        /**
         * 绑定消息框点击事件
         */
        protected void setItemOnClickListener(final PrivateMessageViewHolder holder)
        {
                //绑定评论框点击动作
                holder.getItem().setOnClickListener(v -> {
                        //获取对应位置的数据 , 修复可能的position偏移
                        PrivateMessage privateMessage = (PrivateMessage) getAdapterList().get(holder.getAdapterPosition()-getHeaderRow());
                        //启动私信详情页
                        PrivateMessageActivity.startAction(getAdapterContext(), privateMessage.getAuthor());
                });

        }




}
