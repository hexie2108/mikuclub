package org.mikuclub.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapters.viewHolder.CommentViewHolder;
import org.mikuclub.app.adapters.viewHolder.FooterViewHolder;
import org.mikuclub.app.javaBeans.resources.Comment;
import org.mikuclub.app.ui.fragments.windows.CommentRepliesFragment;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import me.wcy.htmltext.OnTagClickListener;
import mikuclub.app.R;

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
        //正常内容
        private final static int TYPE_ITEM = 0;
        //尾部加载刷新布局
        private final static int TYPE_FOOTER = 1;

        //数据列表
        protected List<Comment> list;
        //上下文
        protected Context mConxt;
        //布局创建器
        private LayoutInflater mInflater;

        //尾部占据的列数
        private int footerRow = 1;

        //错误情况 指示器
        private boolean notMoreError = false;
        private boolean internetError = false;
        //错误情况的 点击事件监听器
        private View.OnClickListener internetErrorListener = null;


        /**
         * 构建函数
         *
         * @param list
         * @param context
         */
        public CommentsAdapter(List<Comment> list, Context context)
        {
                this.list = list;
                this.mConxt = context;
                this.mInflater = LayoutInflater.from(context);
        }


        @Override
        public int getItemViewType(int position)
        {
                if (position == list.size())
                {
                        return TYPE_FOOTER;
                }
                else
                {
                        return TYPE_ITEM;
                }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
                //创建控制器
                final RecyclerView.ViewHolder holder;
                //如果是普通数据类型
                if (viewType == TYPE_ITEM)
                {
                        View view = mInflater.inflate(R.layout.list_item_comment, parent, false);
                        holder = new CommentViewHolder(view);
                        setItemOnClickListener((CommentViewHolder) holder);

                }
                //如果是footer
                else
                {
                        View view = mInflater.inflate(R.layout.list_item_info_util, parent, false);
                        holder = new FooterViewHolder(view);
                }
                return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
        {
                //需要展示对应子视图的时候
                //如果是普通数据组件
                if (holder instanceof CommentViewHolder)
                {
                        bindCommentViewHolder(holder, position);

                }
                else if (holder instanceof FooterViewHolder)
                {
                        AdapterUtils.bindFooterViewHolder(holder, notMoreError, true, internetError, internetErrorListener);
                }

        }

        @Override
        public int getItemCount()
        {
                //获取列表长度
                //因为增加了 footer组件, 所以长度要加上footer占据的数量
                return list.size() + footerRow;
        }


        protected void bindCommentViewHolder(RecyclerView.ViewHolder viewHolder, int position)
        {
                CommentViewHolder holder = (CommentViewHolder) viewHolder;
                //先从列表获取对应位置的数据
                final Comment comment = list.get(position);

                //为视图设置数据
                holder.getItemName().setText(comment.getAuthor_name());
                //生成时间格式
                String dateString = new SimpleDateFormat("yy-MM-dd HH:mm").format(comment.getDate());
                holder.getItemDate().setText(dateString);
                //确保给地址添加上https协议
                String avatarSrc = HttpUtils.checkAndAddHttpsProtocol(comment.getAuthor_avatar_urls().getSize96());
                //加载远程图片
                GlideImageUtils.getSquareImg(mConxt, holder.getItemAvatarImg(), avatarSrc);

                //如果评论有被回复过
                if (comment.getMetadata().getCount_replies() > 0)
                {
                        //显示回复数量
                        holder.getItemCountReplies().setText(comment.getMetadata().getCount_replies() + " 条回复");
                        holder.getItemCountReplies().setVisibility(View.VISIBLE);
                }

                //获取评论内容
                String htmlContent = comment.getContent().getRendered();
                //移除内容外层P标签
                htmlContent = HttpUtils.removeHtmlMainTag(htmlContent, "<p>", "</p>");
                //解析 内容html
                HttpUtils.parseHtml(mConxt, htmlContent, holder.getItemContent(), new OnTagClickListener()
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
                                mConxt.startActivity(intent);
                        }
                });



        }

        /**
         * 绑定评论框点击事件
         */
        protected void setItemOnClickListener(final CommentViewHolder holder){
                //绑定评论框点击动作
                holder.getItem().setOnClickListener(new View.OnClickListener()
                {
                        @Override
                        public void onClick(View v)
                        {

                                CommentRepliesFragment fragment = CommentRepliesFragment.startAction(list.get(holder.getAdapterPosition()));
                                fragment.show(((AppCompatActivity)mConxt).getSupportFragmentManager(), fragment.getClass().toString());

                        }
                });
        }


        /**
         * 设置 没有更多 错误
         *
         * @param notMoreError
         */
        public void setNotMoreError(boolean notMoreError)
        {
                this.notMoreError = notMoreError;
        }

        /**
         * 设置 网络错误 和 重试点击监听器
         *
         * @param internetError
         * @param internetErrorListener
         */
        public void setInternetError(boolean internetError, View.OnClickListener internetErrorListener)
        {
                this.internetError = internetError;
                this.internetErrorListener = internetErrorListener;
        }


        /**
         * 子评论列表的适配器 继承了普通评论列表
         */
        public static class RepliesAdapter extends CommentsAdapter
        {

                /**
                 * 构建函数
                 *
                 * @param list
                 * @param context
                 */
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
