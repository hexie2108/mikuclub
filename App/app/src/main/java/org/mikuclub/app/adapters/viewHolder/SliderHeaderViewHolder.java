package org.mikuclub.app.adapters.viewHolder;

import android.view.View;

import com.zhengsr.viewpagerlib.indicator.RectIndicator;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 文章列表 头部控制器 (显示 置顶文章幻灯片)
 */
public class SliderHeaderViewHolder extends RecyclerView.ViewHolder{

        //首页幻灯片
        private BannerViewPager sliderViewPager;
        private RectIndicator indicator;


        public SliderHeaderViewHolder(@NonNull View itemView)
        {
                super(itemView);
                //管理器绑定各项 视图
                //获取组件
                sliderViewPager = itemView.findViewById(R.id.home_slider_viewpager);
                indicator = itemView.findViewById(R.id.home_slider_indicator);
        }

        public BannerViewPager getSliderViewPager()
        {
                return sliderViewPager;
        }

        public void setSliderViewPager(BannerViewPager sliderViewPager)
        {
                this.sliderViewPager = sliderViewPager;
        }

        public RectIndicator getIndicator()
        {
                return indicator;
        }

        public void setIndicator(RectIndicator indicator)
        {
                this.indicator = indicator;
        }
}