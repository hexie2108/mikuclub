package org.mikuclub.app.controller;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.delegates.CommentDelegate;
import org.mikuclub.app.javaBeans.parameters.CommentParameters;
import org.mikuclub.app.javaBeans.parameters.CreateCommentParameters;
import org.mikuclub.app.javaBeans.response.SingleComment;
import org.mikuclub.app.javaBeans.response.baseResource.Comment;
import org.mikuclub.app.javaBeans.response.Comments;
import org.mikuclub.app.javaBeans.response.baseResource.UserLogin;
import org.mikuclub.app.javaBeans.response.WpError;
import org.mikuclub.app.utils.KeyboardUtils;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.utils.storage.UserUtils;
import org.mikuclub.app.utils.ViewUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;

import androidx.appcompat.app.AlertDialog;
import mikuclub.app.R;

public class CommentController extends BaseController
{

        /*变量*/
        private UserLogin userLogin;
        private int postId;
        private int parentCommentId;
        private int authorId;

        /*组件*/
        private ImageView avatarImage;
        private TextInputLayout inputLayout;
        private TextInputEditText input;
        private CheckBox checkBoxNotifyAuthor;
        private AlertDialog progressDialog;


        public CommentController(Context context)
        {
                super(context);
                if (UserUtils.isLogin())
                {
                        userLogin = UserUtils.getUser();
                }
                //创建进度条弹窗
                progressDialog = ViewUtils.createProgressDialog(getContext(), false, false);
        }

        /**
         * 初始化评论输入框
         */
        public void initCommentInput(ImageView avatarImage, TextInputLayout inputLayout, TextInputEditText input, CheckBox checkBoxNotifyAuthor)
        {
                //绑定评论框组件
                this.avatarImage = avatarImage;
                this.inputLayout = inputLayout;
                this.input = input;
                this.checkBoxNotifyAuthor = checkBoxNotifyAuthor;

                //如果用户有登陆
                if (userLogin != null)
                {
                        //加载用户头像
                        GlideImageUtils.getSquareImg(getContext(), avatarImage, userLogin.getAvatar_urls());
                        //激活评论框
                        inputLayout.setEnabled(true);

                        //改变提示
                        inputLayout.setHint("发表评论...");
                        //如果通知作者的选择框不是null
                        if (checkBoxNotifyAuthor != null)
                        {
                                //显示选择框
                                checkBoxNotifyAuthor.setVisibility(View.VISIBLE);
                        }


                        //创建点击事件监听器
                        View.OnClickListener onClickListener = v -> {
                                //发送评论
                                sendComment();
                        };

                        //input内容监听器, 在内容不为空的情况激活发送按钮 更改图标颜色
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
                                        String content = input.getText().toString().trim();
                                        //如果内容不是空
                                        if (!content.isEmpty())
                                        {
                                                //激活按钮点击事件, 更换按钮颜色
                                                //绑定图标点击
                                                inputLayout.setEndIconOnClickListener(onClickListener);
                                                inputLayout.setEndIconTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.colorPrimary)));
                                        }
                                        else
                                        {
                                                //注销按钮, 更换按钮颜色
                                                inputLayout.setEndIconOnClickListener(null);
                                                inputLayout.setEndIconTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.defaultTextColor)));
                                        }
                                }
                        };
                        //添加内容变化监听器
                        input.addTextChangedListener(textWatcher);


                        //监听键盘动作
                        input.setOnEditorActionListener((v, actionId, event) -> {
                                //如果从键盘点了确认键
                                if (actionId == EditorInfo.IME_ACTION_SEND)
                                {
                                        //发送评论
                                        sendComment();
                                }
                                return true;
                        });
                }
                else
                {
                        //去除发送图标
                        inputLayout.setEndIconDrawable(null);
                }
        }

        /**
         * 设置回复对象id和修改 评论框input提示文字
         *
         * @param parentComment
         * @param isFirstTime
         */
        public void changeParentComment(Comment parentComment, boolean isFirstTime)
        {
                //如果用户有登陆
                if (userLogin != null)
                {
                        //设置父评论id
                        setParentCommentId(parentComment.getId());
                        //设置父评论作者id
                        setAuthorId(parentComment.getAuthor());
                        //修改显示名
                        inputLayout.setHint("回复 " + parentComment.getAuthor_name() + ":");
                        if (!isFirstTime)
                        {
                                //获取焦点, 显示键盘
                                KeyboardUtils.showKeyboard(input);
                        }

                }
        }


        /**
         * 发送评论
         */
        private void sendComment()
        {
                String content = input.getText().toString().trim();
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
                                        if (checkBoxNotifyAuthor != null && checkBoxNotifyAuthor.isChecked())
                                        {
                                                //重置选择框
                                                checkBoxNotifyAuthor.setChecked(false);
                                        }
                                        //获取新添加的评论
                                        Comment newComment = ParserUtils.fromJson(response, SingleComment.class).getBody();
                                        //加进列表
                                        getRecyclerDataList().add(0, newComment);
                                        //通知更新
                                        getRecyclerViewAdapter().notifyItemInserted(0);
                                        //滚动到第一行
                                        getRecyclerView().smoothScrollToPosition(0);
                                }

                                @Override
                                public void onError(String response)
                                {
                                        WpError wpError = ParserUtils.fromJson(response, WpError.class);
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

                        CreateCommentParameters createCommentParameters = new CreateCommentParameters();
                        createCommentParameters.setContent(content);
                        createCommentParameters.setPost(postId);
                        createCommentParameters.setParent(parentCommentId);

                        //新建评论的元数据容器
                        CreateCommentParameters.Meta meta;
                        //在一级评论的情况 如果通知作者的选择框不是null  而且 被勾选了
                        if (checkBoxNotifyAuthor != null && checkBoxNotifyAuthor.isChecked())
                        {
                                LogUtils.e("勾选作者通知");
                                //创建元数据容器
                                meta = new CreateCommentParameters.Meta();
                                //设置被回复用户的id
                                meta.setParent_user_id(authorId);
                                //设置为未读
                                meta.setParent_user_read(0);
                                //添加元数据到请求
                                createCommentParameters.setMeta(meta);
                        }
                        //在二级评论的情况 (回复其他人的评论)
                        else if (checkBoxNotifyAuthor == null)
                        {
                                meta = new CreateCommentParameters.Meta();
                                //设置被回复用户的id
                                meta.setParent_user_id(authorId);
                                //设置为未读
                                meta.setParent_user_read(0);
                                //添加元数据到请求
                                createCommentParameters.setMeta(meta);
                        }

                        ((CommentDelegate) getDelegate()).createComment(httpCallBack, createCommentParameters);

                }
                else
                {
                        ToastUtils.shortToast("评论内容不能为空!");
                }


        }

        /*
       加载更多
        */
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
                                        Comments newComments = ParserUtils.fromJson(response, Comments.class);
                                        //加载数据
                                        getRecyclerDataList().addAll(newComments.getBody());
                                        //通知列表更新, 获取正确的插入位置, 排除可能的头部造成的偏移
                                        int position = getRecyclerDataList().size() + getRecyclerViewAdapter().getHeaderRow();
                                        getRecyclerViewAdapter().notifyItemInserted(position);

                                        //当前页数+1
                                        setCurrentPage(getCurrentPage() + 1);
                                        //如果是还未获取过总页数
                                        if (getTotalPage() == -1)
                                        {
                                                setTotalPage(newComments.getHeaders().getTotalPage());
                                        }

                                        //如果还未到最后一页
                                        if (getCurrentPage() < getTotalPage())
                                        {
                                                //重新开启信号标
                                                setWantMore(true);
                                                //隐藏尾部加载进度条
                                                getRecyclerViewAdapter().updateFooterStatus(false, false, false);
                                        }
                                        //如果已经到最后一页了
                                        else
                                        {
                                                //调用错误处理方法
                                                onError(null);
                                        }
                                }

                                //请求结果包含错误的情况
                                //结果主体为空, 无更多内容
                                @Override
                                public void onError(String response)
                                {
                                        getRecyclerViewAdapter().updateFooterStatus(false, true, false);
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
                                        //隐藏尾部加载进度条
                                        getRecyclerViewAdapter().updateFooterStatus(false, false, false);
                                }
                        };

                        startDelegate(httpCallBack);
                }
        }

        /**
         * 启动代理人发送请求
         *
         * @param httpCallBack
         */
        private void startDelegate(HttpCallBack httpCallBack)
        {
                ((CommentDelegate) getDelegate()).getCommentList(httpCallBack, getCurrentPage() + 1, (CommentParameters) getParameters());
        }


        public void setPostId(int postId)
        {
                this.postId = postId;
        }

        public void setParentCommentId(int parentCommentId)
        {
                this.parentCommentId = parentCommentId;
        }

        public void setAuthorId(int authorId)
        {
                this.authorId = authorId;
        }
}
