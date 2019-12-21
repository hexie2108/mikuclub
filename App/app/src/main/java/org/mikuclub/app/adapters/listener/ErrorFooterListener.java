package org.mikuclub.app.adapters.listener;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.mikuclub.app.adapters.PostsAdapter;
import org.mikuclub.app.adapters.viewHolder.FooterViewHolder;

import java.util.List;

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
        public static void setupErrorSchemaWithListener(RecyclerView.ViewHolder viewHolder, String errorMessage, final View.OnClickListener onClickListener)
        {
                //获取尾部加载组件的管理器
                FooterViewHolder footerViewHolder = (FooterViewHolder) viewHolder;
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
        public static void setupErrorSchema(RecyclerView.ViewHolder viewHolder, String errorMessage)
        {
                //获取尾部加载组件的管理器
                FooterViewHolder footerViewHolder = (FooterViewHolder) viewHolder;
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


        /**
         * 重新恢复显示 加载进度条
         *
         * @param recyclerView
         */
        public static void resetFooterErrorView(RecyclerView recyclerView, List recyclerDataList)
        {
                //获取尾部加载组件的管理器
                FooterViewHolder footerViewHolder = (FooterViewHolder) recyclerView.findViewHolderForAdapterPosition(recyclerDataList.size());
                if (footerViewHolder != null)
                {
                        //获取子组件
                        final ProgressBar itemListProgressBar = footerViewHolder.getProgressBar();
                        final ImageView itemListInfoIcon = footerViewHolder.getImageView();
                        final TextView itemListInfoText = footerViewHolder.getTextView();
                        final View itemView = footerViewHolder.getItemView();

                        //恢复组件们的默认显示状态
                        itemListProgressBar.setVisibility(View.VISIBLE);
                        itemListInfoIcon.setVisibility(View.INVISIBLE);
                        itemListInfoText.setVisibility(View.INVISIBLE);
                }

        }


        /**
         * 自动加载发生错误的情况  就停止自动刷新, 绑定监听器允许用户点击重试
         */
        public static void refreshHttpErrorHandler(RecyclerView recyclerView, List recyclerDataList, View.OnClickListener onClickListener)
        {
                ErrorFooterListener.setupErrorSchemaWithListener(recyclerView.findViewHolderForAdapterPosition(recyclerDataList.size()), "加载失败, 请点击重试", onClickListener);
        }

        /**
         * 自动加载 没有更多数据的情况  停止自动刷新, 提示用户对应信息
         */
        public static void refreshNotMoreErrorHandler(RecyclerView recyclerView, List recyclerDataList)
        {
                ErrorFooterListener.setupErrorSchema(recyclerView.findViewHolderForAdapterPosition(recyclerDataList.size()), "已经到底了~");
        }




}
