package org.mikuclub.app.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.view.BannerViewPager;

import org.mikuclub.app.adapters.viewHolder.SliderHeaderViewHolder;
import org.mikuclub.app.javaBeans.resources.base.Post;
import org.mikuclub.app.ui.activity.PostActivity;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

public class HomeListAdapter extends PostsAdapter
{

        List<Post> headerPostList;

        /**
         * 构建函数
         *
         * @param list           列表文章
         * @param headerPostList //头部幻灯片文章
         * @param context
         */
        public HomeListAdapter(List<Post> list, List<Post> headerPostList, Context context)
        {
                super(list, context);
                this.headerPostList = headerPostList;
                setHeaderRow(1);
        }

        /**
         * 头部控制器 (首页幻灯片)
         *
         * @param parent
         * @return
         */
        @Override
        protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent)
        {
                //加载布局
                View view = getAdpterInflater().inflate(R.layout.slider_view_home, parent, false);
                SliderHeaderViewHolder holder = new SliderHeaderViewHolder(view);
                initSlider(holder);

                return holder;
        }

        /**
         * 初始化幻灯片
         */
        private void initSlider(SliderHeaderViewHolder holder)
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

                                TextView textView = view.findViewById(R.id.item_text);
                                textView.setText(post.getTitle().getRendered());

                                view.setOnClickListener(v -> {
                                        //启动 文章页
                                        PostActivity.startAction(getAdapterContext(), post);
                                });
                        }
                });

        }


}
