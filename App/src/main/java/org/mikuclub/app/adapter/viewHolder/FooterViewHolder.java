package org.mikuclub.app.adapter.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 通用recyclerView尾部组件控制器 (显示加载进度条, 错误提示 )
 * general recyclerView tail view holder (show loading progress bar, error message)
 */
public class FooterViewHolder extends RecyclerView.ViewHolder{

        View itemView;
        ProgressBar progressBar;
        ImageView imageView;
        TextView textView;

        public FooterViewHolder(@NonNull View itemView)
        {
                super(itemView);
                 //管理器获取和绑定各项组件
                this.itemView = itemView;
                progressBar = itemView.findViewById(R.id.list_item_progress_bar);
                imageView = itemView.findViewById(R.id.list_item_info_icon);
                textView = itemView.findViewById(R.id.list_item_info_text);
        }

        public View getItemView()
        {
                return itemView;
        }


        public ProgressBar getProgressBar()
        {
                return progressBar;
        }


        public ImageView getImageView()
        {
                return imageView;
        }


        public TextView getTextView()
        {
                return textView;
        }

}