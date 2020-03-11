package org.mikuclub.app.adapter.viewHolder;

import android.view.View;

import com.google.android.gms.ads.AdView;
import com.zhengsr.viewpagerlib.indicator.RectIndicator;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 *  主页 头部组件控制器 (显示 幻灯片)
 *  Home page head view holder (show slider)
 */
public class HomeSliderHeaderViewHolder extends RecyclerView.ViewHolder{

        //首页幻灯片
        private BannerViewPager sliderViewPager;
        private RectIndicator indicator;
        private AdView adSenseHome;


        public HomeSliderHeaderViewHolder(@NonNull View itemView)
        {
                super(itemView);
                //管理器绑定各项 组件
                sliderViewPager = itemView.findViewById(R.id.home_slider_viewpager);
                indicator = itemView.findViewById(R.id.home_slider_indicator);
                adSenseHome = itemView.findViewById(R.id.ad_sense_home);
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

        public AdView getAdSenseHome()
        {
                return adSenseHome;
        }

        public void setAdSenseHome(AdView adSenseHome)
        {
                this.adSenseHome = adSenseHome;
        }
}