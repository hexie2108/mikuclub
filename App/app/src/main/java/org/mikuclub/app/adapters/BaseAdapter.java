package org.mikuclub.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 带头部和尾部支持的 多布局基础适配器
 */
public abstract class BaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
        /*静态变量*/
        //正常内容
        protected final static int TYPE_ITEM = 0;
        //头部类型
        protected final static int TYPE_HEADER = 1;
        //尾部类型
        protected final static int TYPE_FOOTER = 2;


        /*变量*/

        //数据列表
        private List adapterList;
        //上下文
        private Context adapterContext;
        //布局创建器
        private LayoutInflater adpterInflater;

        //头部占据的列数 默认为0
        private int headerRow = 0;
        //尾部占据的列数 默认为0
        private int footerRow = 0;

        //加载 指示器
        private boolean isLoading = false;
        //错误指示器: 无更多内容
        private boolean notMoreError = false;
        //无更多内容 对应错误信息
        private String notMoreErrorMessage = "已经到底了~";
        //错误指示器: 网络错误
        private boolean internetError = false;
        //网络错误对应错误信息
        private String internetErrorMessage = "加载失败, 请点击重试";
        //网络错误 对应点击事件监听器
        private View.OnClickListener internetErrorListener = null;


        /**
         * 构建函数
         *
         * @param adapterList
         * @param context
         */
        protected BaseAdapter(List adapterList, Context context)
        {
                this.adapterList = adapterList;
                this.adapterContext = context;
                this.adpterInflater = LayoutInflater.from(context);
        }


        @Override
        public int getItemViewType(int position)
        {
                int type;
                //如果是头部行范围内
                if (position < headerRow)
                {
                        type = TYPE_HEADER;
                }
                //如果是尾部 ( 位置 = 列表位置+头部位置+尾部位置)
                else if (position == (adapterList.size() - 1) + headerRow + footerRow)
                {
                        type = TYPE_FOOTER;
                }
                //如果是默认数据
                else
                {
                        type = TYPE_ITEM;
                }
                return type;

        }


        @Override
        public int getItemCount()
        {
                //获取列表长度, 等于 数据长度 + 头部 + 尾部
                return adapterList.size() + headerRow + footerRow;
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
                //声明控制器变量
                RecyclerView.ViewHolder holder;

                //如果是普通数据类型
                if (viewType == TYPE_ITEM)
                {
                        holder = onCreateItemViewHolder(parent);
                }
                //如果是头部类型
                else if (viewType == TYPE_HEADER)
                {
                        holder = onCreateHeaderViewHolder(parent);
                }
                //如果是尾部类型
                else
                {
                        holder = onCreateFooterViewHolder(parent);
                }
                return holder;
        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
        {
                int viewType = holder.getItemViewType();
                //如果是普通数据类型
                if (viewType == TYPE_ITEM)
                {
                        onBindItemViewHolder(holder, position);
                }
                //如果是头部类型
                else if (viewType == TYPE_HEADER)
                {
                        onBindHeaderViewHolder(holder, position);
                }
                //如果是尾部类型
                else
                {
                        onBindFooterViewHolder(holder, isLoading, notMoreError, internetError, internetErrorListener);
                }
        }


        /**
         * 创建普通数据控制器
         *
         * @param parent
         * @return
         */
        protected abstract RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent);

        /**
         * 创建头部控制器
         *
         * @param parent
         * @return
         */
        protected abstract RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent);

        /**
         * 创建尾部控制器
         *
         * @param parent
         * @return
         */
        protected abstract RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent);


        /**
         * 绑定普通数据控制器, 显示数据
         *
         * @param holder
         * @param position
         */
        protected abstract void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position);

        /**
         * 绑定头部数据控制器, 显示头部数据
         *
         * @param holder
         * @param position
         */
        protected abstract void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position);

        /**
         * 绑定错误信息管理器, 显示错误信息
         *
         * @param holder
         * @param isLoading             正在加载
         * @param notMoreError          没有更多错误
         * @param internetError         网络错误
         * @param internetErrorListener 网络错误监听器
         */
        protected abstract void onBindFooterViewHolder(RecyclerView.ViewHolder holder, boolean isLoading, boolean notMoreError, boolean internetError, final View.OnClickListener internetErrorListener);


        /**
         * 更新尾部的错误状态, 并更新UI页面
         *
         * @param isLoading
         * @param notMoreError
         * @param internetError
         */
        public void updateFooterStatus(boolean isLoading, boolean notMoreError, boolean internetError)
        {

                setLoading(isLoading);
                setNotMoreError(notMoreError);
                setInternetError(internetError);
                //获取尾部位置
                int position = getAdapterList().size()+headerRow;
                //更新UI
                notifyItemChanged(position);

        }


        /**
         * 获取item对应的数据在列表的真实位置
         * 如果头部存在, 会导致position的偏移
         * 如果头部不存在则不做修改
         *
         * @param position
         * @return
         */
        public int getListPosition(int position)
        {
                return position - getHeaderRow();
        }


        public List getAdapterList()
        {
                return adapterList;
        }

        public void setAdapterList(List adapterList)
        {
                this.adapterList = adapterList;
        }

        public Context getAdapterContext()
        {
                return adapterContext;
        }

        public void setAdapterContext(Context adapterContext)
        {
                this.adapterContext = adapterContext;
        }

        public LayoutInflater getAdpterInflater()
        {
                return adpterInflater;
        }

        public void setAdpterInflater(LayoutInflater adpterInflater)
        {
                this.adpterInflater = adpterInflater;
        }

        public int getHeaderRow()
        {
                return headerRow;
        }

        public void setHeaderRow(int headerRow)
        {
                this.headerRow = headerRow;
        }

        public int getFooterRow()
        {
                return footerRow;
        }

        public void setFooterRow(int footerRow)
        {
                this.footerRow = footerRow;
        }

        public boolean isLoading()
        {
                return isLoading;
        }

        public void setLoading(boolean loading)
        {
                isLoading = loading;
        }

        public boolean isNotMoreError()
        {
                return notMoreError;
        }

        public void setNotMoreError(boolean notMoreError)
        {
                this.notMoreError = notMoreError;
        }

        public String getNotMoreErrorMessage()
        {
                return notMoreErrorMessage;
        }

        public void setNotMoreErrorMessage(String notMoreErrorMessage)
        {
                this.notMoreErrorMessage = notMoreErrorMessage;
        }

        public boolean isInternetError()
        {
                return internetError;
        }

        public void setInternetError(boolean internetError)
        {
                this.internetError = internetError;
        }

        public String getInternetErrorMessage()
        {
                return internetErrorMessage;
        }

        public void setInternetErrorMessage(String internetErrorMessage)
        {
                this.internetErrorMessage = internetErrorMessage;
        }

        public View.OnClickListener getInternetErrorListener()
        {
                return internetErrorListener;
        }

        public void setInternetErrorListener(View.OnClickListener internetErrorListener)
        {
                this.internetErrorListener = internetErrorListener;
        }


}
