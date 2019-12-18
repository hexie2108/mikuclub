package org.mikuclub.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.utils.http.Request;

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

        //正常内容视图管理器
        private class PostViewHolder extends RecyclerView.ViewHolder{

                CardView cardView;
                NetworkImageView itemImage;
                TextView itemText;

                public PostViewHolder(@NonNull View itemView)
                {
                        super(itemView);
                        //管理器绑定各项 视图
                        cardView = (CardView) itemView;
                        itemImage = itemView.findViewById(R.id.item_image);
                        itemText =  itemView.findViewById(R.id.item_text);


                }
        }

        //正常内容视图管理器
        public class FooterViewHolder extends RecyclerView.ViewHolder{

                View itemView;
                ProgressBar progressBar;
                ImageView imageView;
                TextView textView;

                public FooterViewHolder(@NonNull View itemView)
                {
                        super(itemView);
                        //管理器绑定各项 视图
                        this.itemView = itemView;
                        progressBar = itemView.findViewById(R.id.list_item_progress_bar);
                        imageView = itemView.findViewById(R.id.list_item_info_icon);
                        textView = itemView.findViewById(R.id.list_item_info_text);
                }

                public View getItemView()
                {
                        return itemView;
                }


                public ProgressBar getProgressBar()
                {
                        return progressBar;
                }


                public ImageView getImageView()
                {
                        return imageView;
                }


                public TextView getTextView()
                {
                        return textView;
                }

        }




        public PostsAdapter(List<Post> list)
        {
                this.list = list;
        }


        @Override
        public int getItemViewType(int position)
        {
                if(position == list.size())
                {
                        return TYPE_FOOTER;
                }
                else{
                        return TYPE_ITEM;
                }

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
                //创建管理器的时候, 趁机绑定视图
                RecyclerView.ViewHolder holder;
                //如果是普通数据类型
                if(viewType == TYPE_ITEM)
                {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_posts, parent, false);
                        holder = new PostViewHolder(view);
                }
                //如果是footer
                else{
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_more, parent, false);
                        holder = new FooterViewHolder(view);
                }
                return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
        {
                //需要展示对应子视图的时候
                //如果是普通数据组件
                if(getItemViewType(position) == TYPE_ITEM)
                {
                        PostViewHolder postViewHolder = (PostViewHolder) holder;
                        //先从列表获取对应位置的数据
                        Post post = list.get(position);
                        //为视图设置各项数据
                        postViewHolder.itemText.setText(post.getTitle().getRendered());
                        String imgUrl = post.getMetadata().getThumbnail_img_src().get(0);
                        //加载远程图片
                        Request.getRemoteImg(postViewHolder.itemImage, imgUrl);
                }
                //如果是footer组件
                else{

                }
        }

        @Override
        public int getItemCount()
        {
                //获取列表长度
                //因为增加了 footer组件, 所以长度+1
                return list.size()+1;
        }
}
