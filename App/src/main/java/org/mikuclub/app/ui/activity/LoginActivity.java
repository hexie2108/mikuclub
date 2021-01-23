package org.mikuclub.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

import org.json.JSONException;
import org.json.JSONObject;
import org.mikuclub.app.config.GlobalConfig;
import org.mikuclub.app.delegate.UtilsDelegate;
import org.mikuclub.app.javaBeans.parameters.LoginParameters;
import org.mikuclub.app.storage.UserPreferencesUtils;
import org.mikuclub.app.ui.activity.base.MyActivity;
import org.mikuclub.app.utils.AlertDialogUtils;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.KeyboardUtils;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.ResourcesUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.utils.custom.MyTextWatcher;
import org.mikuclub.app.utils.http.HttpCallBack;
import org.mikuclub.app.utils.social.TencentListener;
import org.mikuclub.app.utils.social.TencentUtils;
import org.mikuclub.app.utils.social.WeiboListener;
import org.mikuclub.app.utils.social.WeiboUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import mikuclub.app.R;

/**
 * 登陆页面
 * login page
 */
public class LoginActivity extends MyActivity
{

        /* 静态变量 Static variable */
        public static final int TAG = 7;
        public static final int REQUEST_CODE = 1;

        /* 变量 local variable */
        private UtilsDelegate delegate;
        private TencentListener tencentListener;
        private IWBAPI weiboAPI;

        /* 组件 views */
        private EditText inputUserName;
        private EditText inputUserPassword;
        private TextInputLayout inputUserNameLayout;
        private TextInputLayout inputUserPasswordLayout;
        private Button loginButton;
        private Button socialButtonWeibo;
        private Button socialButtonQQ;
        private AlertDialog progressDialog;

        @Override
        public void onCreate(Bundle savedInstanceState)
        {

                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_login);

                //绑定组件
                inputUserName = findViewById(R.id.input_user_name);
                inputUserPassword = findViewById(R.id.input_user_password);
                inputUserNameLayout = findViewById(R.id.input_user_name_layout);
                inputUserPasswordLayout = findViewById(R.id.input_user_password_layout);
                loginButton = findViewById(R.id.login_button);

                socialButtonWeibo = findViewById(R.id.social_button_weibo);
                socialButtonQQ = findViewById(R.id.social_button_qq);
                Toolbar toolbar = findViewById(R.id.toolbar);

                //创建进度条弹窗
                progressDialog = AlertDialogUtils.createProgressDialog(this, false, false);
                //创建微博分享的API
               weiboAPI = WeiboUtils.getInstance(this);

                //替换原版标题栏
                setSupportActionBar(toolbar);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null)
                {
                        //显示标题栏返回键
                        actionBar.setDisplayHomeAsUpEnabled(true);
                }

                delegate = new UtilsDelegate(TAG);

                //初始化输入框
                initEditText();
                //初始化社会登陆按钮
                initSocialButton();


        }

        /**
         * 初始化输入框
         * init editText form
         */
        private void initEditText()
        {
                //获取焦点+弹出键盘
                //KeyboardUtils.showKeyboard(inputUserName);
                //input内容监听器, 在都不为空的情况激活登陆按钮
                //自定义 text watcher, 只有在内容变化完成后才会激活回调
                TextWatcher textWatcher = new MyTextWatcher(() -> {
                        //重新输入后 就隐藏错误信息
                        inputUserNameLayout.setError(null);
                        inputUserPasswordLayout.setError(null);

                        //如果用户名和密码都不是空
                        if (!inputUserName.getText().toString().isEmpty() && !inputUserPassword.getText().toString().isEmpty())
                        {
                                //激活按钮
                                loginButton.setEnabled(true);
                        }
                        else
                        {
                                //注销按钮
                                loginButton.setEnabled(false);
                        }
                });
                //添加内容变化监听器
                inputUserName.addTextChangedListener(textWatcher);
                inputUserPassword.addTextChangedListener(textWatcher);

                //监听键盘动作
                inputUserPassword.setOnEditorActionListener((v, actionId, event) -> {
                        //如果是激活状态
                        if (loginButton.isEnabled())
                        {
                                //产生点击事件
                                loginButton.performClick();
                        }
                        else
                        {
                                ToastUtils.shortToast("用户名和密码不能为空");
                        }
                        return true;
                });

                loginButton.setOnClickListener(v -> {
                        //本地登录方式
                        LoginParameters loginParameters = new LoginParameters();
                        loginParameters.setUsername(inputUserName.getText().toString());
                        loginParameters.setPassword(inputUserPassword.getText().toString());
                        sendLogin(loginParameters);
                });
        }


        /**
         * 初始化社会化登陆按钮
         * init social network login button
         */
        private void initSocialButton()
        {
                socialButtonWeibo.setOnClickListener(
                        v -> startWeiboAuth()
                );
                socialButtonQQ.setOnClickListener(
                        v -> startQQAuth()
                );
        }


        /**
         * 发送登陆请求
         * send login request
         *
         * @param loginParameters
         */
        private void sendLogin(LoginParameters loginParameters)
        {
                progressDialog.show();
                loginButton.setEnabled(false);
                //去除当前焦点 和隐藏键盘
                KeyboardUtils.hideKeyboard(getCurrentFocus());

                HttpCallBack httpCallBack = new HttpCallBack()
                {
                        /**请求成功的情况, 但是返回数据还未验证*/
                        @Override
                        public void onSuccessHandler(String response)
                        {
                                try
                                {
                                        //先解析一遍返回数据
                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONObject bodyObject = jsonObject.getJSONObject("body");
                                        //如果没有错误码, 说明登录成功
                                        if (!bodyObject.has("code"))
                                        {
                                                //设置登陆成功的信息
                                                setLoginResult(bodyObject.toString());
                                        }
                                        //如果有错误码
                                        else
                                        {
                                                //调用错误处理
                                                onError(bodyObject.getString("code"));
                                        }
                                }
                                catch (JSONException e)
                                {
                                        onError("social_login_error");
                                }
                        }

                        public void onError(String errorCode)
                        {
                                progressDialog.dismiss();
                                loginButton.setEnabled(true);
                                String errorMessage;
                                if (errorCode.indexOf("username") != -1)
                                {
                                        errorMessage = ResourcesUtils.getString(R.string.login_username_error);
                                }
                                else if (errorCode.indexOf("email") != -1)
                                {
                                        errorMessage = ResourcesUtils.getString(R.string.login_email_error);
                                }
                                else if (errorCode.indexOf("password") != -1)
                                {
                                        errorMessage = ResourcesUtils.getString(R.string.login_password_error);
                                }
                                else if (errorCode.indexOf("social_login_error") != -1)
                                {
                                        errorMessage = ResourcesUtils.getString(R.string.login_social_login_error);
                                }
                                else
                                {
                                        errorMessage = ResourcesUtils.getString(R.string.internet_unknown_error);
                                }
                                inputUserNameLayout.setError(errorMessage);
                        }

                        @Override
                        public void onHttpError()
                        {
                                progressDialog.dismiss();
                                loginButton.setEnabled(true);
                                //显示错误信息
                                inputUserPasswordLayout.setError(ResourcesUtils.getString(R.string.login_server_error_and_please_retry));
                        }

                        @Override
                        public void onCancel()
                        {
                                progressDialog.dismiss();
                                loginButton.setEnabled(true);
                        }
                };
                delegate.login(httpCallBack, loginParameters);

        }


        /**
         * 设置登陆成功后的信息
         * Set information after successful login
         *
         * @param response
         */
        private void setLoginResult(String response)
        {
                //储存用户登陆信息
                UserPreferencesUtils.login(response);
                //设置请求结果 为 成功
                //setResult(RESULT_OK);
                //finish();
                WelcomeActivity.startAction(this);

        }


        /**
         * 启动QQ登陆
         * Start QQ login
         */
        private void startQQAuth()
        {
                tencentListener = new TencentListener(this)
                {
                        @Override
                        public void onSuccess()
                        {
                                //本地登录方式
                                LoginParameters loginParameters = new LoginParameters();
                                loginParameters.setOpen_type(TencentUtils.OPEN_TYPE);
                                loginParameters.setAccess_token(getAccessToken());
                                loginParameters.setOpen_id(getOpenID());
                                loginParameters.setUnion_id(getUnionId());
                                sendLogin(loginParameters);
                                //释放QQ api
                                //TencentUtils.getInstance().logout(LoginActivity.this);
                        }
                };
                TencentUtils.getInstance().login(this, "all", tencentListener);
        }

        /**
         * 启动微博登陆
         * start weibo login
         */
        private void startWeiboAuth()
        {

                WeiboListener listener = new WeiboListener()
                {
                        @Override
                        public void onSuccess()
                        {
                                //本地登录方式
                                LoginParameters loginParameters = new LoginParameters();
                                loginParameters.setOpen_type(WeiboUtils.OPEN_TYPE);
                                loginParameters.setAccess_token(getAccessToken());
                                loginParameters.setOpen_id(getOpenID());
                                sendLogin(loginParameters);
                                //释放微博api
                                //WeiboUtils.removeInstance();
                        }
                };

                weiboAPI.authorize(listener);

        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
        {
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == Constants.REQUEST_LOGIN)
                {
                        LogUtils.v("QQ登陆回调");
                        Tencent.onActivityResultData(requestCode, resultCode, data, tencentListener);
                }
                else
                {
                        LogUtils.v("微博登陆回调");
                        weiboAPI.authorizeCallback(requestCode, resultCode, data);
                }
        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu)
        {
                getMenuInflater().inflate(R.menu.login_menu, menu);
                return true;
        }

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
                        case R.id.forgotten_password:
                                //启动游览器 访问重置密码网页
                                HttpUtils.startWebViewIntent(this, GlobalConfig.Server.FORGOTTEN_PASSWORD, null);
                                return true;
                }
                return super.onOptionsItemSelected(item);
        }


        /**
         * 启动本活动的并返回结果的静态方法
         * Static method that starts this activity and returns the result
         *
         * @param context
         */
        public static void startActionForResult(Context context)
        {
                Intent intent = new Intent(context, LoginActivity.class);
                ((AppCompatActivity) context).startActivityForResult(intent, REQUEST_CODE);
        }

        /**
         * 启动本活动的静态方法
         * @param context
         */
        public static void startAction(Context context)
        {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
        }



}
