package org.mikuclub.app.adapter.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 文章列表的组件控制器
 * post list view holder
 */
public class PostManageViewHolder extends RecyclerView.ViewHolder
{

        private ConstraintLayout postItem;
        private ImageView postImage;
        private TextView postTitle;
        private TextView postStatusValue;
        private TextView postDate;
        private TextView postViews;
        private TextView postCountComments;
        private TextView postCountLike;
        private TextView postCountFavorite;
        private TextView postCountShare;
        private Button editButton;
        private Button deleteButton;


        public PostManageViewHolder(@NonNull View itemView)
        {
                super(itemView);
                 //管理器获取和绑定各项组件
                postItem = itemView.findViewById(R.id.post_item);;
                postImage = itemView.findViewById(R.id.post_image);
                postTitle = itemView.findViewById(R.id.post_title);
                postStatusValue= itemView.findViewById(R.id.post_status_value);
                postDate= itemView.findViewById(R.id.post_date);
                postViews= itemView.findViewById(R.id.post_views);
               postCountComments= itemView.findViewById(R.id.post_count_comments);
               postCountLike= itemView.findViewById(R.id.post_count_like);
               postCountFavorite = itemView.findViewById(R.id.post_count_favorite);
               postCountShare= itemView.findViewById(R.id.post_count_share);
               editButton= itemView.findViewById(R.id.button_edit);
              deleteButton= itemView.findViewById(R.id.button_delete);

        }

        public TextView getPostCountFavorite()
        {
                return postCountFavorite;
        }

        public void setPostCountFavorite(TextView postCountFavorite)
        {
                this.postCountFavorite = postCountFavorite;
        }

        public ConstraintLayout getPostItem()
        {
                return postItem;
        }

        public void setPostItem(ConstraintLayout postItem)
        {
                this.postItem = postItem;
        }

        public ImageView getPostImage()
        {
                return postImage;
        }

        public void setPostImage(ImageView postImage)
        {
                this.postImage = postImage;
        }

        public TextView getPostTitle()
        {
                return postTitle;
        }

        public void setPostTitle(TextView postTitle)
        {
                this.postTitle = postTitle;
        }

        public TextView getPostStatusValue()
        {
                return postStatusValue;
        }

        public void setPostStatusValue(TextView postStatusValue)
        {
                this.postStatusValue = postStatusValue;
        }

        public TextView getPostDate()
        {
                return postDate;
        }

        public void setPostDate(TextView postDate)
        {
                this.postDate = postDate;
        }

        public TextView getPostViews()
        {
                return postViews;
        }

        public void setPostViews(TextView postViews)
        {
                this.postViews = postViews;
        }

        public TextView getPostCountComments()
        {
                return postCountComments;
        }

        public void setPostCountComments(TextView postCountComments)
        {
                this.postCountComments = postCountComments;
        }

        public TextView getPostCountLike()
        {
                return postCountLike;
        }

        public void setPostCountLike(TextView postCountLike)
        {
                this.postCountLike = postCountLike;
        }

        public TextView getPostCountShare()
        {
                return postCountShare;
        }

        public void setPostCountShare(TextView postCountShare)
        {
                this.postCountShare = postCountShare;
        }

        public Button getEditButton()
        {
                return editButton;
        }

        public void setEditButton(Button editButton)
        {
                this.editButton = editButton;
        }

        public Button getDeleteButton()
        {
                return deleteButton;
        }

        public void setDeleteButton(Button deleteButton)
        {
                this.deleteButton = deleteButton;
        }
}

