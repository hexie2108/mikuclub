package org.mikuclub.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapters.viewHolder.CommentViewHolder;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.resources.Comment;
import org.mikuclub.app.ui.fragments.windows.CommentRepliesFragment;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import me.wcy.htmltext.OnTagClickListener;
import mikuclub.app.R;

public class CommentsAdapter extends BaseAdapterWithFooter
{

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

                //如果评论有被回复过
                if (comment.getMetadata().getCount_replies() > 0)
                {
                        //显示回复数量
                        viewHolder.getItemCountReplies().setText(comment.getMetadata().getCount_replies() + " 条回复");
                        viewHolder.getItemCountReplies().setVisibility(View.VISIBLE);
                }

                //获取评论内容
                String htmlContent = comment.getContent().getRendered();
                //移除内容外层P标签
                htmlContent = HttpUtils.removeHtmlMainTag(htmlContent, "<p>", "</p>");
                //解析 内容html
                HttpUtils.parseHtml(getAdapterContext(), htmlContent, viewHolder.getItemContent(), new OnTagClickListener()
                {
                        //设置 点击图片tag的动作
                        @Override
                        public void onImageClick(Context context, List<String> imagesSrc, int position)
                        {
                        }

                        //设置点击链接tag的动作
                        @Override
                        public void onLinkClick(Context context, String url)
                        {
                                // link click
                                Uri uri = Uri.parse(HttpUtils.checkAndAddHttpsProtocol(url));
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                intent.setData(uri);
                                getAdapterContext().startActivity(intent);
                        }
                });
        }


        /**
         * 绑定评论框点击事件
         */
        protected void setItemOnClickListener(final CommentViewHolder holder)
        {
                //绑定评论框点击动作
                holder.getItem().setOnClickListener(v ->{
                        Comment comment= (Comment) getAdapterList().get(holder.getAdapterPosition());
                        CommentRepliesFragment fragment = CommentRepliesFragment.startAction(comment);
                        fragment.show(((AppCompatActivity) getAdapterContext()).getSupportFragmentManager(), fragment.getClass().toString());
                });

        }



        /**
         * 子评论列表的适配器 继承了普通评论的适配器, 只是改变了动作
         */
        public static class RepliesAdapter extends CommentsAdapter
        {

                public RepliesAdapter(List<Comment> list, Context context)
                {
                        super(list, context);
                }

                @Override
                protected void setItemOnClickListener(final CommentViewHolder holder)
                {

                }
        }

}
