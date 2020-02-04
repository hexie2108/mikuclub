package org.mikuclub.app.controller;

import android.content.Context;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.mikuclub.app.controller.base.BaseController;
import org.mikuclub.app.delegate.MessageDelegate;
import org.mikuclub.app.javaBeans.parameters.CreatePrivateMessageParameters;
import org.mikuclub.app.javaBeans.response.PrivateMessages;
import org.mikuclub.app.javaBeans.response.SinglePrivateMessage;
import org.mikuclub.app.javaBeans.response.WpError;
import org.mikuclub.app.javaBeans.response.baseResource.PrivateMessage;
import org.mikuclub.app.utils.AlertDialogUtils;
import org.mikuclub.app.utils.KeyboardUtils;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.utils.ResourcesUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.utils.http.HttpCallBack;

import java.util.Collections;

import androidx.appcompat.app.AlertDialog;
import mikuclub.app.R;


/**
 * 获取当前登陆用户和另外一个用户之间的私信列表的请求控制器
 * Request controller to get the list of private messages between the current user and an another user
 */
public class PrivateMessageController extends BaseController
{
        //私信作者的id
        private Integer senderId;

        /*额外组件*/
        //输入框布局
        private TextInputLayout inputLayout;
        private TextInputEditText input;

        private AlertDialog progressDialog;

        public PrivateMessageController(Context context)
        {
                super(context);
                //创建进度条弹窗
                progressDialog = AlertDialogUtils.createProgressDialog(context, false, false);
        }

        /**
         * 发送消息
         */
        public void sendMessage()
        {

                String content = input.getText().toString().trim();
                //把换行符号字符替换成<br>换行
                content = content.replace("\n", "<br/>");
                content = content.replace("\r", "<br/>");

                //评论内容不是空的
                if (!content.isEmpty())
                {
                        //显示加载进度条
                        progressDialog.show();
                        //隐藏键盘
                        KeyboardUtils.hideKeyboard(input);

                        HttpCallBack httpCallBack = new HttpCallBack()
                        {
                                @Override
                                public void onSuccess(String response)
                                {
                                        //清空内容
                                        input.setText("");
                                        //如果通知作者的选择框不是null  而且 被勾选了
                                        //获取新添加的评论
                                        PrivateMessage privateMessage = ParserUtils.fromJson(response, SinglePrivateMessage.class).getBody();
                                        //加进列表
                                        getRecyclerDataList().add(privateMessage);
                                        //通知更新, 修正可能存在的 头部header带来的位置偏移
                                        int position = getRecyclerViewAdapter().getLastItemPositionWithHeaderRowFix();
                                        //通知更新
                                        getRecyclerViewAdapter().notifyItemInserted(position);
                                        //滚动到最后一行
                                        getRecyclerView().smoothScrollToPosition(position);
                                }

                                @Override
                                public void onError(WpError wpError)
                                {

                                        ToastUtils.shortToast(wpError.getBody().getMessage());
                                }

                                @Override
                                public void onFinally()
                                {
                                        progressDialog.dismiss();
                                }

                                @Override
                                public void onCancel()
                                {
                                        progressDialog.dismiss();
                                }
                        };

                        CreatePrivateMessageParameters createPrivateMessageParameters = new CreatePrivateMessageParameters();
                        createPrivateMessageParameters.setContent(content);
                        createPrivateMessageParameters.setRecipient_id(senderId);

                        ((MessageDelegate) getDelegate()).sendPrivateMessage(httpCallBack, createPrivateMessageParameters);

                }
                else
                {
                        ToastUtils.shortToast(ResourcesUtils.getString(R.string.input_empty_error));
                }
        }

        @Override
        public void getMore()
        {
                //检查信号标
                if (isWantMore())
                {
                        //关闭信号标
                        setWantMore(false);
                        //显示尾部加载
                        getRecyclerViewAdapter().updateFooterStatus(true, false, false);

                        HttpCallBack httpCallBack = new HttpCallBack()
                        {
                                //成功的情况
                                @Override
                                public void onSuccess(String response)
                                {

                                        //解析数据
                                        PrivateMessages privateMessages = ParserUtils.fromJson(response, PrivateMessages.class);
                                        //逆转数据列表的排列, 把从新到旧 改成 从旧到新排列
                                        Collections.reverse(privateMessages.getBody());
                                        //加载数据
                                        getRecyclerDataList().addAll(privateMessages.getBody());
                                        //通知列表更新, 获取正确的插入位置, 排除可能的头部造成的偏移
                                        int position = getRecyclerDataList().size() + getRecyclerViewAdapter().getHeaderRow();

                                        //通知更新
                                        getRecyclerViewAdapter().notifyItemInserted(position);

                                        //跳转到最后一条消息
                                        getRecyclerView().scrollToPosition(position);

                                        //关闭信号标
                                        setWantMore(false);
                                        //隐藏尾部加载进度条
                                        getRecyclerViewAdapter().updateFooterStatus(false, false, false);

                                }

                                //请求结果包含错误的情况
                                //结果主体为空, 无更多内容
                                @Override
                                public void onError(WpError wpError)
                                {

                                        getRecyclerViewAdapter().updateFooterStatus(false, false, false);
                                }

                                //网络失败的情况
                                @Override
                                public void onHttpError()
                                {
                                        //显示错误信息, 绑定点击事件允许用户手动重试
                                        getRecyclerViewAdapter().setInternetErrorListener(v -> {
                                                //重置请求状态
                                                setWantMore(true);
                                                getMore();
                                        });
                                        getRecyclerViewAdapter().updateFooterStatus(false, false, true);
                                }

                                //取消请求的情况
                                @Override
                                public void onCancel()
                                {
                                        setWantMore(true);
                                }
                        };

                        //启动代理人发送请求 只获取第一页
                        startDelegate(httpCallBack, 1);
                }
        }

        /**
         * 启动代理人发送请求
         *
         * @param httpCallBack
         * @param page
         */
        private void startDelegate(HttpCallBack httpCallBack, int page)
        {
                ((MessageDelegate) getDelegate()).getPrivateMessage(httpCallBack, page, false, senderId);
        }



        public void setSenderId(Integer senderId)
        {
                this.senderId = senderId;
        }

        public void setInputLayout(TextInputLayout inputLayout)
        {
                this.inputLayout = inputLayout;
        }

        public void setInput(TextInputEditText input)
        {
                this.input = input;
        }
}
