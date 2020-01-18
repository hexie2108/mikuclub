package org.mikuclub.app.ui.fragments.windows;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.resources.base.Post;
import org.mikuclub.app.ui.activity.PostActivity;
import org.mikuclub.app.utils.ClipboardUtils;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.ScreenUtils;
import org.mikuclub.app.utils.ToastUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import mikuclub.app.R;

/**
 * 文章页 下载窗口碎片
 */
public class DownloadFragment extends BottomSheetDialogFragment
{

        /*变量*/
        //获取文章数据
        private Post post;

        /*组件*/
        private ConstraintLayout down1Box;
        private ConstraintLayout down2Box;
        private TextView DownInfo;
        private TextView down1Title;
        private TextView down2Title;
        private TextView down1PasswordText;
        private TextView down2PasswordText;
        private TextView down1Password;
        private TextView down2Password;
        private TextView down1UnzipPasswordText;
        private TextView down2UnzipPasswordText;
        private TextView down1UnzipPassword;
        private TextView down2UnzipPassword;
        private MaterialButton down1Button;
        private MaterialButton down2Button;
        private TextView downInfo;
        private MaterialButton returnButton;



        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
                // 为fragment加载主布局
                return  inflater.inflate(R.layout.fragment_download_windows, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {

                super.onViewCreated(view, savedInstanceState);

                //绑定组件
                down1Box = view.findViewById(R.id.down1_box);
                down2Box = view.findViewById(R.id.down2_box);
                DownInfo = view.findViewById(R.id.down_info);
                down1Title = view.findViewById(R.id.down1_title);
                down2Title = view.findViewById(R.id.down2_title);
                down1PasswordText = view.findViewById(R.id.down1_password_text);
                down2PasswordText = view.findViewById(R.id.down2_password_text);
                down1Password = view.findViewById(R.id.down1_password);
                down2Password = view.findViewById(R.id.down2_password);
                down1UnzipPasswordText = view.findViewById(R.id.down1_unzip_password_text);
                down2UnzipPasswordText = view.findViewById(R.id.down2_unzip_password_text);
                down1UnzipPassword = view.findViewById(R.id.down1_unzip_password);
                down2UnzipPassword = view.findViewById(R.id.down2_unzip_password);
                down1Button = view.findViewById(R.id.down1_button);
                down2Button = view.findViewById(R.id.down2_button);
                downInfo = view.findViewById(R.id.down_info);
                returnButton = view.findViewById(R.id.return_button);

                //获取文章的数据
                post = ((PostActivity) getActivity()).getPost();


                //初始化父评论
                initDownloadView();

                //动态调整布局高度
                final View myView = view;
                myView.post(() -> GeneralUtils.setMaxHeightOfLayout(getActivity(), myView, GlobalConfig.HEIGHT_PERCENTAGE_OF_FLOAT_WINDOWS));
        }

        /**
         * 初始化下载页面
         */
        private void initDownloadView()
        {
                Post.Metadata metadata = post.getMetadata();
                setubDownloadBox(metadata.getDown(), metadata.getPassword(), metadata.getUnzip_password(), down1Box, down1PasswordText, down1UnzipPasswordText, down1Password, down1UnzipPassword, down1Button);
                setubDownloadBox(metadata.getDown2(), metadata.getPassword2(), metadata.getUnzip_password2(), down2Box, down2PasswordText, down2UnzipPasswordText, down2Password, down2UnzipPassword, down2Button);

                //如果没有访问密码
                if (GeneralUtils.listIsNullOrHasEmptyElement(metadata.getPassword()) && GeneralUtils.listIsNullOrHasEmptyElement(metadata.getPassword2()))
                {
                        //隐藏提示
                        downInfo.setVisibility(View.GONE);
                }

                //绑定返回按钮
                returnButton.setOnClickListener(v -> {
                        //关闭窗口
                        DownloadFragment.this.dismiss();
                });
        }


        /**
         * 单独配置下载窗口
         * @param downList
         * @param passwordList
         * @param unzipPasswordList
         * @param downBox
         * @param downPasswordText
         * @param downUnzipPasswordText
         * @param downPassword
         * @param downUnzipPassword
         * @param downButton
         */
        private void setubDownloadBox(final List<String> downList, List<String> passwordList, List<String> unzipPasswordList, ConstraintLayout downBox, TextView downPasswordText, TextView downUnzipPasswordText, final TextView downPassword, TextView downUnzipPassword, MaterialButton downButton)
        {

                //下载地址 不是空的
                if (!GeneralUtils.listIsNullOrHasEmptyElement(downList))
                {
                        //访问密码不是空的
                        if (!GeneralUtils.listIsNullOrHasEmptyElement(passwordList))
                        {
                                //设置密码
                                downPassword.setText(passwordList.get(0));
                        }
                        else
                        {
                                //隐藏密码组件
                                downPasswordText.setVisibility(View.GONE);
                                downPassword.setVisibility(View.GONE);

                        }
                        //解压密码不是空的
                        if (!GeneralUtils.listIsNullOrHasEmptyElement(unzipPasswordList))
                        {
                                //设置解压密码
                                downUnzipPassword.setText(unzipPasswordList.get(0));
                        }
                        else
                        {
                                //隐藏解压密码组件
                                downUnzipPasswordText.setVisibility(View.GONE);
                                downUnzipPassword.setVisibility(View.GONE);
                        }
                        //获取下载地址
                        final String downUrl = HttpUtils.checkAndAddHttpsProtocol(downList.get(0));
                        View.OnClickListener onClickListener;
                        //检测是否是磁链地址
                        if (downUrl.indexOf("magnet:") != -1 || downUrl.indexOf("ed2k:") != -1 )
                        {
                                downButton.setText("复制磁链");
                                onClickListener = v -> {
                                        //创建字符剪切内容, 因为是磁链, 不需要http头部,所以重新从原列表获取下载地址
                                        ClipboardUtils.setText(downList.get(0));
                                        ToastUtils.shortToast("已复制磁链到剪切板");
                                };
                        }
                        //如果普通下载地址
                        else
                        {

                                //如果是百度盘标准地址
                                if (downUrl.indexOf(GlobalConfig.BAIDU_PAN_URL_VALIDATE_PATH) != -1)
                                {
                                        //更改按钮文字 为百度
                                        downButton.setText("百度网盘");
                                        //设置图标
                                        downButton.setIcon(getResources().getDrawable(R.drawable.baidu_pan));
                                        //绑定动作
                                        onClickListener = v -> {
                                                //如果访问密码不是空
                                                if (!downPassword.getText().toString().isEmpty())
                                                {
                                                        ClipboardUtils.setText(downPassword.getText().toString());
                                                        ToastUtils.shortToast("已复制访问密码到剪切板");
                                                }
                                                // 打开链接的方式

                                                //百度地址 开始切割识别码的位置
                                                String splitSymbole = "/s/";
                                                String identifyUrl = downUrl.substring(downUrl.indexOf(splitSymbole) + splitSymbole.length());
                                                //去除第一位 的1位数 (百度app政策)
                                                identifyUrl = identifyUrl.substring(1);
                                                String baiduUrl = GlobalConfig.BAIDU_PAN_APP_WAKE_URL + "?" + GlobalConfig.BAIDU_PAN_PARAMETER_NAME + "=" + identifyUrl;
                                                //启动第三方应用
                                                GeneralUtils.startWebViewIntent(getActivity(), baiduUrl, downUrl);
                                        };

                                }
                                else
                                {
                                        //设置默认文字
                                        downButton.setText("普通下载");
                                        onClickListener = v -> {
                                                //如果访问密码不是空
                                                if (!downPassword.getText().toString().isEmpty())
                                                {
                                                        ClipboardUtils.setText(downPassword.getText().toString());
                                                        ToastUtils.shortToast("已复制访问密码到剪切板");
                                                }
                                                // 用游览器打开链接
                                                GeneralUtils.startWebViewIntent(getActivity(), downUrl, downUrl);
                                        };
                                }
                        }

                        //绑定点击监听器
                        downButton.setOnClickListener(onClickListener);

                }
                //没有下载地址就直接隐藏这个下载位
                else
                {
                        downBox.setVisibility(View.GONE);
                }

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


        /**
         * 本碎片的静态启动方法
         *
         * @return
         */
        public static DownloadFragment startAction()
        {
                DownloadFragment fragment = new DownloadFragment();
                return fragment;
        }

}
