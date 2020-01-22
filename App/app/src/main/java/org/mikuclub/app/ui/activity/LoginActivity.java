package org.mikuclub.app.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

import org.json.JSONException;
import org.json.JSONObject;
import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.delegates.UtilsDelegate;
import org.mikuclub.app.javaBeans.parameters.LoginParameters;
import org.mikuclub.app.utils.GeneralUtils;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.utils.UserUtils;
import org.mikuclub.app.utils.social.TencentListener;
import org.mikuclub.app.utils.social.TencentUtils;
import org.mikuclub.app.utils.social.WeiboListener;
import org.mikuclub.app.utils.social.WeiboUtils;
import org.mikuclub.app.utils.ViewUtils;

import mikuclub.app.R;

public class LoginActivity extends AppCompatActivity
{

        /*静态变量*/
        public static final int TAG = 7;
        public static final int REQUEST_CODE = 1;

        /*变量*/
        private EditText inputUserName;
        private EditText inputUserPassword;
        private TextInputLayout inputUserNameLayout;
        private TextInputLayout inputUserPasswordLayout;
        private Button loginButton;
        private Button socialButtonWeibo;
        private Button socialButtonQQ;
        private AlertDialog progressDialog;

        private UtilsDelegate delegate;

        private TencentListener tencentListener;

        /*组件*/


        @Override
        public void onCreate(Bundle savedInstanceState)
        {

                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_login);

                inputUserName = findViewById(R.id.input_user_name);
                inputUserPassword = findViewById(R.id.input_user_password);
                inputUserNameLayout = findViewById(R.id.input_user_name_layout);
                inputUserPasswordLayout = findViewById(R.id.input_user_password_layout);
                loginButton = findViewById(R.id.login_button);

                socialButtonWeibo = findViewById(R.id.social_button_weibo);
                socialButtonQQ = findViewById(R.id.social_button_qq);
                Toolbar toolbar = findViewById(R.id.toolbar);

                //创建进度条弹窗
                progressDialog = ViewUtils.initProgressDialog(this);

                //替换原版标题栏
                setSupportActionBar(toolbar);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null)
                {
                        //显示返回键
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
         */
        private void initEditText()
        {
                //获取焦点+弹出键盘
                //KeyboardUtils.showKeyboard(inputUserName);
                //input内容监听器, 在都不为空的情况激活登陆按钮
                TextWatcher textWatcher = new TextWatcher()
                {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after)
                        {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count)
                        {
                        }

                        @Override
                        public void afterTextChanged(Editable s)
                        {
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
                        }
                };
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
         * 初始化微博登陆按钮
         */
        private void initSocialButton()
        {
                socialButtonWeibo.setOnClickListener(v -> {
                        startWeiboAuth();
                });
                socialButtonQQ.setOnClickListener(v -> {
                        startQQAuth();
                });
        }


        /**
         * 发送登陆请求
         */
        private void sendLogin(LoginParameters loginParameters)
        {
                progressDialog.show();
                loginButton.setEnabled(false);

                HttpCallBack httpCallBack = new HttpCallBack()
                {
                        /**
                         * 请求成功的情况, 但是返回数据还未验证
                         * @param response
                         */
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

                                if (errorCode.indexOf("username") != -1)
                                {
                                        //显示错误信息
                                        inputUserNameLayout.setError("用户名错误");

                                }
                                else if (errorCode.indexOf("email") != -1)
                                {
                                        //显示错误信息
                                        inputUserNameLayout.setError("邮箱地址错误");

                                }
                                else if (errorCode.indexOf("password") != -1)
                                {
                                        //显示错误信息
                                        inputUserPasswordLayout.setError("密码错误");
                                }
                                else if (errorCode.indexOf("social_login_error") != -1)
                                {
                                        //显示错误信息
                                        inputUserPasswordLayout.setError("社会化登录错误");
                                }
                                else
                                {
                                        //显示错误信息
                                        inputUserPasswordLayout.setError("未知错误");
                                }
                        }

                        @Override
                        public void onHttpError()
                        {
                                progressDialog.dismiss();
                                loginButton.setEnabled(true);
                                //显示错误信息
                                inputUserPasswordLayout.setError("网络请求错误, 请重试");
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
         */
        private void setLoginResult(String response)
        {
                //储存用户登陆信息
                UserUtils.login(response);
                //设置请求结果 为 成功
                setResult(RESULT_OK);
                finish();

        }


        /**
         * 启动QQ登陆
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
                                TencentUtils.getInstance().logout(LoginActivity.this);
                        }
                };
                TencentUtils.getInstance().login(this, "all", tencentListener);
        }

        /**
         * 启动微博登陆
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
                                WeiboUtils.removeInstance();
                        }
                };
                WeiboUtils.getInstance(this).authorize(listener);

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
                        WeiboUtils.getInstance(this).authorizeCallback(requestCode, resultCode, data);
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
                                GeneralUtils.startWebViewIntent(this, GlobalConfig.Server.FORGOTTEN_PASSWORD, null);
                                return true;
                }
                return super.onOptionsItemSelected(item);
        }


        /**
         * 静态 启动本活动 并返回结果的方法
         *
         * @param context
         */
        public static void startActionForResult(Context context)
        {

                Intent intent = new Intent(context, LoginActivity.class);
                ((AppCompatActivity) context).startActivityForResult(intent, REQUEST_CODE);
        }


}
