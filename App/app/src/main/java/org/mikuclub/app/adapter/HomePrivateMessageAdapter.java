package org.mikuclub.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapter.base.BaseAdapterWithFooter;
import org.mikuclub.app.adapter.viewHolder.PrivateMessageViewHolder;
import org.mikuclub.app.javaBeans.response.baseResource.PrivateMessage;
import org.mikuclub.app.ui.activity.PrivateMessageActivity;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 主页消息页面 私信列表适配器
 * 主体显示 用户收到的私信列表
 * home private message page: private message recyclerView adapter
 * the main body displays a list of private messages received by user
 */
public class HomePrivateMessageAdapter extends BaseAdapterWithFooter
{


        /**
         * 构建函数 default constructor
         *
         * @param context
         * @param list
         */
        public HomePrivateMessageAdapter(Context context, List list)
        {
                super(context, list);
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
                String dateString = GeneralUtils.DateToString(privateMessage.getDate());
                viewHolder.getItemDate().setText(dateString);
                //加载远程图片
                GlideImageUtils.getSquareImg(getAdapterContext(), viewHolder.getItemAvatarImg(), privateMessage.getAuthor().getAvatar_src());

                //显示解析过html的内容
                HttpUtils.parseHtmlDefault(getAdapterContext(), privateMessage.getContent(), viewHolder.getItemContent());

                //如果是未读消息
                if (privateMessage.getStatus() == 0)
                {
                        //显示未读提示
                        viewHolder.getItemUnread().setVisibility(View.VISIBLE);
                }

        }


        /**
         * 设置主体元素的点击事件监听器
         *
         * @param holder 元素控制器
         */
        private void setItemOnClickListener(final PrivateMessageViewHolder holder)
        {
                //绑定评论框点击动作
                holder.getItem().setOnClickListener(v -> {
                        //获取对应位置的数据 , 修复可能的position偏移
                        PrivateMessage privateMessage = (PrivateMessage) getAdapterListElementWithHeaderRowFix(holder.getAdapterPosition());
                        //启动私信详情页
                        PrivateMessageActivity.startAction(getAdapterContext(), privateMessage.getAuthor());
                });

        }




}
