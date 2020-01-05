package org.mikuclub.app.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.ui.activity.ImageActivity;
import org.mikuclub.app.ui.activity.PostActivity;
import org.mikuclub.app.utils.GeneralUtils;
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
        /*变量*/
        //当前页面需要的文章数据
        private Post post;

        /*组件*/
        private TextView postTitle;
        private TextView postDate;
        private TextView postViews;
        private TextView postCountComments;
        private ImageView postAuthorImg;
        private TextView postAuthorName;
        private TextView postSource;
        private TextView postDescription;
        private TextView postCountLike;
        private Button postCountLikeButton;
        private TextView postCountShare;
        private Button postCountShareButton;
        private TextView postCountFailDown;
        private Button postCountFailDownButton;
        private TextView postBilibili;
        private Button postBilibiliButton;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
                // 为fragment加载主布局
                return inflater.inflate(R.layout.fragment_post_main, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
                super.onViewCreated(view, savedInstanceState);

                postTitle = view.findViewById(R.id.post_title);
                postDate = view.findViewById(R.id.post_date);
                postViews = view.findViewById(R.id.post_views);
                postCountComments = view.findViewById(R.id.post_count_comments);
                postAuthorImg = view.findViewById(R.id.post_author_img);
                postAuthorName = view.findViewById(R.id.post_author_name);
                postSource = view.findViewById(R.id.post_source);
                postDescription = view.findViewById(R.id.post_description);
                postCountLike = view.findViewById(R.id.post_count_like);
                postCountLikeButton = view.findViewById(R.id.post_count_like_button);
                postCountShare = view.findViewById(R.id.post_count_share);
                postCountShareButton = view.findViewById(R.id.post_count_share_button);
                postCountFailDown = view.findViewById(R.id.post_count_fail_down);
                postCountFailDownButton = view.findViewById(R.id.post_count_fail_down_button);
                postBilibili = view.findViewById(R.id.post_bilibili);
                postBilibiliButton = view.findViewById(R.id.post_bilibili_button);

                //从活动中获取文章数据
                post = ((PostActivity) getActivity()).getPost();

                initPost();
        }

        /**
         * 初始化文章描述
         */
        private void initPost()
        {

                postTitle.setText(post.getTitle().getRendered());

                String dateString = new SimpleDateFormat(GlobalConfig.DATE_FORMAT).format(post.getDate());
                postDate.setText(dateString);

                //获取文章元数据
                Post.Metadata metadata = post.getMetadata();
                //如果数据不是空
                if (metadata.getViews() != null)
                {
                        postViews.setText(metadata.getViews().get(0).toString() + " 次查看");
                }
                if (metadata.getCount_comments() != null)
                {
                        postCountComments.setText(metadata.getCount_comments().get(0).toString() + " 条评论");
                }

                //设置作者信息
                postAuthorName.setText(metadata.getAuthor().get(0).getDisplay_name());
                //确保给地址添加上https协议
                String avatarSrc = HttpUtils.checkAndAddHttpsProtocol(metadata.getAuthor().get(0).getAvatar_src());
                //获取头像
                GlideImageUtils.getSquareImg(getActivity(), postAuthorImg, avatarSrc);

                //设置工具栏信息
                //不是空的 或者默认0
                if (!GeneralUtils.listIsNullOrHasEmptyElement(metadata.getCount_like()))
                {
                        postCountLike.setText(metadata.getCount_like().get(0).toString() + " 次点赞");
                }
                //不是空的或默认0
                if (!GeneralUtils.listIsNullOrHasEmptyElement(metadata.getFail_time()))
                {
                        if (metadata.getFail_time().get(0) > 0)
                        {
                                postCountFailDown.setText(metadata.getFail_time().get(0).toString() + " 次失效");
                        }
                }
                //在线视频不是空的
                if (!GeneralUtils.listIsNullOrHasEmptyElement(metadata.getVideo()))
                {
                        String videoSrc = metadata.getVideo().get(0);
                        //确如果是 b站地址
                        if (videoSrc.indexOf("av") != -1 && videoSrc.indexOf("cid") != -1)
                        {
                                //截取av号
                                String av = videoSrc.split(",")[0];
                                final String bilibiliAppSrc = GlobalConfig.BILIBILI_APP_WAKE_URL + av.substring(2);
                                final String bilibiliWebSrc = GlobalConfig.BILIBILI_HOST + av;
                                //监听按钮点击
                                postBilibiliButton.setOnClickListener(v -> {
                                        //启动第三方应用
                                        GeneralUtils.startWebViewIntent(getActivity(), bilibiliAppSrc, bilibiliWebSrc);
                                });

                                //显示b站视频按钮
                                postBilibiliButton.setVisibility(View.VISIBLE);
                                postBilibili.setVisibility(View.VISIBLE);
                        }
                }

                //如果有来源地址
                if (!GeneralUtils.listIsNullOrHasEmptyElement(metadata.getSource()))
                {
                        //设置来源地址
                        postSource.setText(metadata.getSource().get(0));
                }
                //如果有来源说明
                if (!GeneralUtils.listIsNullOrHasEmptyElement(metadata.getSource_name()))
                {
                        //再加上来源说明
                        postSource.setText(postSource.getText().toString() + "\n" + metadata.getSource_name().get(0));
                }
                //如果没有任何来源说明
                if (postSource.getText().toString().isEmpty())
                {
                        //隐藏组件
                        postSource.setVisibility(View.GONE);
                }

                //开启超链接支持
                postDescription.setMovementMethod(LinkMovementMethod.getInstance());
                String htmlDescription;
                //如果描述元数据存在
                if (metadata.getContent() != null)
                {
                        htmlDescription = metadata.getContent().get(0);
                }
                //如果不存在 则只能去获取已被格式化的文章描述
                else
                {
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
                                ImageActivity.startAction(getActivity(), newImagesSrc);
                        }

                        //设置点击链接tag的动作
                        @Override
                        public void onLinkClick(Context context, String url)
                        {
                                //检测链接格式时候正确
                                url = HttpUtils.checkAndAddHttpsProtocol(url);
                                //启动第三方应用
                                GeneralUtils.startWebViewIntent(context, url, "");
                        }
                });
        }
}
