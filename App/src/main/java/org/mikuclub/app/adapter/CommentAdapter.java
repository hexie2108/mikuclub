package org.mikuclub.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapter.base.BaseAdapterWithFooter;
import org.mikuclub.app.adapter.viewHolder.CommentViewHolder;
import org.mikuclub.app.controller.CommentController;
import org.mikuclub.app.javaBeans.response.baseResource.Author;
import org.mikuclub.app.javaBeans.response.baseResource.Comment;
import org.mikuclub.app.storage.UserPreferencesUtils;
import org.mikuclub.app.ui.activity.AuthorActivity;
import org.mikuclub.app.ui.fragments.windows.CommentRepliesFragment;
import org.mikuclub.app.utils.ClipboardUtils;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.ResourcesUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 文章页面 评论列表适配器
 * 主体显示 评论列表
 * post page: comment recyclerView adapter
 * the main body displays a list of comments
 */
public class CommentAdapter extends BaseAdapterWithFooter
{
        //是否要显示 收到的回复数量
        private boolean displayReplyCount = true;

        /**
         * 构建函数 default constructor
         *
         * @param context
         * @param list
         */
        public CommentAdapter(Context context, List<Comment> list)
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
                final Comment comment = (Comment) getAdapterList().get(position);

                //为视图设置数据
                viewHolder.getItemName().setText(comment.getAuthor_name());
                //生成时间格式
                String dateString = GeneralUtils.DateToString(comment.getDate());
                viewHolder.getItemDate().setText(dateString);
                //加载远程图片
                GlideImageUtils.getSquareImg(getAdapterContext(), viewHolder.getItemAvatarImg(), comment.getAuthor_avatar_urls().getSize96());

                //如果要显示回复数 和 回复不为空
                if (displayReplyCount && !GeneralUtils.listIsNullOrHasEmptyElement(comment.getMetadata().getComment_reply_ids()))
                {
                        //显示回复数量
                        viewHolder.getItemCountReplies().setText(comment.getMetadata().getComment_reply_ids().size() + " " + ResourcesUtils.getString(R.string.reply_count));
                        viewHolder.getItemCountReplies().setVisibility(View.VISIBLE);
                }
                //获取回复内容
                String commentContent = comment.getContent().getRendered();
                //如果是在回复评论的情况下, 去除回复头部
                if (!displayReplyCount)
                {
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
         * 设置主体元素的点击事件监听器
         *
         * @param holder 元素控制器
         */
        protected void setItemOnClickListener(final CommentViewHolder holder)
        {



                //绑定头像的点击事件
                holder.getItemAvatarImg().setOnClickListener(v -> {
                        //获取对应位置的数据 , 修复可能的position偏移
                        Comment comment = (Comment) getAdapterListElementWithHeaderRowFix(holder.getAdapterPosition());
                        //只有在不是 登陆用户 和 评论作者 不是同个人的情况, 避免访问自己的作者页面
                        if (!UserPreferencesUtils.isCurrentUser(comment.getAuthor()))
                        {
                                //启动作者页面
                                Author author = new Author();
                                author.setDisplay_name(comment.getAuthor_name());
                                author.setId(comment.getAuthor());
                                author.setUser_image(comment.getAuthor_avatar_urls().getSize96());
                                AuthorActivity.startAction(getAdapterContext(), author);
                        }

                });

                //绑定评论框整体点击动作
                holder.getItem().setOnClickListener(v -> {
                        //获取对应位置的数据 , 修复可能的position偏移
                        Comment comment = (Comment) getAdapterListElementWithHeaderRowFix(holder.getAdapterPosition());
                        CommentRepliesFragment fragment = CommentRepliesFragment.startAction(comment);
                        fragment.show(((AppCompatActivity) getAdapterContext()).getSupportFragmentManager(), fragment.getClass().toString());

                });
                //绑定评论框整体长按动作
                holder.getItem().setOnLongClickListener(v -> {
                        //获取对应位置的数据 , 修复可能的position偏移
                        Comment comment = (Comment) getAdapterListElementWithHeaderRowFix(holder.getAdapterPosition());
                        //复制评论到剪切版
                        ClipboardUtils.setText(HttpUtils.removeHtmlMainTag(comment.getContent().getRendered(), "<p>", "</p>"));
                        ToastUtils.shortToast(ResourcesUtils.getString(R.string.comment_copy_message));

                        //消耗掉点击事件
                        return true;
                });

        }

        public void setDisplayReplyCount(boolean displayReplyCount)
        {
                this.displayReplyCount = displayReplyCount;
        }

        /**
         * 文章页面 子评论列表适配器
         * 主体显示 子评论列表
         * 继承了普通评论适配器, 只是覆盖元素的点击事件监听,
         * 之后 为了解决数据沟通问题, 需要传递 controller,
         * post page: sub comment recyclerView adapter
         * the main body displays a list of sub comments
         */
        public static class RepliesAdapter extends CommentAdapter
        {
                private CommentController controller;

                /**
                 * @param list
                 * @param context
                 */
                public RepliesAdapter(List<Comment> list, Context context)
                {
                        super(context, list);
                        //关闭显示被回复数
                        setDisplayReplyCount(false);

                }

                @Override
                protected void setItemOnClickListener(final CommentViewHolder holder)
                {
                        //绑定评论框点击动作
                        holder.getItem().setOnClickListener(v -> {
                                //某个评论点击的话 就变更为被回复对象,  , 修复可能的position偏移
                                Comment parentComment = (Comment) getAdapterListElementWithHeaderRowFix(holder.getAdapterPosition());
                                //只有在不是 登陆用户 和 评论作者 不是同个人的情况, 避免自己回复自己
                                if (!UserPreferencesUtils.isCurrentUser(parentComment.getAuthor()))
                                {
                                        controller.changeParentComment(parentComment, false);
                                }
                                //提示用户可复制评论
                                ToastUtils.shortToast(ResourcesUtils.getString(R.string.suggest_to_copy_message));
                        });
                        //绑定评论框整体长按动作
                        holder.getItem().setOnLongClickListener(v -> {
                                //获取对应位置的数据 , 修复可能的position偏移
                                Comment comment = (Comment) getAdapterListElementWithHeaderRowFix(holder.getAdapterPosition());
                                //复制评论到剪切版
                                ClipboardUtils.setText(HttpUtils.removeHtmlMainTag(comment.getContent().getRendered(), "<p>", "</p>"));
                                ToastUtils.shortToast(ResourcesUtils.getString(R.string.comment_copy_message));
                                //消耗掉点击事件
                                return true;
                        });

                }

                public void setController(CommentController controller)
                {
                        this.controller = controller;
                }
        }


}
