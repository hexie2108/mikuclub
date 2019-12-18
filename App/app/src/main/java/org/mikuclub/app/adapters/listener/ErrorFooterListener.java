package org.mikuclub.app.adapters.listener;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.mikuclub.app.adapters.PostsAdapter;

import androidx.recyclerview.widget.RecyclerView;

public class ErrorFooterListener
{


        /**
         * 获取列表数据 但是网络错误的时候调用
         *
         * @param viewHolder
         * @param errorMessage
         * @param onClickListener
         */
        public static void setupHttpErrorSchema(RecyclerView.ViewHolder viewHolder, String errorMessage, final View.OnClickListener onClickListener)
        {
                //获取尾部加载组件的管理器
                PostsAdapter.FooterViewHolder footerViewHolder = (PostsAdapter.FooterViewHolder) viewHolder;
                //获取子组件
                final ProgressBar itemListProgressBar = footerViewHolder.getProgressBar();
                final ImageView itemListInfoIcon = footerViewHolder.getImageView();
                final TextView itemListInfoText = footerViewHolder.getTextView();
                final View itemView = footerViewHolder.getItemView();

                //切换组件们的显示状态
                itemListProgressBar.setVisibility(View.INVISIBLE);
                itemListInfoIcon.setVisibility(View.VISIBLE);
                itemListInfoText.setVisibility(View.VISIBLE);
                itemListInfoText.setText(errorMessage);

                itemView.setOnClickListener(new
                                                    View.OnClickListener()
                                                    {
                                                            @Override
                                                            public void onClick(View v)
                                                            {
                                                                    //切换组件们的显示状态
                                                                    itemListProgressBar.setVisibility(View.VISIBLE);
                                                                    itemListInfoIcon.setVisibility(View.INVISIBLE);
                                                                    itemListInfoText.setVisibility(View.INVISIBLE);
                                                                    onClickListener.onClick(v);
                                                            }
                                                    });


        }

        /**
         * 数据列表已到底的时候调用, 显示错误信息
         *
         * @param viewHolder
         */
        public static void setupNotMoreErrorSchema(RecyclerView.ViewHolder viewHolder, String errorMessage)
        {
                //获取尾部加载组件的管理器
                PostsAdapter.FooterViewHolder footerViewHolder = (PostsAdapter.FooterViewHolder) viewHolder;
                //获取子组件
                final ProgressBar itemListProgressBar = footerViewHolder.getProgressBar();
                final ImageView itemListInfoIcon = footerViewHolder.getImageView();
                final TextView itemListInfoText = footerViewHolder.getTextView();
                final View itemView = footerViewHolder.getItemView();

                //切换组件们的显示状态
                itemListProgressBar.setVisibility(View.INVISIBLE);
                itemListInfoIcon.setVisibility(View.VISIBLE);
                itemListInfoText.setVisibility(View.VISIBLE);
                itemListInfoText.setText(errorMessage);
                //删除组件上的监听器
                itemView.setOnClickListener(null);

        }

}
