package org.mikuclub.app.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.mikuclub.app.adapters.viewHolder.FooterViewHolder;

import androidx.recyclerview.widget.RecyclerView;

/**
 *
 */
public class AdapterUtils
{
        /**
         * 错误信息显示
         * @param holder
         * @param notMoreError 没有更多内容的错误
         * @param notMoreHideFooter 在没有更多内容的情况下是否隐藏尾部
         * @param internetError 网络错误
         * @param internetErrorListener 网络错误监听器
         */
        public static void bindFooterViewHolder(RecyclerView.ViewHolder holder, boolean notMoreError, boolean notMoreHideFooter, boolean internetError, final View.OnClickListener internetErrorListener)
        {
                FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
                //获取子组件
                final ProgressBar itemListProgressBar = footerViewHolder.getProgressBar();
                final ImageView itemListInfoIcon = footerViewHolder.getImageView();
                final TextView itemListInfoText = footerViewHolder.getTextView();
                final View itemView = footerViewHolder.getItemView();
                //没有更多的 错误
                if (notMoreError)
                {
                        //如果需要隐藏尾部
                        if(notMoreHideFooter){
                                itemView.setVisibility(View.INVISIBLE);
                        }
                        else{
                                String errorMessage = "已经到底了~";
                                //切换组件们的显示状态
                                itemListProgressBar.setVisibility(View.INVISIBLE);
                                itemListInfoIcon.setVisibility(View.VISIBLE);
                                itemListInfoText.setVisibility(View.VISIBLE);
                                itemListInfoText.setText(errorMessage);
                        }
                        //删除组件上的监听器
                        itemView.setOnClickListener(null);

                }
                //网络错误
                else if (internetError)
                {

                        String errorMessage = "加载失败, 请点击重试";

                        //切换组件们的显示状态
                        itemListProgressBar.setVisibility(View.INVISIBLE);
                        itemListInfoIcon.setVisibility(View.VISIBLE);
                        itemListInfoText.setVisibility(View.VISIBLE);
                        itemListInfoText.setText(errorMessage);
                        //点击监听器
                        itemView.setOnClickListener(new View.OnClickListener()
                        {
                                @Override
                                public void onClick(View v)
                                {
                                        //切换组件们的显示状态
                                        itemListProgressBar.setVisibility(View.VISIBLE);
                                        itemListInfoIcon.setVisibility(View.INVISIBLE);
                                        itemListInfoText.setVisibility(View.INVISIBLE);
                                        internetErrorListener.onClick(v);
                                }
                        });
                }
                else{

                        //切换组件们的显示状态
                        itemListProgressBar.setVisibility(View.VISIBLE);
                        itemListInfoIcon.setVisibility(View.INVISIBLE);
                        itemListInfoText.setVisibility(View.INVISIBLE);
                        //删除组件上的监听器
                        itemView.setOnClickListener(null);

                }


        }


}
