package org.mikuclub.app.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;

import org.mikuclub.app.adapters.viewHolder.FooterViewHolder;
import org.mikuclub.app.adapters.viewHolder.HeaderViewHolder;
import org.mikuclub.app.adapters.viewHolder.PostViewHolder;
import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.ui.activity.PostActivity;
import org.mikuclub.app.ui.fragments.HomeMainFragment;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

public class HomeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
        //正常内容
        private final static int TYPE_ITEM = 0;
        //头部
        private final static int TYPE_HEADER = 1;
        //尾部
        private final static int TYPE_FOOTER = 2;

        //数据列表
        private List<Post> list;
        private List<Post> headerPostList;

        //上下文
        private Context mConxt;
        //布局创建器
        private LayoutInflater mInflater;
        //头部占据的列数
        private int headerRow = 1;
        //尾部占据的列数
        private int footerRow = 1;

        //错误情况 指示器
        private boolean notMoreError = false;
        private boolean internetError = false;
        //错误情况的 点击事件监听器
        private View.OnClickListener internetErrorListener = null;

        /**
         * 构建函数
         *
         * @param list           列表文章
         * @param headerPostList //头部幻灯片文章
         * @param context
         */
        public HomeListAdapter(List<Post> list, List<Post> headerPostList, Context context )
        {
                this.list = list;
                this.headerPostList = headerPostList;
                this.mConxt = context;
                this.mInflater = LayoutInflater.from(context);
        }


        @Override
        public int getItemViewType(int position)
        {
                //如果是头部行范围内
                if (position < headerRow)
                {
                        return TYPE_HEADER;
                }
                //如果是尾部 ( 位置 = 列表位置+头部位置+尾部位置)
                else if (position == (list.size() - 1) + headerRow + footerRow)
                {
                        return TYPE_FOOTER;
                }
                //默认是普通文章
                else
                {
                        return TYPE_ITEM;
                }

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
                //创建控制器
                RecyclerView.ViewHolder holder;
                //如果是普通数据类型
                if (viewType == TYPE_ITEM)
                {
                        holder = createPostViewHolder(parent);
                }
                //如果是头部类型
                else if (viewType == TYPE_HEADER)
                {
                        holder = createHeaderViewHolder(parent);
                }
                //如果是尾部类型
                else
                {
                        holder = createFooterViewHolder(parent);
                }
                return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
        {

                LogUtils.e("onBindViewHolder");
                LogUtils.e(holder.getClass().getName());
                LogUtils.e(position+"");
                //需要展示对应子视图的时候
                //如果是普通数据组件
                if (holder instanceof PostViewHolder)
                {
                        LogUtils.e("PostViewHolder");
                        bindPostViewHolder(holder, position);
                }
                else if (holder instanceof FooterViewHolder)
                {
                        LogUtils.e("刷新 footer");
                        AdapterUtils.bindFooterViewHolder(holder, notMoreError, internetError, internetErrorListener);
                }
        }

        @Override
        public int getItemCount()
        {
                //获取列表长度, 等于 数据长度 + 头部 + 尾部
                return list.size() + headerRow + footerRow;
        }


        public PostViewHolder createPostViewHolder(ViewGroup parent)
        {
                //加载布局
                View view = mInflater.inflate(R.layout.list_item_posts, parent, false);
                //生成控制器
                final PostViewHolder holder = new PostViewHolder(view);
                //绑定监听器
                ((PostViewHolder) holder).getCardView().setOnClickListener(new View.OnClickListener()
                {
                        @Override
                        public void onClick(View v)
                        {
                                //获取数据在列表中的位置
                                int position = holder.getAdapterPosition();
                                int listPosition = getListPosition(position);
                                //获取数据
                                Post post = list.get(listPosition);
                                //启动 文章页
                                PostActivity.startAction(mConxt, post);
                        }
                });
                return holder;

        }

        /**
         * 首页幻灯片 头部控制器
         * @param parent
         * @return
         */
        public HeaderViewHolder createHeaderViewHolder(ViewGroup parent)
        {
                //加载布局
                View view = mInflater.inflate(R.layout.home_slider_view, parent, false);
                final HeaderViewHolder holder = new HeaderViewHolder(view);

                //配置幻灯片
                PageBean bean = new PageBean.Builder<Post>()
                        .data(headerPostList)
                        .indicator(holder.getTransIndicator())
                        .builder();

                // animation of slider
                // MzTransformer, DepthPageTransformer，ZoomOutPageTransformer
                //sliderViewPager.setPageTransformer(false, new DepthPageTransformer());

                holder.getSliderViewPager().setPageListener(bean, R.layout.home_slider_view_item, new PageHelperListener<Post>()
                {
                        @Override
                        public void getItemView(View view, final Post post)
                        {
                                //获取原图地址
                                String imageUrl = post.getMetadata().getImages_src().get(0);
                                //获取缩微图地址
                                String thumbnailSrc = post.getMetadata().getThumbnail_src().get(0);
                                //获取图片组件
                                ImageView imageView = view.findViewById(R.id.item_image);
                                //加载图片
                                GlideImageUtils.getWithThumbnail(mConxt, imageView, imageUrl, thumbnailSrc);

                                TextView textView = view.findViewById(R.id.item_text);
                                textView.setText(post.getTitle().getRendered());

                                view.setOnClickListener(new View.OnClickListener()
                                {
                                        @Override
                                        public void onClick(View v)
                                        {
                                                //启动 文章页
                                                PostActivity.startAction(mConxt, post);
                                        }
                                });

                        }
                });

                return holder;

        }

        public FooterViewHolder createFooterViewHolder(ViewGroup parent)
        {
                //加载布局
                View view = mInflater.inflate(R.layout.list_item_info_util, parent, false);
                //生成控制器
                FooterViewHolder holder = new FooterViewHolder(view);

                return holder;
        }



        public void bindPostViewHolder(RecyclerView.ViewHolder holder, int position)
        {
                PostViewHolder postViewHolder = (PostViewHolder) holder;

                //把adapter位置转换成对应的list列表地址
                position = getListPosition(position);
                //然后从列表获取对应位置的数据
                Post post = list.get(position);
                //为视图设置各项数据
                postViewHolder.getItemText().setText(post.getTitle().getRendered());
                String imgUrl = post.getMetadata().getThumbnail_src().get(0);
                //加载远程图片
                GlideImageUtils.get(mConxt, postViewHolder.getItemImage(), imgUrl);
        }


        /**
         * 因为头部和尾部骚扰, 创建专用的方法
         * 在适配器内 获取tem对应的数据列表真实位置
         *
         * @param adapterPosition
         * @return
         */
        private int getListPosition(int adapterPosition)
        {
                return adapterPosition - headerRow;
        }

        /**
         * 设置 没有更多 错误
         *
         * @param notMoreError
         */
        public void setNotMoreError(boolean notMoreError)
        {
                this.notMoreError = notMoreError;
        }
        /**
         * 设置 网络错误 和 重试点击监听器
         *
         * @param internetError
         * @param  internetErrorListener
         */
        public void setInternetError(boolean internetError, View.OnClickListener internetErrorListener)
        {
                this.internetError = internetError;
                this.internetErrorListener = internetErrorListener;
        }

}
