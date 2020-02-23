package org.mikuclub.app.ui.fragments.windows;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.javaBeans.response.baseResource.Post;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import mikuclub.app.R;

/**
 * 文章页 分享窗口碎片
 * post page: sharing windows fragment
 */
public class SharingFragment extends BottomSheetDialogFragment
{

        /* 变量 local variable */
        //获取文章数据
        private Post post;
        //分享链接
        private String sharingUrl;
        //QQ和qq空间回调监听器
        private IUiListener qqShareListener;
        //微博回调监听器
        private WbShareCallback weiboShareListener;

        /* 组件 views */
        private MaterialButton shareQQButton;
        private MaterialButton shareWeiboButton;
        private MaterialButton shareLinkButton;
        private MaterialButton shareAnotherMethodsButton;

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
                shareAnotherMethodsButton = view.findViewById(R.id.another_methods);
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
         * 初始化分享按钮们
         * init sharing buttons
         */
        private void initButtons()
        {

                initShareQQButton();
                initWeiboButton();
                initShareLinkButton();
                initShareAnotherMethodsButton();

                //绑定返回按钮
                returnButton.setOnClickListener(v -> {
                        //关闭窗口
                        SharingFragment.this.dismiss();
                });
        }


        /**
         * 初始化QQ分享按钮
         * init  QQ sharing button
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

                //修复标题中可能存在的被html转义的特殊符号
                String title = GeneralUtils.unescapeHtml(post.getTitle().getRendered());
                params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
                params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "");
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, sharingUrl);
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, post.getMetadata().getThumbnail_src().get(0));


                //绑定分享按钮
                shareQQButton.setOnClickListener(
                        v -> TencentUtils.getInstance().shareToQQ(getActivity(), params, qqShareListener)
                );
        }


        /**
         * 初始化微博分享按钮
         * init weibo sharing button
         */
        private void initWeiboButton()
        {

                weiboShareListener = new WbShareCallback()
                {
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

                //修复标题中可能存在的被html转义的特殊符号
                String title = GeneralUtils.unescapeHtml(post.getTitle().getRendered());

                WeiboMultiMessage message = new WeiboMultiMessage();
                //设置分享文字
                TextObject textObject = new TextObject();

                textObject.text = "【" + getResources().getString(R.string.app_name) + "】" + title + " ";
                message.textObject = textObject;
                //设置分享链接
                WebpageObject webObject = new WebpageObject();
                webObject.identify = UUID.randomUUID().toString();
                webObject.title = title;
                webObject.description = "";
                webObject.actionUrl = sharingUrl;
                //设置缩微图 图标
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
                //把图片转换成 byte输出流
                ByteArrayOutputStream os = null;
                try
                {
                        os = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 85, os);
                        webObject.thumbData = os.toByteArray();
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }
                finally
                {
                        try
                        {
                                if (os != null)
                                {
                                        os.close();
                                }
                        }
                        catch (IOException e)
                        {
                                e.printStackTrace();
                        }
                }
                message.mediaObject = webObject;

                //绑定分享按钮
                shareWeiboButton.setOnClickListener(
                        v -> WeiboUtils.getInstance(getActivity()).shareMessage(message, false)
                );
        }

        /**
         * 复制链接按钮
         * init general sharing button
         */
        private void initShareLinkButton()
        {
                //修复标题中可能存在的被html转义的特殊符号
                String text = "【初音社】" + GeneralUtils.unescapeHtml(post.getTitle().getRendered()) + " " + sharingUrl;
                //绑定分享按钮
                shareLinkButton.setOnClickListener(v -> {
                        //复制到剪切板
                        ClipboardUtils.setText(text);
                        //生成用户提示
                        ToastUtils.shortToast("已复分享制链接到剪切板");
                        //完成分享后的动作
                        afterSharing();
                });
        }

        /**
         * 初始化其他方式的分享按钮
         * init QQ Zone sharing button
         * qq空间分享直接共用qq的返回结果监听器
         */
        private void initShareAnotherMethodsButton()
        {
                String text = "【初音社】" + GeneralUtils.unescapeHtml(post.getTitle().getRendered()) + " " + sharingUrl;
                shareAnotherMethodsButton.setOnClickListener(v -> {
                        HttpUtils.startSharingIntent(getActivity(), text);
                        //完成分享后的动作
                        afterSharing();
                });

        }

        /**
         * 完成分享后的回调动作
         * callback after sharing
         */
        private void afterSharing()
        {
                //关闭窗口
                SharingFragment.this.dismiss();
                //通过父活动的viewpager适配器获取当前文章内容碎片
                PostMainFragment fragment = ((PostActivity) getActivity()).getPostMainFragment();
                //通知文章内容碎片更新
                fragment.afterShareAction();
        }


        /**
         * 禁止浮动页面滑动
         * Disable sliding of windows
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
         * static method to start current fragment
         *
         * @return
         */
        public static SharingFragment startAction()
        {
                return new SharingFragment();
        }

}
