package org.mikuclub.app.adapters.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhengsr.viewpagerlib.indicator.TransIndicator;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 文章列表 头部控制器 (显示 置顶文章幻灯片)
 */
public class HeaderViewHolder extends RecyclerView.ViewHolder{

        //首页幻灯片
        private BannerViewPager sliderViewPager;
        private TransIndicator transIndicator;


        public HeaderViewHolder(@NonNull View itemView)
        {
                super(itemView);
                //管理器绑定各项 视图
                //获取组件
                sliderViewPager = itemView.findViewById(R.id.home_slider_viewpager);
                transIndicator = itemView.findViewById(R.id.home_slider_indicator);
        }

        public BannerViewPager getSliderViewPager()
        {
                return sliderViewPager;
        }

        public void setSliderViewPager(BannerViewPager sliderViewPager)
        {
                this.sliderViewPager = sliderViewPager;
        }

        public TransIndicator getTransIndicator()
        {
                return transIndicator;
        }

        public void setTransIndicator(TransIndicator transIndicator)
        {
                this.transIndicator = transIndicator;
        }
}