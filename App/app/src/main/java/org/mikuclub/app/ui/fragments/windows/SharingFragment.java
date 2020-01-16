package org.mikuclub.app.ui.fragments.windows;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.resources.Post;
import org.mikuclub.app.ui.activity.PostActivity;
import org.mikuclub.app.ui.fragments.PostMainFragment;
import org.mikuclub.app.utils.ClipboardUtils;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.ScreenUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.utils.social.TencentUtils;
import org.mikuclub.app.utils.social.WeiboUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import mikuclub.app.R;

/**
 * 文章页 分享窗口碎片
 */
public class SharingFragment extends BottomSheetDialogFragment
{

        /*变量*/
        //获取文章数据
        private Post post;
        //分享链接
        private String sharingUrl;
        //QQ和qq空间回调监听器
        private IUiListener qqShareListener;
        //微博回调监听器
        private WbShareCallback weiboShareListener;

        /*组件*/
        private MaterialButton shareQQButton;
        private MaterialButton shareQQZoneButton;
        private MaterialButton shareLinkButton;
        private MaterialButton shareWeiboButton;
        private MaterialButton returnButton;


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
                // 为fragment加载主布局
                return inflater.inflate(R.layout.fragment_sharing_windows, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {

                super.onViewCreated(view, savedInstanceState);

                shareQQButton = view.findViewById(R.id.share_qq_button);
                shareQQZoneButton = view.findViewById(R.id.share_qq_zone_button);
                shareLinkButton = view.findViewById(R.id.share_link_button);
                shareWeiboButton = view.findViewById(R.id.share_weibo_button);
                returnButton = view.findViewById(R.id.return_button);

                //获取文章的数据
                post = ((PostActivity) getActivity()).getPost();
                //生成分享地址
                sharingUrl = GlobalConfig.Server.HOST + post.getId();

                //动态调整布局高度
                final View myView = view;
                myView.post(() -> GeneralUtils.setMaxHeightOfLayout(getActivity(), myView, GlobalConfig.HEIGHT_PERCENTAGE_OF_FLOAT_WINDOWS));


                initButtons();
        }

        /**
         * 初始化按钮
         */
        private void initButtons()
        {

                initShareQQButton();
                initShareQQZoneButton();
                initWeiboButton();
                initShareLinkButton();


                //绑定返回按钮
                returnButton.setOnClickListener(v -> {
                        //关闭窗口
                        SharingFragment.this.dismiss();
                });
        }


        /**
         * QQ分享按钮
         */
        private void initShareQQButton()
        {
                //初始化QQ和QQ空间回调监听器
                qqShareListener = new IUiListener()
                {
                        @Override
                        public void onComplete(Object o)
                        {
                                LogUtils.v("QQ分享成功");
                                ToastUtils.shortToast("分享成功");
                                afterSharing();
                        }

                        @Override
                        public void onError(UiError uiError)
                        {
                                LogUtils.v("QQ分享错误");
                        }

                        @Override
                        public void onCancel()
                        {
                                LogUtils.v("QQ分享取消");
                        }
                };

                Bundle params = new Bundle();
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                params.putString(QQShare.SHARE_TO_QQ_TITLE, post.getTitle().getRendered());
                params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "");
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, sharingUrl);
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, post.getMetadata().getThumbnail_src().get(0));


                //绑定返回按钮
                shareQQButton.setOnClickListener(v -> {
                        TencentUtils.getInstance().shareToQQ(getActivity(), params, qqShareListener);
                });
        }

        /**
         * QQ空间分享按钮
         */
        private void initShareQQZoneButton()
        {
                //qq空间分享使用qq的返回结果监听器

                Bundle params = new Bundle();
                params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
                params.putString(QzoneShare.SHARE_TO_QQ_TITLE, post.getTitle().getRendered());
                params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "");
                params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, sharingUrl);
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, post.getMetadata().getThumbnail_src().get(0));
                //只使用一张图
                ArrayList<String> images = new ArrayList<>(Arrays.asList(post.getMetadata().getImages_src().get(0)));
                params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, images);

                //绑定返回按钮
                shareQQZoneButton.setOnClickListener(v -> {
                        TencentUtils.getInstance().shareToQzone(getActivity(), params, qqShareListener);
                });
        }

        /**
         * 微博分享按钮
         */
        private void initWeiboButton()
        {


               weiboShareListener = new WbShareCallback(){
                        @Override
                        public void onComplete()
                        {
                                LogUtils.v("微博分享成功");
                                ToastUtils.shortToast("分享成功");
                                afterSharing();
                        }

                        @Override
                        public void onError(com.sina.weibo.sdk.common.UiError uiError)
                        {
                                LogUtils.v("微博分享错误");
                        }

                        @Override
                        public void onCancel()
                        {
                                LogUtils.v("微博分享取消");
                        }
                };

                WeiboMultiMessage message = new WeiboMultiMessage();
                WebpageObject webObject = new WebpageObject();
                webObject.identify = UUID.randomUUID().toString();
                webObject.title = post.getTitle().getRendered();
                webObject.description = "";
                webObject.actionUrl = sharingUrl;
                webObject.defaultText = "分享链接";
                message.mediaObject = webObject;

                //绑定返回按钮
                shareWeiboButton.setOnClickListener(v -> {
                        WeiboUtils.getInstance(getActivity()).shareMessage(message, false );
                });
        }

        /**
         * 复制链接按钮
         */
        private void initShareLinkButton()
        {
                //绑定返回按钮
                shareLinkButton.setOnClickListener(v -> {
                        //复制到剪切板
                        ClipboardUtils.setText(post.getTitle().getRendered() + " " + sharingUrl);
                        //生成用户提示
                        ToastUtils.shortToast("已复分享制链接到剪切板");
                        //完成分享后的动作
                        afterSharing();
                });
        }

        /**
         * 完成分享后的动作
         */
        private void afterSharing()
        {
                //关闭窗口
                SharingFragment.this.dismiss();
                //通过父活动的viewpager适配器获取当前文章内容碎片
                PostMainFragment fragment = ((PostActivity) getActivity()).getPostMainFragment();
                //通知文章内容碎片更新
                fragment.executedShareAction();
        }


        /**
         * 禁止浮动页面滑动
         *
         * @param dialog
         * @param style
         */
        @Override
        public void setupDialog(Dialog dialog, int style)
        {
                ScreenUtils.disableDraggingOfBottomSheetDialogFragment(dialog);
        }


        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
        {
                super.onActivityResult(requestCode, resultCode, data);

                if (requestCode == Constants.REQUEST_QQ_SHARE || requestCode == Constants.REQUEST_QZONE_SHARE)
                {
                        LogUtils.v("QQ分享回调");
                        Tencent.onActivityResultData(requestCode, resultCode, data, qqShareListener);
                }
                else
                {
                        LogUtils.v("微博登陆回调");
                        WeiboUtils.getInstance(getActivity()).doResultIntent(data, weiboShareListener);
                }

        }

        /**
         * 本碎片的静态启动方法
         *
         * @return
         */
        public static SharingFragment startAction()
        {
                SharingFragment fragment = new SharingFragment();
                return fragment;
        }

}