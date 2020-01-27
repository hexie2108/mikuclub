package org.mikuclub.app.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapters.viewHolder.CommentViewHolder;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.response.baseResource.Author;
import org.mikuclub.app.javaBeans.response.baseResource.Comment;
import org.mikuclub.app.ui.activity.AuthorActivity;
import org.mikuclub.app.ui.activity.PostActivity;
import org.mikuclub.app.ui.activity.PrivateMessageActivity;
import org.mikuclub.app.ui.fragments.windows.CommentRepliesFragment;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

public class CommentsAdapter extends BaseAdapterWithFooter
{

        private boolean displayReplyCount = true;

        /**
         * 构建函数
         *
         * @param list
         * @param context
         */
        public CommentsAdapter(List<Comment> list, Context context)
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
                final Comment comment = (Comment) getAdapterList().get(position);

                //为视图设置数据
                viewHolder.getItemName().setText(comment.getAuthor_name());
                //生成时间格式
                String dateString = new SimpleDateFormat(GlobalConfig.DATE_FORMAT).format(comment.getDate());
                viewHolder.getItemDate().setText(dateString);
                //确保给地址添加上https协议
                String avatarSrc = HttpUtils.checkAndAddHttpsProtocol(comment.getAuthor_avatar_urls().getSize96());
                //加载远程图片
                GlideImageUtils.getSquareImg(getAdapterContext(), viewHolder.getItemAvatarImg(), avatarSrc);

                //如果要显示回复数 和 回复不为空
                if (displayReplyCount && !GeneralUtils.listIsNullOrHasEmptyElement(comment.getMetadata().getComment_reply_ids()))
                {
                        //显示回复数量
                        viewHolder.getItemCountReplies().setText(comment.getMetadata().getComment_reply_ids().size() + " 条回复");
                        viewHolder.getItemCountReplies().setVisibility(View.VISIBLE);
                }

                String commentContent = comment.getContent().getRendered();
                //如果是在回复评论的情况下, 去除回复头部
                if(!displayReplyCount){
                        //如果固定回复头部存在
                        String startText = "回复";
                        String endText = " : ";
                        int index = commentContent.indexOf(startText);
                        //如果回复头部在前5位之间
                        if (index > -1 && index < 5)
                        {
                                commentContent = commentContent.substring(commentContent.indexOf(endText) + endText.length());
                        }
                }
                //显示解析过html的内容
                HttpUtils.parseHtmlDefault(getAdapterContext(), commentContent, viewHolder.getItemContent());
        }


        /**
         * 绑定评论框点击事件
         */
        protected void setItemOnClickListener(final CommentViewHolder holder)
        {
                //绑定评论框点击动作
                holder.getItem().setOnClickListener(v -> {
                        //获取对应位置的数据 , 修复可能的position偏移
                        Comment comment = (Comment) getAdapterList().get(holder.getAdapterPosition() - getHeaderRow());
                        CommentRepliesFragment fragment = CommentRepliesFragment.startAction(comment);
                        fragment.show(((AppCompatActivity) getAdapterContext()).getSupportFragmentManager(), fragment.getClass().toString());
                });

                //绑定头像的点击事件
                holder.getItemAvatarImg().setOnClickListener(v -> {
                        //获取对应位置的数据 , 修复可能的position偏移
                        Comment comment = (Comment) getAdapterList().get(holder.getAdapterPosition() - getHeaderRow());
                        //启动作者页面
                        Author author = new Author();
                        author.setName(comment.getAuthor_name());
                        author.setAuthor_id(comment.getAuthor());
                        author.setAvatar_src(comment.getAuthor_avatar_urls().getSize96());

                        AuthorActivity.startAction(getAdapterContext(), author);
                });

        }

        public void setDisplayReplyCount(boolean displayReplyCount)
        {
                this.displayReplyCount = displayReplyCount;
        }

        /**
         * 子评论列表的适配器 继承了普通评论的适配器, 只是改变了动作
         */
        public static class RepliesAdapter extends CommentsAdapter
        {

                public RepliesAdapter(List<Comment> list, Context context)
                {
                        super(list, context);
                        setDisplayReplyCount(false);
                }



                @Override
                protected void setItemOnClickListener(final CommentViewHolder holder)
                {

                }
        }



}
