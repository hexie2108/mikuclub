package org.mikuclub.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.delegate.MediaDelegate;
import org.mikuclub.app.delegate.UserDelegate;
import org.mikuclub.app.javaBeans.parameters.UpdateUserParameters;
import org.mikuclub.app.javaBeans.response.SingleMedia;
import org.mikuclub.app.javaBeans.response.SingleUser;
import org.mikuclub.app.javaBeans.response.WpError;
import org.mikuclub.app.javaBeans.response.baseResource.User;
import org.mikuclub.app.javaBeans.response.baseResource.UserLogin;
import org.mikuclub.app.storage.UserPreferencesUtils;
import org.mikuclub.app.utils.AlertDialogUtils;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.KeyboardUtils;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.utils.ResourcesUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.utils.custom.MyTextWatcher;
import org.mikuclub.app.utils.file.FileUtils;
import org.mikuclub.app.utils.file.ImageCropBean;
import org.mikuclub.app.utils.file.LocalResourceIntent;
import org.mikuclub.app.utils.http.GlideImageUtils;
import org.mikuclub.app.utils.http.HttpCallBack;
import org.mikuclub.app.utils.http.Request;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import mikuclub.app.R;

/**
 * 用户信息页面
 * user profile page
 */
public class UserProfileActivity extends AppCompatActivity
{
        /* 静态变量 Static variable */
        public static final int TAG = 15;

        private static final int TOTAL_REQUEST_COUNT = 4;

        /* 变量 local variable */
        private int requestCount = 0;

        //数据请求代理人
        private UserDelegate delegate;
        private MediaDelegate mediaDelegate;
        private UserLogin user;
        private File newAvatarFile;
        private Uri newAvatarUri;
        private int newAvatarId = 0;

        /* 组件 views */
        private ImageView avatarImg;
        private Button buttonChangeAvatar;
        private TextInputLayout inputEmailLayout;
        private TextInputEditText inputEmail;
        private TextInputLayout inputNicknameLayout;
        private TextInputEditText inputNickname;
        private TextInputLayout inputDescriptionLayout;
        private TextInputEditText inputDescription;
        private TextInputLayout inputPasswordLayout;
        private TextInputEditText inputPassword;
        private Button buttonUpdate;
        private AlertDialog progressDialog;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_user_profile);

                //绑定组件
                Toolbar toolbar = findViewById(R.id.toolbar);
                avatarImg = findViewById(R.id.avatar_img);
                buttonChangeAvatar = findViewById(R.id.button_change_avatar);
                inputEmailLayout = findViewById(R.id.input_email_layout);
                inputEmail = findViewById(R.id.input_email);
                inputNicknameLayout = findViewById(R.id.input_nickname_layout);
                inputNickname = findViewById(R.id.input_nickname);
                inputDescriptionLayout = findViewById(R.id.input_description_layout);
                inputDescription = findViewById(R.id.input_description);
                inputPasswordLayout = findViewById(R.id.input_password_layout);
                inputPassword = findViewById(R.id.input_password);
                buttonUpdate = findViewById(R.id.button_update);
                //创建进度条弹窗
                progressDialog = AlertDialogUtils.createProgressDialog(this, false, false);

                //创建数据请求 代理人
                delegate = new UserDelegate(TAG);
                mediaDelegate = new MediaDelegate(TAG);

                //获取用户数据
                user = UserPreferencesUtils.getUser();

                //替换原版标题栏
                setSupportActionBar(toolbar);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null)
                {
                        //显示标题栏返回键
                        actionBar.setDisplayHomeAsUpEnabled(true);
                }
                //初始化表单
                initInputForm();

        }

        /**
         * 初始化表单
         * init input form
         */
        private void initInputForm()
        {
                //获取加载头像
                GlideImageUtils.getSquareImg(this, avatarImg, user.getAvatar_urls());
                buttonChangeAvatar.setOnClickListener(v -> changeAvatarOnclickListener());

                inputEmail.setText(user.getUser_email());
                inputNickname.setText(user.getUser_display_name());
                //如果用户描述不是空
                if (!GeneralUtils.listIsNullOrHasEmptyElement(user.getUser_meta().getDescription()))
                {
                        //加载用户描述
                        inputDescription.setText(user.getUser_meta().getDescription().get(0));
                }

                //绑定邮箱栏状态监听器
                //自定义 text watcher, 只有在内容变化完成后才会激活回调
                TextWatcher textWatcherOnEmail = new MyTextWatcher(() -> {
                        String content = "";
                        if (inputEmail.getText() != null)
                        {
                                content = inputEmail.getText().toString().trim();
                        }
                        //如果内容是空的  或者 不是个有效的邮箱地址
                        if (content.isEmpty())
                        {
                                //显示错误信息
                                inputEmailLayout.setError(ResourcesUtils.getString(R.string.input_empty_error));
                        }
                        //检测是否不是有效的邮箱地址
                        else if (!GeneralUtils.isValidEmail(content))
                        {
                                //显示错误信息
                                inputEmailLayout.setError(ResourcesUtils.getString(R.string.input_not_valid_email_error_message));
                        }
                        else
                        {
                                //去除错误提示
                                inputEmailLayout.setError(null);
                        }

                        //根据 表单的错误情况 激活或者注销 按钮
                        activeUpdateButtonOnErrorStatus();


                });

                //绑定昵称栏状态监听器
                //自定义 text watcher, 只有在内容变化完成后才会激活回调
                TextWatcher textWatcherOnNickName = new MyTextWatcher(() -> {
                        String content = "";
                        if (inputNickname.getText() != null)
                        {
                                content = inputNickname.getText().toString().trim();
                        }
                        //如果内容是空的  或者 不是个有效的邮箱地址
                        if (content.isEmpty())
                        {
                                //显示错误信息
                                inputNicknameLayout.setError(ResourcesUtils.getString(R.string.input_empty_error));
                        }
                        else
                        {
                                //去除错误提示
                                inputNicknameLayout.setError(null);
                        }
                        //根据 表单的错误情况 激活或者注销 按钮
                        activeUpdateButtonOnErrorStatus();
                });

                //密码栏状态监听器
                //自定义 text watcher, 只有在内容变化完成后才会激活回调
                TextWatcher textWatcherOnPassword = new MyTextWatcher(() -> {

                        String content = "";
                        if (inputPassword.getText() != null)
                        {
                                content = inputPassword.getText().toString().trim();
                        }
                        //如果内容大于0个字 而且 小于6个字
                        if (content.length() > 0 && content.length() < 6)
                        {
                                inputPasswordLayout.setError(ResourcesUtils.getString(R.string.input_not_valid_password_error_message));
                        }
                        else
                        {
                                //去除错误提示
                                inputPasswordLayout.setError(null);
                        }
                        //根据 表单的错误情况 激活或者注销 按钮
                        activeUpdateButtonOnErrorStatus();
                });
                //描述栏状态监听器
                //自定义 text watcher, 只有在内容变化完成后才会激活回调
                TextWatcher textWatcherOnDescription = new MyTextWatcher(() -> {
                        //根据 表单的错误情况 激活或者注销 按钮
                        activeUpdateButtonOnErrorStatus();
                });

                //添加内容变化监听器
                inputEmail.addTextChangedListener(textWatcherOnEmail);
                inputNickname.addTextChangedListener(textWatcherOnNickName);
                inputPassword.addTextChangedListener(textWatcherOnPassword);
                inputDescription.addTextChangedListener(textWatcherOnDescription);
                //按钮绑定点击事件监听
                buttonUpdate.setOnClickListener(v -> updateUser());

        }

        /**
         * 根据 表单的错误情况 激活或者注销 提交按钮
         */
        private void activeUpdateButtonOnErrorStatus()
        {
                //如果任意一个输入框 有错误存在
                if (inputEmailLayout.getError() != null || inputNicknameLayout.getError() != null || inputPasswordLayout.getError() != null)
                {
                        //注销按钮
                        buttonUpdate.setEnabled(false);
                }
                else
                {
                        //激活按钮
                        buttonUpdate.setEnabled(true);
                }
        }

        /**
         * 根据情况 用不同流程更新用户信息
         * 如果有更换过头像 就先上传图片, 之后再更新用户信息
         * 如果没有更换过游戏, 就直接更新用户信息
         */
        private void updateUser()
        {
                //显示进度条加载
                progressDialog.show();
                //获取当前有焦点的组件, 然后隐藏键盘, 如果没有组件有焦点就无视
                KeyboardUtils.hideKeyboard(getCurrentFocus());

                //有新头像文件的话, 需要先上传图片
                if (newAvatarFile != null)
                {
                        uploadAvatar();
                }
                //没有新头像, 只需要更新用户信息
                else
                {
                        requestCount = 3;
                        updateUserInfo();
                }
        }


        /**
         * 上传新头像
         */
        private void uploadAvatar()
        {
                LogUtils.v("开始上传图片");
                HttpCallBack httpCallBack = new HttpCallBack()
                {
                        @Override
                        public void onSuccess(String response)
                        {
                                LogUtils.v("图片上传完成");
                                newAvatarId = ParserUtils.fromJson(response, SingleMedia.class).getBody().getId();

                                //更新新头像的元数据
                                updateAvatarMeta();
                                //删除旧头像,  如果存在的话
                                deleteOldAvatar();
                                //更新用户信息
                                updateUserInfo();

                                //检查所有请求的完成状态
                                checkCompletionStatus();
                        }

                        @Override
                        public void onError(WpError wpError)
                        {
                                ToastUtils.shortToast(ResourcesUtils.getString(R.string.update_image_error_message));
                                onCancel();
                        }

                        @Override
                        public void onHttpError()
                        {
                                onCancel();
                        }

                        @Override
                        public void onCancel()
                        {
                                requestCount = 0;
                                progressDialog.dismiss();
                        }
                };
                mediaDelegate.updateAvatar(httpCallBack, newAvatarFile, user.getId());
        }

        /**
         * 更新头像的元数据metadata
         */
        private void updateAvatarMeta()
        {
                LogUtils.v("开始更新新头像的元数据");
                HttpCallBack httpCallBack = new HttpCallBack()
                {
                        @Override
                        public void onSuccess(String response)
                        {
                                LogUtils.v("更新新头像的元数据成功");
                                //检查所有请求的完成状态
                                checkCompletionStatus();
                        }

                        @Override
                        public void onError(WpError wpError)
                        {
                                ToastUtils.shortToast(ResourcesUtils.getString(R.string.update_image_meta_error_message));
                                onCancel();
                        }

                        @Override
                        public void onHttpError()
                        {
                                onCancel();
                        }

                        @Override
                        public void onCancel()
                        {
                                requestCount = 0;
                                progressDialog.dismiss();
                        }
                };
                mediaDelegate.updateAvatarMeta(httpCallBack, newAvatarId, user.getId());

        }

        /**
         * 请求删除旧头像, 如果存在的话
         */
        private void deleteOldAvatar()
        {

                //如果 用户有存储 对应的旧 头像 id
                if (!GeneralUtils.listIsNullOrHasEmptyElement(user.getUser_meta().getMm_user_avatar()))
                {
                        LogUtils.v("删除旧头像");
                        HttpCallBack httpCallBack = new HttpCallBack()
                        {
                                @Override
                                public void onSuccess(String response)
                                {
                                        LogUtils.v("删除旧头像成功");
                                }

                                @Override
                                public void onCancel()
                                {
                                        requestCount = 0;
                                        progressDialog.dismiss();
                                }

                                @Override
                                public void onFinally()
                                {
                                        //检查所有请求的完成状态
                                        checkCompletionStatus();
                                }
                        };
                        int oldAvatarId = Integer.valueOf(user.getUser_meta().getMm_user_avatar().get(0));
                        //发送删除请求
                        mediaDelegate.deleteMedia(httpCallBack, oldAvatarId);
                }
                else
                {
                        LogUtils.v("没有发现旧头像");
                        //检查所有请求的完成状态
                        checkCompletionStatus();
                }
        }


        /**
         * 更新用户信息
         */
        private void updateUserInfo()
        {
                LogUtils.v("开始更新用户信息");
                HttpCallBack httpCallBack = new HttpCallBack()
                {
                        @Override
                        public void onSuccess(String response)
                        {
                                LogUtils.v("更新用户信息成功");
                                User updatedUser = ParserUtils.fromJson(response, SingleUser.class).getBody();
                                updateLocalUserInfo(updatedUser);
                                checkCompletionStatus();
                        }

                        @Override
                        public void onError(WpError wpError)
                        {
                                //如果是邮箱相关的错误
                                if (wpError.getBody().getCode().contains("email"))
                                {
                                        ToastUtils.shortToast(ResourcesUtils.getString(R.string.input_not_valid_email_error_notice));
                                        inputEmailLayout.setError(ResourcesUtils.getString(R.string.input_not_valid_email_error_notice));
                                }
                                else
                                {
                                        ToastUtils.shortToast(ResourcesUtils.getString(R.string.update_user_error_message));
                                }

                                onCancel();
                        }

                        @Override
                        public void onHttpError()
                        {
                                onCancel();
                        }

                        @Override
                        public void onCancel()
                        {
                                requestCount = 0;
                                progressDialog.dismiss();
                        }
                };

                startDelegateToUpdateUserInfo(httpCallBack);

        }



        /**
         * 生成请求参数 然后调用delegate 发起请求
         *
         * @param httpCallBack
         */
        private void startDelegateToUpdateUserInfo(HttpCallBack httpCallBack)
        {
                //创建请求参数
                UpdateUserParameters parameters = new UpdateUserParameters();

                if (inputEmail.getText() != null)
                {
                        //添加邮箱
                        parameters.setEmail(inputEmail.getText().toString());
                }

                if (inputNickname.getText() != null)
                {
                        //添加昵称
                        parameters.setNickname(inputNickname.getText().toString());
                }

                //只有在描述不是空的情况 才设置
                if (inputDescription.getText() != null && !inputDescription.getText().toString().trim().isEmpty())
                {
                        //添加描述
                        parameters.setDescription(inputDescription.getText().toString());
                }

                //只有在密码不是空的情况 才设置
                if (inputPassword.getText() != null && !inputPassword.getText().toString().trim().isEmpty())
                {
                        //添加密码
                        parameters.setPassword(inputPassword.getText().toString());
                }
                //如果有设置新的头像id
                if (newAvatarId > 0)
                {
                        //创建 meta类型
                        UpdateUserParameters.Meta meta = new UpdateUserParameters.Meta();
                        //添加 新的avatar头像id
                        meta.setMm_user_avatar(newAvatarId);
                        //添加meta到参数里
                        parameters.setMeta(meta);
                }

                delegate.updateUser(httpCallBack, parameters);
        }


        /**
         * 请求完成计数+1
         * 然后检查是否所有请求都已完成
         * 都已完成的话 才进行后续操作
         */
        private synchronized void checkCompletionStatus()
        {
                //请求计数+1
                requestCount++;
                LogUtils.v("请求计数 " + requestCount);

                //如果所有请求都完成了
                if (requestCount == TOTAL_REQUEST_COUNT)
                {
                        //隐藏进度条
                        progressDialog.dismiss();
                        //提示信息更新
                        ToastUtils.shortToast(ResourcesUtils.getString(R.string.update_successfully_message));
                        //注销提交按钮
                        //buttonUpdate.setEnabled(false);
                        //移除图片文件的指针
                        //newAvatarFile = null;
                        //newAvatarId = 0;

                        //更新成功结束当前页面
                        finish();
                }

        }

        /**
         * 更新本地储存的用户信息
         */
        private void updateLocalUserInfo(User updatedUser)
        {
                user.setUser_email(updatedUser.getEmail());
                user.setUser_display_name(updatedUser.getName());
                //更新描述
                user.getUser_meta().setDescription(new ArrayList<>(Collections.singletonList(updatedUser.getDescription())));
                //新头像id存在的情况
                if (newAvatarId > 0)
                {
                        //设置新头像的id
                        user.getUser_meta().setMm_user_avatar(new ArrayList<>(Collections.singletonList(String.valueOf(newAvatarId))));
                        //更新新头像的地址
                        user.setAvatar_urls(updatedUser.getMetadata().getAvatar_src());
                }

                UserPreferencesUtils.setUser(user);
        }


        /**
         * 更改头像的点击动作监听
         */
        private void changeAvatarOnclickListener()
        {
                LocalResourceIntent.startActionForResultToGetImage(this);

        }

        /**
         * 选择完图片后 启动裁剪图片页面
         */
        private void afterSelectedImageAndStartCropImage(Uri imageUri)
        {

                //创建获取临时缓存文件
                newAvatarFile = FileUtils.createNewCacheFile(this);
                newAvatarUri = FileUtils.getUri(newAvatarFile);
                if (imageUri == null)
                {
                        ToastUtils.shortToast("错误: 无法获取选中图片的URI");
                }
                else if (newAvatarFile == null)
                {
                        ToastUtils.shortToast("错误: 无法创建缓存文件");
                }
                else if (newAvatarUri == null)
                {
                        ToastUtils.shortToast("错误: 无法获取缓存文件URI");
                }
                else
                {
                        ImageCropBean cropBean = new ImageCropBean();
                        cropBean.setDataUri(imageUri);
                        cropBean.setOutputX(GlobalConfig.USER_AVATAR_SIZE);
                        cropBean.setOutputY(GlobalConfig.USER_AVATAR_SIZE);
                        cropBean.calculateAspect();
                        cropBean.setScale(true);
                        cropBean.setReturnData(false);
                        cropBean.setSaveUri(newAvatarUri);
                        LocalResourceIntent.startActionForResultToCropImage(this, cropBean);
                }
        }

        /**
         * 成功完成裁剪后
         */
        private void afterCropImage()
        {

                //加载新的裁剪头像
                Glide.with(this).load(newAvatarUri).circleCrop().into(avatarImg);
                //激活提交更新的按钮
                buttonUpdate.setEnabled(true);
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
        {
                super.onActivityResult(requestCode, resultCode, data);
                //如果请求成功了
                if (resultCode == RESULT_OK)
                {
                        //请求码 为获取图片
                        switch (requestCode)
                        {

                                case LocalResourceIntent.requestCodeToGetImage:
                                        //选完图片后
                                        if (data != null)
                                        {
                                                afterSelectedImageAndStartCropImage(data.getData());
                                        }
                                        break;
                                case LocalResourceIntent.requestCodeToCropImage:
                                        //裁剪完图片后
                                        afterCropImage();
                                        break;
                        }
                }
        }

        /**
         * 监听标题栏菜单动作
         * listen toolbar item click event
         *
         * @param item
         * @return
         */
        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item)
        {
                switch (item.getItemId())
                {
                        //如果点了返回键
                        case android.R.id.home:
                                //结束当前活动页
                                finish();
                                return true;
                }
                return super.onOptionsItemSelected(item);
        }


        @Override
        protected void onStop()
        {
                //取消本活动相关的所有网络请求
                Request.cancelRequest(TAG);
                super.onStop();
        }


        /**
         * 启动本活动的静态回调方法
         * static method to start current activity
         *
         * @param activity
         */
        public static void startActionFroResult(Activity activity)
        {
                Intent intent = new Intent(activity, UserProfileActivity.class);
                activity.startActivityForResult(intent, 0);
        }


}
