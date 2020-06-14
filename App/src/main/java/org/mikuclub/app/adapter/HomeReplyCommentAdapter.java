package org.mikuclub.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapter.base.BaseAdapterWithFooter;
import org.mikuclub.app.adapter.viewHolder.CommentViewHolder;
import org.mikuclub.app.javaBeans.response.baseResource.Comment;
import org.mikuclub.app.javaBeans.response.baseResource.HomeReplyComment;
import org.mikuclub.app.ui.activity.AuthorActivity;
import org.mikuclub.app.ui.activity.PostActivity;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.ResourcesUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 主页消息页面 评论回复列表适配器
 * 主体显示 用户收到的评论回复列表
 * home comment reply page: comment reply recyclerView adapter
 * the main body displays a list of comment reply received by user
 */
public class HomeReplyCommentAdapter extends BaseAdapterWithFooter
{


        /**
         * 构建函数 default constructor
         *
         * @param context
         * @param list
         */
        public HomeReplyCommentAdapter(Context context, List<Comment> list)
        {
                super(context, list);
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
                String dateString = GeneralUtils.DateToString(replyComment.getDate());
                viewHolder.getItemDate().setText(dateString);
                //加载远程图片
                GlideImageUtils.getSquareImg(getAdapterContext(), viewHolder.getItemAvatarImg(), replyComment.getAuthor().getAvatar_src());


                //把评论回复数的view用来显示评论的文章来源

                viewHolder.getItemCountReplies().setText(ResourcesUtils.getString(R.string.comment_source)+" "+GeneralUtils.unescapeHtml(replyComment.getPost_title()));
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
         * 设置主体元素的点击事件监听器
         *
         * @param holder 元素控制器
         */
        private void setItemOnClickListener(final CommentViewHolder holder)
        {
                //绑定评论元素整体的点击动作监听器
                holder.getItem().setOnClickListener(v -> {
                        //获取对应位置的数据 , 修复可能的position偏移
                        HomeReplyComment replyComment = (HomeReplyComment) getAdapterListElementWithHeaderRowFix(holder.getAdapterPosition());
                        //启动文章页
                        PostActivity.startAction(getAdapterContext(), replyComment.getPost());
                });

                //绑定头像的点击事件监听器
                holder.getItemAvatarImg().setOnClickListener(v -> {
                        //获取对应位置的数据 , 修复可能的position偏移
                        HomeReplyComment replyComment = (HomeReplyComment) getAdapterListElementWithHeaderRowFix(holder.getAdapterPosition());
                        //启动作者页面
                        AuthorActivity.startAction(getAdapterContext(), replyComment.getAuthor());
                });

        }


}
