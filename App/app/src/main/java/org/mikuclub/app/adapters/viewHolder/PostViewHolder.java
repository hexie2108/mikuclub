package org.mikuclub.app.adapters.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 文章列表 item控制器
 */
public class PostViewHolder extends RecyclerView.ViewHolder
{


        private CardView item;
        private ImageView itemImage;
        private TextView itemText;

        public PostViewHolder(@NonNull View itemView)
        {
                super(itemView);
                //管理器绑定各项 视图
                item = (CardView) itemView;
                itemImage = itemView.findViewById(R.id.item_image);
                itemText = itemView.findViewById(R.id.item_text);

        }

        public CardView getItem()
        {
                return item;
        }

        public void setItem(CardView item)
        {
                this.item = item;
        }

        public ImageView getItemImage()
        {
                return itemImage;
        }

        public void setItemImage(ImageView itemImage)
        {
                this.itemImage = itemImage;
        }

        public TextView getItemText()
        {
                return itemText;
        }

        public void setItemText(TextView itemText)
        {
                this.itemText = itemText;
        }
}

