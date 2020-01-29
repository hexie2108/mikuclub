package org.mikuclub.app.adapter.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.mikuclub.app.adapter.viewHolder.FooterViewHolder;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 开启了底部组件的多布局 recyclerView适配器
 * multiple layout recyclerView adapter with tail view activated
 */
public abstract class BaseAdapterWithFooter extends BaseAdapter
{

        /**
         * 构建函数 default constructor
         *
         * @param list
         * @param context
         */
        protected BaseAdapterWithFooter(List list, Context context)
        {
                super(list, context);
                //开启尾部
                setFooterRow(1);
        }

        @Override
        protected RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent)
        {
                //加载布局
                View view = getAdpterInflater().inflate(R.layout.list_item_info_util, parent, false);
                //生成控制器
                FooterViewHolder holder = new FooterViewHolder(view);
                return holder;
        }

        @Override
        protected void onBindFooterViewHolder(RecyclerView.ViewHolder holder, boolean isLoading, boolean notMoreError, boolean internetError, final View.OnClickListener internetErrorListener)
        {
                FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
                //获取子组件
                final ProgressBar itemListProgressBar = footerViewHolder.getProgressBar();
                final ImageView itemListInfoIcon = footerViewHolder.getImageView();
                final TextView itemListInfoText = footerViewHolder.getTextView();
                final View itemView = footerViewHolder.getItemView();

                //如果是在正常加载
                if (isLoading)
                {
                        //切换组件们的显示状态
                        itemView.setVisibility(View.VISIBLE);
                        //显示进度条
                        itemListProgressBar.setVisibility(View.VISIBLE);
                        //隐藏错误信息
                        itemListInfoIcon.setVisibility(View.INVISIBLE);
                        itemListInfoText.setVisibility(View.INVISIBLE);
                        //删除组件上的监听器
                        itemView.setOnClickListener(null);
                }
                //如果有错误
                else if (notMoreError || internetError)
                {
                        //切换组件们的显示状态
                        itemView.setVisibility(View.VISIBLE);
                        //隐藏进度条
                        itemListProgressBar.setVisibility(View.INVISIBLE);
                        //显示错误信息
                        itemListInfoIcon.setVisibility(View.VISIBLE);
                        itemListInfoText.setVisibility(View.VISIBLE);

                        String errorMessage;
                        //没有更多错误
                        if (notMoreError)
                        {
                                errorMessage = getNotMoreErrorMessage();
                                //移除点击监听
                                itemView.setOnClickListener(null);
                        }
                        //网络错误
                        else
                        {
                                errorMessage = getInternetErrorMessage();
                                //绑定监听器
                                itemView.setOnClickListener(internetErrorListener);
                        }
                        //更改显示文字
                        itemListInfoText.setText(errorMessage);
                }
                //恢复默认 隐藏状态
                else
                {
                        //隐藏尾部组件
                        itemView.setVisibility(View.INVISIBLE);
                        //删除组件上的监听器
                        itemView.setOnClickListener(null);

                }
        }


        @Override
        protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent)
        {
                return null;
        }

        @Override
        protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position)
        {

        }
}
