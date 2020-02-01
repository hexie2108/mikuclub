package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.mikuclub.app.delegate.UserDelegate;
import org.mikuclub.app.javaBeans.response.baseResource.UserLogin;
import org.mikuclub.app.storage.UserPreferencesUtils;
import org.mikuclub.app.utils.AlertDialogUtils;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.ResourcesUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.utils.custom.MyTextWatcher;
import org.mikuclub.app.utils.http.GlideImageUtils;
import org.mikuclub.app.utils.http.HttpCallBack;
import org.mikuclub.app.utils.http.Request;

import androidx.annotation.NonNull;
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

        /* 变量 local variable */

        //数据请求代理人
        private UserDelegate delegate;
        private UserLogin user;

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
                //获取用户数据
                user = UserPreferencesUtils.getUser();

                //替换原版标题栏
                setSupportActionBar(toolbar);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null)
                {
                        //显示标题栏返回键
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        actionBar.setTitle("用户信息");
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
                inputEmail.setText(user.getUser_email());
                inputNickname.setText(user.getUser_display_name());
                //如果用户描述不是空
                if(!GeneralUtils.listIsNullOrHasEmptyElement(user.getUser_meta().getDescription()))
                {
                        //加载用户描述
                        inputDescription.setText(user.getUser_meta().getDescription().get(0));
                }

                //绑定密码栏状态监听器
                //input内容监听器, 在密码内容为空的情况 或者 长度小于6 , 注销更新按钮
                //自定义 text watcher, 只有在内容变化完成后才会激活回调
                TextWatcher textWatcherOnPassword = new MyTextWatcher(() -> {
                        String content="";
                        if(inputPassword.getText() != null){
                                content = inputPassword.getText().toString().trim();
                        }
                        //如果内容是空, 或者 小于6个字
                        if (content.isEmpty() || content.length() < 6)
                        {
                                //注销按钮
                                buttonUpdate.setEnabled(false);
                                inputPasswordLayout.setError(" 新密码长度必须是6位以上");
                        }
                        else
                        {
                                //激活按钮
                                buttonUpdate.setEnabled(true);
                                //去除错误提示
                                inputPasswordLayout.setError(null);
                        }
                });
                //添加内容变化监听器
                inputPassword.addTextChangedListener(textWatcherOnPassword);

        }

        /**
         * 发送错误报告信息
         * send report
         */
        private void sendReport()
        {

                //显示加载进度条
                progressDialog.show();

                HttpCallBack httpCallBack = new HttpCallBack()
                {
                        @Override
                        public void onSuccess(String response)
                        {


                                ToastUtils.longToast(ResourcesUtils.getString(R.string.report_send_successful));
                        }

                        @Override
                        public void onError(String response)
                        {
                                ToastUtils.longToast(ResourcesUtils.getString(R.string.report_send_failure));
                        }

                        @Override
                        public void onHttpError()
                        {
                                onError(null);
                        }

                        @Override
                        public void onFinally()
                        {
                                //隐藏进度条
                                progressDialog.dismiss();
                        }

                        @Override
                        public void onCancel()
                        {
                                //隐藏进度条
                                progressDialog.dismiss();
                        }
                };




                // delegate.sendPrivateMessage(httpCallBack, bodyParameters);

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
         * 启动本活动的静态方法
         * static method to start current activity
         *
         * @param context
         */
        public static void startAction(Context context)
        {
                Intent intent = new Intent(context, UserProfileActivity.class);
                context.startActivity(intent);
        }


}
