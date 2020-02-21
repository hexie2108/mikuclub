package org.mikuclub.app.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.utils.ResourcesUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 基础多布局 recyclerView适配器
 * 支持单独头部布局和单独尾部布局
 * Basic multiple layout recyclerView adapter
 * Support separate head layout and separate tail layout
 */
public abstract class BaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
        /* 静态变量 Static variable */
        //正常内容
        protected final static int TYPE_ITEM = 0;
        //头部类型
        protected final static int TYPE_HEADER = 1;
        //尾部类型
        protected final static int TYPE_FOOTER = 2;


        /* 变量 local variable */
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

        //尾部组件错误管理相关
        //是否 显示加载进度条
        private boolean isLoading = false;
        //是否显示 内容错误 (没有更多内容的情况, respond状态码异常, 等等)
        private boolean notMoreError = false;
        //内容错误的时候 显示的错误信息
        private String notMoreErrorMessage = ResourcesUtils.getString(R.string.not_more_error_message);

        //是否显示 网络错误 (无网络, 连接不到服务器, 服务器异常)
        private boolean internetError = false;
        //网络错误的时候 显示的错误信息
        private String internetErrorMessage = ResourcesUtils.getString(R.string.internet_error_message);
        //网络错误的时候 点击事件监听器
        private View.OnClickListener internetErrorListener = null;


        /**
         * 构建函数 default constructor
         *
         * @param context
         * @param adapterList
         */
        protected BaseAdapter(Context context, List adapterList)
        {
                this.adapterList = adapterList;
                this.adapterContext = context;
                this.adpterInflater = LayoutInflater.from(context);
        }

        /**
         *根据位置获取 列表元素的类型
         * @param position
         * @return
         */
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

        /**
         * 获取列表内元素的数量
         * @return
         */
        @Override
        public int getItemCount()
        {
                //获取对应数据列表的长度 + 头部占用的行数+ 尾部占用的行数
                return adapterList.size() + headerRow + footerRow;
        }

        /**
         * 创建组件的控制器
         * 根据组件类型 调用对应的 创建控制器方法
         * @param parent
         * @param viewType
         * @return
         */
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

        /**
         * 获取控制器并加载数据
         * 先根据位置判断元素的类型, 之后再根据元素的类型, 调用对应的数据加载方法
         * @param holder 元素控制器
         * @param position
         */
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
         * 创建主体元素控制器
         *
         * @param parent
         * @return
         */
        protected abstract RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent);

        /**
         * 创建头部组件控制器
         *
         * @param parent
         * @return
         */
        protected abstract RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent);

        /**
         * 创建尾部组件控制器
         *
         * @param parent
         * @return
         */
        protected abstract RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent);


        /**
         * 获取主体元素控制器, 并显示相关数据
         *
         * @param holder 元素控制器
         * @param position
         */
        protected abstract void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position);

        /**
         * 获取头部组件控制器, 并显示相关数据
         *
         * @param holder 元素控制器
         * @param position
         */
        protected abstract void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position);

        /**
         * 获取尾部组件控制器, 并显示相关数据
         *
         * @param holder 元素控制器
         * @param isLoading     是否正在加载
         * @param notMoreError          是否有内容错误
         * @param internetError         是否有网络错误
         * @param internetErrorListener 网络错误点击事件监听器
         */
        protected abstract void onBindFooterViewHolder(RecyclerView.ViewHolder holder, boolean isLoading, boolean notMoreError, boolean internetError, final View.OnClickListener internetErrorListener);


        /**
         * 更新尾部组件错误信息, 并通知适配器更新UI显示
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
                //获取尾部位置, 排除可能存在的header带来的行数偏移
                int position = getLastItemPositionWithHeaderRowFix();
                //通知更新UI
                notifyItemChanged(position);

        }


        /**
         *获取适配器最后一个元素的位置  (解决可能存在的头部带来的位置偏移)
         * @return
         */
        public int getLastItemPositionWithHeaderRowFix()
        {
                return adapterList.size()+headerRow;
        }

        /**
         * 获取适配器元素对应的数据 (解决可能存在的头部带来的位置偏移)
         * 排除可能存在的头部组件带来的主体元素位置 和 数据列表里的数据位置 之间偏移的问题
         *
         * @param position
         * @return
         */
        public Object getAdapterListElementWithHeaderRowFix(int position){
                return getAdapterList().get(position-headerRow);
        }

        /**
         * 删除适配器元素对应的数据 (解决可能存在的头部带来的位置偏移)
         * 排除可能存在的头部组件带来的主体元素位置 和 数据列表里的数据位置 之间偏移的问题
         *
         * @param position
         * @return
         */
        public void removeAdapterListElementWithHeaderRowFix(int position){
                getAdapterList().remove(position-headerRow);
        }





        public List getAdapterList()
        {
                return adapterList;
        }


        public Context getAdapterContext()
        {
                return adapterContext;
        }


        public LayoutInflater getAdpterInflater()
        {
                return adpterInflater;
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
