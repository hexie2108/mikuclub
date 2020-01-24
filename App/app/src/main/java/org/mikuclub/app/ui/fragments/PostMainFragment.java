package org.mikuclub.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.delegates.PostDelegate;
import org.mikuclub.app.javaBeans.resources.base.Post;
import org.mikuclub.app.ui.activity.AuthorActivity;
import org.mikuclub.app.ui.activity.ImageActivity;
import org.mikuclub.app.ui.activity.PostActivity;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.utils.storage.PreferencesUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import me.wcy.htmltext.OnTagClickListener;
import mikuclub.app.R;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * 文章页 主内容碎片
 */
public class PostMainFragment extends Fragment
{
        public static final int TAG = 8;
        /*变量*/
        //当前页面需要的文章数据
        private Post post;
        private PostDelegate delegate;
        //点赞过的文章id数组
        private List<Integer> likedPostIds;
        private int countLike;


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
        private MaterialButton postCountLikeButton;
        private TextView postCountShare;
        private MaterialButton postCountShareButton;
        private TextView postCountFailDown;
        private MaterialButton postCountFailDownButton;
        private TextView postBilibili;
        private MaterialButton postBilibiliButton;


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
                delegate = new PostDelegate(TAG);

                initPost();
                initLikeButton();
                initShareButton();
                initFailDownButton();

                //通知服务器 增加查看次数计数
                delegate.postViewCount(post.getId());
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
                if (metadata.getCount_sharing() != null)
                {
                        postCountShare.setText(metadata.getCount_sharing().get(0).toString() + " 次分享");
                }

                //创建作者页面点击监听器
                View.OnClickListener authorActivityListener = v -> {
                        AuthorActivity.startAction(getActivity(), metadata.getAuthor().get(0).getAuthor_id(), metadata.getAuthor().get(0).getName());
                };
                //加载头像
                GlideImageUtils.getSquareImg(getActivity(), postAuthorImg, metadata.getAuthor().get(0).getAvatar_src());
                //设置作者信息
                postAuthorName.setText(metadata.getAuthor().get(0).getName());
                //绑定点击事件
                postAuthorImg.setOnClickListener(authorActivityListener);
                postAuthorName.setOnClickListener(authorActivityListener);

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

                                //启动单独的图片查看页面
                                ImageActivity.startAction(getActivity(), imagesSrc, position);
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

        /**
         * 初始化点赞按钮
         */
        private void initLikeButton()
        {

                //获取点赞数据
                String likedPostIdsString = PreferencesUtils.getPostPreference().getString(GlobalConfig.Preferences.POST_LIKED_ARRAY, null);
                //如果相关点赞数据字符串存在
                if (likedPostIdsString != null)
                {
                        //解析为数组
                        likedPostIds = ParserUtils.integerArrayList(likedPostIdsString);
                }

                boolean buttonIsActivated = false;
                //点赞数 不是空的也不是0
                if (!GeneralUtils.listIsNullOrHasEmptyElement(post.getMetadata().getCount_like()))
                {
                        countLike = post.getMetadata().getCount_like().get(0);
                }

                //如果点赞文章数组不是空+这个文章已经点过赞
                if (likedPostIds != null && likedPostIds.contains(post.getId()))
                {
                        buttonIsActivated = true;
                        //检查是否是点过赞
                        countLike++;
                }
                postCountLike.setText(countLike + " 次点赞");


                //根据激活状态 设置 按钮样式和动作
                likeAction(buttonIsActivated);
        }

        /**
         * 点赞操作
         * 根据点赞状态, 变化点赞按钮
         *
         * @param isActivated true=已激活过, false =未激活
         */
        private void likeAction(boolean isActivated)
        {
                int iconColorId = R.color.defaultTextColor;
                //如果是已激活, 设置不同颜色
                if (isActivated)
                {
                        iconColorId = R.color.colorPrimary;
                }

                //更改按钮样式
                postCountLikeButton.setIconTint(ContextCompat.getColorStateList(getActivity(), iconColorId));
                //绑定点击监听器
                postCountLikeButton.setOnClickListener(v -> {
                        //屏蔽按钮
                        postCountLikeButton.setEnabled(false);
                        //设置按钮
                        likeAction(!isActivated);

                        delegate.postLikeCount(new HttpCallBack()
                        {
                                @Override
                                public void onSuccess(String response)
                                {
                                        //更新数组
                                        manageLikedPost(!isActivated);

                                        //提示信息
                                        String toastMessage = "已点赞";
                                        if (isActivated)
                                        {
                                                toastMessage = "已取消点赞";
                                        }
                                        ToastUtils.shortToast(toastMessage);
                                }

                                @Override
                                public void onFinally()
                                {
                                        //激活按钮
                                        postCountLikeButton.setEnabled(true);
                                }

                                @Override
                                public void onCancel()
                                {
                                        onFinally();
                                }
                        }, post.getId(), !isActivated);
                });
        }


        /**
         * 管理操控 点赞文章数组
         *
         * @param isAdd true=添加, false=删除
         */
        private void manageLikedPost(boolean isAdd)
        {
                //如果为null,
                if (likedPostIds == null)
                {
                        //初始化一个新数组
                        likedPostIds = new ArrayList<>();
                }

                //检查id是否已存在
                int position = likedPostIds.indexOf(post.getId());
                //如果id不存在, 而且 是添加操作
                if (position == -1 && isAdd)
                {
                        //添加
                        likedPostIds.add(post.getId());
                        //如果数组长度已超过上限
                        if (likedPostIds.size() > GlobalConfig.Preferences.POST_LIKED_ARRAY_SIZE)
                        {
                                //去除一半
                                likedPostIds = likedPostIds.subList(likedPostIds.size() / 2, likedPostIds.size());
                        }
                }
                else if (position != -1 && !isAdd)
                {
                        //移除
                        likedPostIds.remove(position);
                }
                //保存数组参数里
                PreferencesUtils.getPostPreference()
                        .edit()
                        .putString(GlobalConfig.Preferences.POST_LIKED_ARRAY, ParserUtils.integerArrayListToJson(likedPostIds))
                        .apply();

                //根据当前操作  增加或减少点赞数
                if (isAdd)
                {
                        countLike++;
                }
                else
                {
                        countLike--;
                }
                //更新UI
                postCountLike.setText(countLike + " 次点赞");
        }

        /**
         * 初始化分享按钮
         */
        private void initShareButton()
        {

                //绑定点击监听器
                postCountShareButton.setOnClickListener(v -> {
                        //启动分享窗口
                        ((PostActivity)getActivity()).startSharingWindowsFragment();
                });

        }

        /**
         * 分享完后的动作
         */
        public void afterShareAction()
        {
                //更改按钮颜色
                int iconColorId = R.color.colorPrimary;
                postCountShareButton.setIconTint(ContextCompat.getColorStateList(getActivity(), iconColorId));
                //设置默认分享次数为1
                int countSharing = 1;
                //如果文章不是第一次被分享
                if(post.getMetadata().getCount_sharing()!=null){
                        //就获取当前分享次数 然后 +1
                        countSharing = post.getMetadata().getCount_sharing().get(0) + 1;
                }
                postCountShare.setText(countSharing+" 次分享");
                //通知服务器 增加分享次数计数
                delegate.postShareCount(post.getId());

        }


        /**
         * 初始化反馈下载失效按钮
         */
        private void initFailDownButton(){

                postCountFailDownButton.setOnClickListener(v -> {
                        failDownAction();
                });

        }

        /**
         * 下载失效动作
         */
        private void failDownAction(){
                //更改按钮颜色
                int iconColorId = R.color.colorPrimary;
                postCountFailDownButton.setIconTint(ContextCompat.getColorStateList(getActivity(), iconColorId));
                //注销按钮
                postCountFailDownButton.setEnabled(false);

                HttpCallBack httpCallBack = new HttpCallBack()
                {
                        @Override
                        public void onSuccess(String response)
                        {
                                LogUtils.e(response);
                                ToastUtils.longToast("上报成功, 管理员将会根据上报次数对稿件进行退稿处理, 并通知UP主补档");
                        }
                };

                delegate.postFailDownCount(httpCallBack , post.getId());
        }
}