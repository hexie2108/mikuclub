package org.mikuclub.app.adapter.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 私信列表组件控制器
 * private message list view holder
 */
public class PrivateMessageViewHolder extends RecyclerView.ViewHolder
{

        private ConstraintLayout item;
        private ImageView itemAvatarImg;
        private TextView itemName;
        private TextView itemDate;
        private TextView itemContent;
        //只有在首页私信列表里会用到
        private TextView itemUnread;

        public PrivateMessageViewHolder(@NonNull View itemView)
        {
                super(itemView);
                 //管理器获取和绑定各项组件
                item = (ConstraintLayout) itemView;
                itemAvatarImg = itemView.findViewById(R.id.item_avatar_img);
                itemName = itemView.findViewById(R.id.item_name);
                itemDate = itemView.findViewById(R.id.item_date);
                itemContent = itemView.findViewById(R.id.item_content);
                itemUnread = itemView.findViewById(R.id.item_unread);

        }


        public TextView getItemUnread()
        {
                return itemUnread;
        }

        public void setItemUnread(TextView itemUnread)
        {
                this.itemUnread = itemUnread;
        }

        public ConstraintLayout getItem()
        {
                return item;
        }

        public void setItem(ConstraintLayout item)
        {
                this.item = item;
        }

        public ImageView getItemAvatarImg()
        {
                return itemAvatarImg;
        }

        public void setItemAvatarImg(ImageView itemAvatarImg)
        {
                this.itemAvatarImg = itemAvatarImg;
        }

        public TextView getItemName()
        {
                return itemName;
        }

        public void setItemName(TextView itemName)
        {
                this.itemName = itemName;
        }

        public TextView getItemDate()
        {
                return itemDate;
        }

        public void setItemDate(TextView itemDate)
        {
                this.itemDate = itemDate;
        }

        public TextView getItemContent()
        {
                return itemContent;
        }

        public void setItemContent(TextView itemContent)
        {
                this.itemContent = itemContent;
        }
}

