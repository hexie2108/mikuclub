package org.mikuclub.app.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.ui.activity.ImageActivity;
import org.mikuclub.app.ui.activity.PostActivity;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import me.wcy.htmltext.OnTagClickListener;
import mikuclub.app.R;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * 文章页 主内容碎片
 */
public class PostMainFragment extends Fragment
{

        private TextView postTitle;
        private TextView postDate;
        private TextView postViews;
        private TextView postCountComments;
        private TextView postCountLike;
        private ImageView postAuthorImg;
        private TextView postAuthorName;
        private TextView postDescription;

        private Post post;


        public PostMainFragment()
        {
                // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
                // Inflate the layout for this fragment
                return inflater.inflate(R.layout.fragment_post_main, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
                super.onViewCreated(view, savedInstanceState);

                //从活动中获取文章数据
                post = ((PostActivity)getActivity()).getPost();

                postTitle = view.findViewById(R.id.post_title);
                postDate= view.findViewById(R.id.post_date);
                postViews= view.findViewById(R.id.post_views);
                postCountComments= view.findViewById(R.id.post_count_comments);
                postCountLike= view.findViewById(R.id.post_count_like);
                postAuthorImg = view.findViewById(R.id.post_author_img);
                postAuthorName = view.findViewById(R.id.post_author_name);
                postDescription= view.findViewById(R.id.post_description);


                initPost();
        }

        /**
         * 初始化文章描述
         */
        private void initPost(){

                postTitle.setText(post.getTitle().getRendered());

                String dateString = new SimpleDateFormat("yy-MM-dd HH:mm").format(post.getDate());
                postDate.setText(dateString);

                //获取文章元数据
                Post.Metadata metadata = post.getMetadata();

                if(metadata.getViews() != null)
                {
                        postViews.setText(metadata.getViews().get(0).toString()+" 查看");
                }
                if(metadata.getCount_comments() != null)
                {
                        postCountComments.setText(metadata.getCount_comments().get(0).toString()+" 评论");
                }
                if(metadata.getCount_like()!= null){
                        postCountLike.setText(metadata.getCount_like().get(0).toString()+" 点赞");
                }

                postAuthorName.setText(metadata.getAuthor().get(0).getDisplay_name());

                //确保给地址添加上https协议
                String avatarSrc = HttpUtils.checkAndAddHttpsProtocol(metadata.getAuthor().get(0).getAvatar_src());
                //获取头像
                GlideImageUtils.getSquareImg(getActivity(), postAuthorImg, avatarSrc);

                //开启超链接支持
                postDescription.setMovementMethod(LinkMovementMethod.getInstance());
                String htmlDescription;
                //如果描述元数据存在
                if(metadata.getContent()!= null){
                        htmlDescription = metadata.getContent().get(0);
                }
                //如果不存在 则只能去获取已被格式化的文章描述
                else{
                        htmlDescription = post.getContent().getRendered();
                }

                //解析 html描述
                HttpUtils.parseHtml(getActivity(), htmlDescription, postDescription, new OnTagClickListener()
                {
                        //设置 点击图片tag的动作
                        @Override
                        public void onImageClick(Context context, List<String> imagesSrc, int position)
                        {
                                //新建列表
                                ArrayList<String> newImagesSrc = new ArrayList<>();
                                //截取当前位置和后续位置的地址
                                newImagesSrc.addAll(imagesSrc.subList(position, imagesSrc.size()));
                                //然后再添加 开头位置 到 当前位置-1 的地址
                                newImagesSrc.addAll(imagesSrc.subList(0, position));
                                //以此达到重建新列表的目标

                                //启动单独的图片查看页面
                                ImageActivity.startAction(getActivity(), newImagesSrc );


                        }
                        //设置点击链接tag的动作
                        @Override
                        public void onLinkClick(Context context, String url)
                        {
                                // link click
                                Uri uri = Uri.parse(HttpUtils.checkAndAddHttpsProtocol(url));
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                intent.setData(uri);
                                startActivity(intent);
                        }
                });






        }

}
