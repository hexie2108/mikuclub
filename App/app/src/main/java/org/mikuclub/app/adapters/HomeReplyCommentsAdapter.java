package org.mikuclub.app.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapters.viewHolder.CommentViewHolder;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.response.baseResource.Author;
import org.mikuclub.app.javaBeans.response.baseResource.Comment;
import org.mikuclub.app.javaBeans.response.baseResource.HomeReplyComment;
import org.mikuclub.app.ui.activity.AuthorActivity;
import org.mikuclub.app.ui.activity.PostActivity;
import org.mikuclub.app.ui.fragments.windows.CommentRepliesFragment;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

public class HomeReplyCommentsAdapter extends BaseAdapterWithFooter
{


        /**
         * 构建函数
         *
         * @param list
         * @param context
         */
        public HomeReplyCommentsAdapter(List<Comment> list, Context context)
        {
                super(list, context);
        }


        @Override
        protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent)
        {
                View view = getAdpterInflater().inflate(R.layout.list_item_comment, parent, false);
                //创建item控制器
                CommentViewHolder holder = new CommentViewHolder(view);
                //绑定动作
                setItemOnClickListener(holder);
                return holder;
        }

        @Override
        protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position)
        {
                CommentViewHolder viewHolder = (CommentViewHolder) holder;
                //先从列表获取对应位置的数据
                final HomeReplyComment replyComment = (HomeReplyComment) getAdapterList().get(position);

                //为视图设置数据
                viewHolder.getItemName().setText(replyComment.getAuthor().getName());
                //生成时间格式
                String dateString = new SimpleDateFormat(GlobalConfig.DATE_FORMAT).format(replyComment.getDate());
                viewHolder.getItemDate().setText(dateString);
                //确保给地址添加上https协议
                String avatarSrc = HttpUtils.checkAndAddHttpsProtocol(replyComment.getAuthor().getAvatar_src());
                //加载远程图片
                GlideImageUtils.getSquareImg(getAdapterContext(), viewHolder.getItemAvatarImg(), avatarSrc);


                //把评论回复数的view用来显示评论的文章来源
                viewHolder.getItemCountReplies().setText("评论来源: " + replyComment.getPost_title());
                viewHolder.getItemCountReplies().setVisibility(View.VISIBLE);

                //如果未读标签存在 并且注明是未读
                if (replyComment.getStatus() == 0)
                {
                        //显示未读标签
                        viewHolder.getItemUnread().setVisibility(View.VISIBLE);
                }
                //显示解析过html的内容
                HttpUtils.parseHtmlDefault(getAdapterContext(), replyComment.getContent(), viewHolder.getItemContent());
        }


        /**
         * 绑定评论框点击事件
         */
        protected void setItemOnClickListener(final CommentViewHolder holder)
        {
                //绑定评论整体的点击动作
                holder.getItem().setOnClickListener(v -> {
                        //获取对应位置的数据 , 修复可能的position偏移
                        HomeReplyComment replyComment = (HomeReplyComment) getAdapterList().get(holder.getAdapterPosition() - getHeaderRow());
                        //启动文章页
                        PostActivity.startAction(getAdapterContext(), replyComment.getPost());
                });

                //绑定头像的点击事件
                holder.getItemAvatarImg().setOnClickListener(v -> {
                        //获取对应位置的数据 , 修复可能的position偏移
                        HomeReplyComment replyComment = (HomeReplyComment) getAdapterList().get(holder.getAdapterPosition() - getHeaderRow());
                        //启动作者页面
                        AuthorActivity.startAction(getAdapterContext(), replyComment.getAuthor());


                });

        }


}
