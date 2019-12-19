package org.mikuclub.app.adapters.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 列表结尾 功能组件 (显示加载进度条 和 显示对应错误)
 */
public class FooterViewHolder extends RecyclerView.ViewHolder{

        View itemView;
        ProgressBar progressBar;
        ImageView imageView;
        TextView textView;

        public FooterViewHolder(@NonNull View itemView)
        {
                super(itemView);
                //管理器绑定各项 视图
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