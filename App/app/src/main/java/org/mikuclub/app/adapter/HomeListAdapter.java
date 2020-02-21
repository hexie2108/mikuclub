package org.mikuclub.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import org.mikuclub.app.adapter.viewHolder.HomeSliderHeaderViewHolder;
import org.mikuclub.app.javaBeans.response.baseResource.Post;
import org.mikuclub.app.ui.activity.PostActivity;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 主页页面 适配器
 * 头部显示幻灯片置顶文章列表
 * 主体显示 文章列表
 * home page: recyclerView adapter
 * header displays slider of sticky posts
 * the main body displays a list of post
 */
public class HomeListAdapter extends PostAdapter
{

        List<Post> headerPostList;

        /**
         * 构建函数 default constructor
         *  @param headerPostList 头部幻灯片文章
         * @param list 列表文章
         * @param context
         */
        public HomeListAdapter(List<Post> headerPostList, List<Post> list, Context context)
        {
                super(context, list);
                this.headerPostList = headerPostList;
                //开启头部组件显示
                setHeaderRow(1);
        }

        /**
         * 头部控制器 (首页幻灯片)
         */
        @Override
        protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent)
        {
                //加载布局
                View view = getAdpterInflater().inflate(R.layout.slider_view_home, parent, false);
                HomeSliderHeaderViewHolder holder = new HomeSliderHeaderViewHolder(view);
                initSlider(holder);

                return holder;
        }

        /**
         * 初始化幻灯片
         */
        private void initSlider(HomeSliderHeaderViewHolder holder)
        {

                BannerViewPager bannerViewPager = holder.getSliderViewPager();
                bannerViewPager.addIndicator(holder.getIndicator());
                bannerViewPager.setPageListener(R.layout.slider_view_item_home, headerPostList, new PageHelperListener<Post>()
                {
                        @Override
                        public void bindView(View view, Post post, int position)
                        {
                                //获取原图地址
                                String imageUrl = post.getMetadata().getImages_src().get(0);
                                //获取缩微图地址
                                String thumbnailSrc = post.getMetadata().getThumbnail_src().get(0);
                                //获取图片组件
                                ImageView imageView = view.findViewById(R.id.item_image);
                                //加载图片
                                GlideImageUtils.getWithThumbnail(getAdapterContext(), imageView, imageUrl, thumbnailSrc);
                                //设置标题
                                TextView textView = view.findViewById(R.id.item_text);
                                //修复标题中可能存在的被html转义的特殊符号
                                String title = GeneralUtils.unescapeHtml(post.getTitle().getRendered());
                                textView.setText(title);
                                //绑定点击事件监听器
                                view.setOnClickListener(v -> {
                                        //启动 文章页
                                        PostActivity.startAction(getAdapterContext(), post);
                                });
                        }
                });

        }


}
