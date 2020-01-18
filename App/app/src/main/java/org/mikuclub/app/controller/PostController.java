package org.mikuclub.app.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.delegates.BaseDelegate;
import org.mikuclub.app.delegates.PostDelegate;
import org.mikuclub.app.javaBeans.parameters.BaseParameters;
import org.mikuclub.app.javaBeans.parameters.PostParameters;
import org.mikuclub.app.javaBeans.resources.Posts;
import org.mikuclub.app.utils.KeyboardUtils;
import org.mikuclub.app.utils.ParserUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.utils.custom.MyEditTextNumberFilterMinMax;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import mikuclub.app.R;

/**
 * 文章列表控制器
 */
public class PostController extends BaseController
{
        /*额外变量*/
        //下拉刷新后 需要跳转到的item位置
        private int scrollPositionAfterRefresh = 1;

        /*额外组件*/
        //下拉刷新布局
        private SwipeRefreshLayout swipeRefresh;


        public PostController(Context context, BaseDelegate delegate, RecyclerView recyclerView, SwipeRefreshLayout swipeRefresh, BaseParameters parameters)
        {
                super(context, delegate, recyclerView, parameters);
                this.swipeRefresh = swipeRefresh;

        }

        /**
         * 加载更多
         */
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
                                        Posts newPosts = ParserUtils.posts(response);
                                        //加载数据
                                        getRecyclerDataList().addAll(newPosts.getBody());
                                        //通知列表更新, 获取正确的插入位置, 排除可能的头部造成的偏移
                                        int position = getRecyclerDataList().size()+getRecyclerViewAdapter().getHeaderRow();
                                        getRecyclerViewAdapter().notifyItemInserted(position);

                                        //当前页数+1
                                        setCurrentPage(getCurrentPage() + 1);
                                        //如果是还未获取过总页数
                                        if (getTotalPage() == -1)
                                        {
                                                setTotalPage(newPosts.getHeaders().getTotalPage());
                                        }

                                        //如果还未到最后一页
                                        if (getCurrentPage() < getTotalPage())
                                        {
                                                //重新开启信号标
                                                setWantMore(true);
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
                                        //隐藏尾部
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
                                }
                        };

                        //委托代理人发送请求
                        startDelegate(httpCallBack, getCurrentPage() + 1);
                }
        }


        /**
         * 下拉刷新文章+跳转功能
         *
         * @param page 请求文章的页数
         */
        public void refreshPosts(final int page)
        {
                setWantMore(false);

                //如果加载进度条没有出现 (跳转页面情况)
                if (!swipeRefresh.isRefreshing())
                {
                        //让加载进度条显示
                        swipeRefresh.setRefreshing(true);
                }

                HttpCallBack httpCallBack = new HttpCallBack()
                {
                        //成功
                        @Override
                        public void onSuccess(String response)
                        {
                                //解析新数据
                                Posts newPosts = ParserUtils.posts(response);
                                //清空旧数据
                                getRecyclerDataList().clear();
                                //添加数据到列表
                                getRecyclerDataList().addAll(newPosts.getBody());

                                //重置列表尾部状态
                                getRecyclerViewAdapter().updateFooterStatus(false, false, false);
                                //更新数据
                                getRecyclerViewAdapter().notifyDataSetChanged();

                                //更新当前页数
                                setCurrentPage(page);
                                //返回顶部
                                getRecyclerView().scrollToPosition(scrollPositionAfterRefresh);
                        }

                        @Override
                        public void onError(String response)
                        {
                                ToastUtils.shortToast("请求错误");
                        }

                        @Override
                        public void onHttpError()
                        {
                                onError(null);
                        }

                        //请求结束后
                        @Override
                        public void onFinally()
                        {
                                //关闭进度条
                                swipeRefresh.setRefreshing(false);
                                setWantMore(true);
                        }

                        //如果请求被取消
                        @Override
                        public void onCancel()
                        {
                                //关闭加载进度条
                                swipeRefresh.setRefreshing(false);
                                //重置信号标
                                setWantMore(true);
                        }
                };

                startDelegate(httpCallBack, page);
        }

        /**
         * 启动代理人发送请求
         *
         * @param httpCallBack
         * @param page
         */
        private void startDelegate(HttpCallBack httpCallBack, int page)
        {
                ((PostDelegate) getDelegate()).getPostList(httpCallBack, page, (PostParameters) getParameters());
        }


        /**
         * 显示跳转弹窗
         */
        public void openJumPageAlertDialog()
        {
                AppCompatActivity activity = (AppCompatActivity) getContext();
                //创建布局组件
                View view = activity.getLayoutInflater().inflate(R.layout.alert_dialog_input, (ViewGroup) activity.findViewById(android.R.id.content).getRootView(), false);
                final EditText input = view.findViewById(R.id.text_input);
                TextInputLayout textInputLayout = view.findViewById(R.id.text_input_layout);
                //加载占位符
                textInputLayout.setHint("第" + getCurrentPage() + "页 / 总共" + getTotalPage() + "页");
                //限制input输入范围 [从1到最后一页]
                input.setFilters(new InputFilter[]{new MyEditTextNumberFilterMinMax(1, getTotalPage())});
                //获取焦点+弹出键盘
                KeyboardUtils.showKeyboard(input);

                //创建 弹出构造器
                final AlertDialog.Builder builder = new MaterialAlertDialogBuilder(activity);
                //设置标题
                builder.setTitle("跳转");
                //加载布局
                builder.setView(view);
                //设置确认按钮 和 动作
                builder.setPositiveButton("确认", (dialog, which) -> {
                        //必须不是空
                        if (!input.getText().toString().isEmpty())
                        {
                                //传递要跳转的页码
                                int page = Integer.valueOf(input.getText().toString());
                                refreshPosts(page);
                        }
                        else
                        {
                                ToastUtils.shortToast("未输入页数!");
                        }

                });
                //设置取消按钮 和 无动作
                builder.setNegativeButton("取消", (dialog, which) -> {
                });
                //创建弹窗
                final AlertDialog alertDialog = builder.create();

                //监听键盘动作
                input.setOnEditorActionListener((v, actionId, event) -> {
                        //如果从键盘点了确认键
                        if (actionId == EditorInfo.IME_ACTION_DONE)
                        {
                                //触发弹窗确认键的点击
                                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
                        }
                        return true;
                });
                //显示弹窗
                alertDialog.show();

        }

}
