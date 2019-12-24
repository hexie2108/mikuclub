package org.mikuclub.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.mikuclub.app.adapters.viewHolder.FooterViewHolder;
import org.mikuclub.app.adapters.viewHolder.PostViewHolder;
import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.ui.activity.PostActivity;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
        //正常内容
        private final static int TYPE_ITEM = 0;
        //尾部加载刷新布局
        private final static int TYPE_FOOTER = 1;

        //数据列表
        private List<Post> list;
        //上下文
        private Context mConxt;

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
         * @param list
         */
        public PostsAdapter(List<Post> list)
        {
                this.list = list;
        }


        @Override
        public int getItemViewType(int position)
        {
                if (position == list.size())
                {
                        return TYPE_FOOTER;
                }
                else
                {
                        return TYPE_ITEM;
                }

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
                //获取保存上下文
                if (mConxt == null)
                {
                        mConxt = parent.getContext();
                }

                //创建控制器
                final RecyclerView.ViewHolder holder;
                //如果是普通数据类型
                if (viewType == TYPE_ITEM)
                {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_posts, parent, false);
                        holder = new PostViewHolder(view);
                        ((PostViewHolder) holder).getCardView().setOnClickListener(new
                                                                                           View.OnClickListener()
                                                                                           {
                                                                                                   @Override
                                                                                                   public void onClick(View v)
                                                                                                   {
                                                                                                           //获取数据在列表中的位置
                                                                                                           int position = holder.getAdapterPosition();
                                                                                                           Post post = list.get(position);
                                                                                                           //启动 文章页
                                                                                                           PostActivity.startAction(mConxt, post);
                                                                                                   }
                                                                                           });
                }
                //如果是footer
                else
                {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_info_util, parent, false);
                        holder = new FooterViewHolder(view);
                }
                return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
        {
                //需要展示对应子视图的时候
                //如果是普通数据组件
                if (holder instanceof PostViewHolder)
                {
                        bindPostViewHolder(holder, position);

                }
                else if(holder instanceof  FooterViewHolder){
                        AdapterUtils.bindFooterViewHolder(holder, notMoreError, internetError, internetErrorListener);
                }

        }

        @Override
        public int getItemCount()
        {
                //获取列表长度
                //因为增加了 footer组件, 所以长度要加上footer占据的数量
                return list.size() + footerRow;
        }


        private void bindPostViewHolder(RecyclerView.ViewHolder holder, int position)
        {
                PostViewHolder postViewHolder = (PostViewHolder) holder;
                //先从列表获取对应位置的数据
                Post post = list.get(position);
                //为视图设置各项数据
                postViewHolder.getItemText().setText(post.getTitle().getRendered());
                String imgUrl = post.getMetadata().getThumbnail_src().get(0);
                //加载远程图片
                GlideImageUtils.get(mConxt, postViewHolder.getItemImage(), imgUrl);
        }

        private void bindFooterViewHolder(RecyclerView.ViewHolder holder)
        {
                FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
                if (notMoreError)
                {
                        String errorMessage = "已经到底了~";

                        //获取子组件
                        ProgressBar itemListProgressBar = footerViewHolder.getProgressBar();
                        ImageView itemListInfoIcon = footerViewHolder.getImageView();
                        TextView itemListInfoText = footerViewHolder.getTextView();
                        View itemView = footerViewHolder.getItemView();

                        //切换组件们的显示状态
                        itemListProgressBar.setVisibility(View.INVISIBLE);
                        itemListInfoIcon.setVisibility(View.VISIBLE);
                        itemListInfoText.setVisibility(View.VISIBLE);
                        itemListInfoText.setText(errorMessage);
                        //删除组件上的监听器
                        itemView.setOnClickListener(null);
                }
                else if (internetError)
                {

                        String errorMessage = "加载失败, 请点击重试";

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
