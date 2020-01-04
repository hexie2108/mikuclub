package org.mikuclub.app.adapters.listener;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import mikuclub.app.R;

public class ManageInfoUtilView
{

        //信息提示组件
        private ConstraintLayout listItemInfoView;
        //信息提示进度条
        private ProgressBar listItemProgressBar;
        //信息提示 图标
        private ImageView listItemInfoIcon;
        //信息提示文本
        private TextView listItemInfoText;

        public ManageInfoUtilView(View root)
        {
                listItemInfoView = root.findViewById(R.id.list_item_info_view);
                listItemProgressBar = root.findViewById(R.id.list_item_progress_bar);
                listItemInfoIcon = root.findViewById(R.id.list_item_info_icon);
                listItemInfoText = root.findViewById(R.id.list_item_info_text);
        }

        /**
         * 显示错误信息
         *
         * @param errorMessage
         * @param onClickListener
         */
        public void setErrorInfo(String errorMessage, View.OnClickListener onClickListener)
        {


                listItemProgressBar.setVisibility(View.INVISIBLE);
                listItemInfoIcon.setVisibility(View.VISIBLE);
                listItemInfoText.setVisibility(View.VISIBLE);
                listItemInfoText.setText(errorMessage);
                //设置监听器
                listItemInfoView.setOnClickListener(onClickListener);
                //显示
                listItemInfoView.setVisibility(View.VISIBLE);
        }

        /**
         * 恢复显示加载进度条
         */
        public void setLoadingInfo()
        {
                listItemProgressBar.setVisibility(View.VISIBLE);
                listItemInfoIcon.setVisibility(View.INVISIBLE);
                listItemInfoText.setVisibility(View.INVISIBLE);
                //移除监听器
                listItemInfoView.setOnClickListener(null);
                //显示
                listItemInfoView.setVisibility(View.VISIBLE);
        }

        /**
         * 设置 本组件的可见性
         * @param visibility
         */
        public void setVisibility(boolean visibility)
        {
                if (visibility)
                {
                        listItemInfoView.setVisibility(View.VISIBLE);
                }
                else
                {
                        listItemInfoView.setVisibility(View.GONE);
                }

        }
}
