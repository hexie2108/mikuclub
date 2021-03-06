package org.mikuclub.app.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapter.base.BaseAdapterWithFooter;
import org.mikuclub.app.adapter.viewHolder.PostManageViewHolder;
import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.delegate.PostDelegate;
import org.mikuclub.app.javaBeans.response.baseResource.Post;
import org.mikuclub.app.ui.activity.PostActivity;
import org.mikuclub.app.ui.activity.PostSubmitActivity;
import org.mikuclub.app.utils.AlertDialogUtils;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.ResourcesUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;
import org.mikuclub.app.utils.http.HttpCallBack;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 投稿管理 适配器
 */
public class PostManageAdapter extends BaseAdapterWithFooter
{

        private PostDelegate delegate;
        private AlertDialog progressDialog;


        /**
         * 构建函数 default constructor
         *
         * @param context
         * @param list     列表文章
         * @param delegate 删除按钮的点击确认动作
         */
        public PostManageAdapter(Context context, List<Post> list, PostDelegate delegate)
        {
                super(context, list);
                this.delegate = delegate;
                this.progressDialog = AlertDialogUtils.createProgressDialog(context, false, false);
        }

        @Override
        protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent)
        {
                View view = getAdpterInflater().inflate(R.layout.list_item_post_manage, parent, false);
                //创建item控制器
                PostManageViewHolder holder = new PostManageViewHolder(view);
                //绑定动作
                setItemOnClickListener(holder);
                return holder;
        }

        @Override
        protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position)
        {
                PostManageViewHolder viewHolder = (PostManageViewHolder) holder;
                //先从列表获取对应位置的数据
                Post post = (Post) getAdapterListElementWithHeaderRowFix(holder.getAdapterPosition());

                //为视图设置各项数据
                String imgUrl = post.getMetadata().getThumbnail_src().get(0);
                //加载远程图片
                GlideImageUtils.get(getAdapterContext(), viewHolder.getPostImage(), imgUrl);

                //修复标题中可能存在的被html转义的特殊符号
                String title = GeneralUtils.unescapeHtml(post.getTitle().getRendered());
                viewHolder.getPostTitle().setText(title);

                //设置状态名
                setPostStatus(viewHolder, post.getStatus());

                //生成时间格式
                String dateString = GeneralUtils.DateToString(post.getDate());
                viewHolder.getPostDate().setText(dateString);

                //获取文章元数据
                Post.Metadata metadata = post.getMetadata();

                //获取文章相关统计数
                int viewsCount = 0;
                int commentsCount = 0;
                int likesCount = 0;
                int sharingCount = 0;
                int favoriteCount =0;

                //如果查看数不是空
                if (!GeneralUtils.listIsNullOrHasEmptyElement(metadata.getViews()))
                {
                        viewsCount = metadata.getViews().get(0);
                }
                //如果评论数不是空
                if (!GeneralUtils.listIsNullOrHasEmptyElement(metadata.getCount_comments()))
                {
                        commentsCount = metadata.getCount_comments().get(0);
                }
                //如果点赞数不是空
                if (!GeneralUtils.listIsNullOrHasEmptyElement(metadata.getCount_like()))
                {
                        likesCount = metadata.getCount_like().get(0);
                }
                //如果分享数不是空
                if (!GeneralUtils.listIsNullOrHasEmptyElement(metadata.getCount_sharing()))
                {
                        sharingCount = metadata.getCount_sharing().get(0);
                }
                //如果收藏数不是空
                if (!GeneralUtils.listIsNullOrHasEmptyElement(metadata.getCount_favorite()))
                {
                        favoriteCount = metadata.getCount_favorite().get(0);
                }

                String postViewsText = String.format(ResourcesUtils.getString(R.string.post_view_count), viewsCount);
                String postCountCommentText = String.format(ResourcesUtils.getString(R.string.post_comment_count), commentsCount);
                String postCountLikeText = String.format(ResourcesUtils.getString(R.string.post_like_count), likesCount);
                String postCountSharingText = String.format(ResourcesUtils.getString(R.string.post_sharing_count), sharingCount);
                String postCountFavoriteText = String.format(ResourcesUtils.getString(R.string.post_favorite_count), favoriteCount);

                viewHolder.getPostViews().setText(postViewsText);
                viewHolder.getPostCountComments().setText(postCountCommentText);
                viewHolder.getPostCountLike().setText(postCountLikeText);
                viewHolder.getPostCountShare().setText(postCountSharingText);
                viewHolder.getPostCountFavorite().setText(postCountFavoriteText);



        }

        /**
         * 设置主体元素的点击事件监听器
         *
         * @param holder 元素控制器
         */
        private void setItemOnClickListener(final PostManageViewHolder holder)
        {
                //文章主体
                holder.getPostItem().setOnClickListener(v -> {

                        //获取数据, 修复可能的position偏移
                        Post post = (Post) getAdapterListElementWithHeaderRowFix(holder.getAdapterPosition());
                        //启动 文章页
                        PostActivity.startAction(getAdapterContext(), post);
                });
                //编辑按钮
                holder.getEditButton().setOnClickListener(v -> {

                        //获取数据, 修复可能的position偏移
                        Post post = (Post) getAdapterListElementWithHeaderRowFix(holder.getAdapterPosition());
                        //启动投稿编辑页面
                        PostSubmitActivity.startAction(getAdapterContext(), post.getId());

                });
                //删除按钮
                holder.getDeleteButton().setOnClickListener(v -> {

                        final int position = holder.getAdapterPosition();

                        String title = ResourcesUtils.getString(R.string.delete_post);
                        String content = ResourcesUtils.getString(R.string.confirm_delete_post);
                        //确认按钮点击监听器
                        DialogInterface.OnClickListener positiveClickListener = (dialog, which) -> {
                                deletePostByPosition(position);
                        };

                        //创建确认弹窗
                        AlertDialog dialog = AlertDialogUtils.createDeleteConfirmDialog(getAdapterContext(), positiveClickListener);
                        //显示弹窗
                        dialog.show();


                });

        }


        /**
         * 根据文章状态值 设置对应的状态名称和颜色
         *
         * @param holder
         * @param status
         * @return
         */
        private void setPostStatus(PostManageViewHolder holder, String status)
        {

                String statusValue = null;
                int color = ResourcesUtils.getColor(R.color.defaultTextColor);

                switch (status)
                {
                        case GlobalConfig.Post.Status.PUBLISH:
                                statusValue = ResourcesUtils.getString(R.string.post_status_publish);
                                color = ResourcesUtils.getColor(R.color.colorPrimary);
                                break;
                        case GlobalConfig.Post.Status.PENDING:
                                statusValue = ResourcesUtils.getString(R.string.post_status_pending);
                                color = ResourcesUtils.getColor(android.R.color.holo_red_light);
                                break;
                        case GlobalConfig.Post.Status.DRAFT:
                                statusValue = ResourcesUtils.getString(R.string.post_status_draft);
                                color = ResourcesUtils.getColor(R.color.defaultTextColor);
                                break;
                }

                holder.getPostStatusValue().setText(statusValue);
                holder.getPostStatusValue().setTextColor(color);

        }


        /**
         * 根据列表位置 获取对应文章 并删除
         *
         * @param position
         */
        private void deletePostByPosition(int position)
        {

                //获取数据, 修复可能的position偏移
                Post post = (Post) getAdapterListElementWithHeaderRowFix(position);

                progressDialog.show();

                HttpCallBack httpCallBack = new HttpCallBack()
                {
                        @Override
                        public void onSuccess(String response)
                        {
                                //显示消息
                                ToastUtils.shortToast(ResourcesUtils.getString(R.string.delete_successful));
                                //从数据列表里删除元素
                                removeAdapterListElementWithHeaderRowFix(position);
                                //通知UI列表删除元素
                                notifyItemRemoved(position);
                        }


                        @Override
                        public void onFinally()
                        {
                                progressDialog.dismiss();
                        }

                        @Override
                        public void onCancel()
                        {
                                progressDialog.dismiss();
                        }
                };

                delegate.deletePost(httpCallBack, post.getId());

        }


}
