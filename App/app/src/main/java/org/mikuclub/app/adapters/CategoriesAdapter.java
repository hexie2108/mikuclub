package org.mikuclub.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import org.mikuclub.app.adapters.viewHolder.FooterViewHolder;
import org.mikuclub.app.javaBeans.resources.Categories;
import org.mikuclub.app.javaBeans.resources.Category;
import org.mikuclub.app.ui.activity.CategoryActivity;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import me.wcy.htmltext.OnTagClickListener;
import mikuclub.app.R;

public class CategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
        /*静态变量*/
        //正常内容
        private final static int TYPE_ITEM = 0;
        //尾部加载刷新布局
        private final static int TYPE_FOOTER = 1;

        /*变量*/
        //数据列表
        protected List<Category> list;
        //上下文
        protected Context mConxt;
        //布局创建器
        private LayoutInflater mInflater;
        //尾部占据的列数
        private int footerRow = 0;
        //错误情况 指示器
        private boolean notMoreError = false;
        private boolean internetError = false;
        //错误情况的 点击事件监听器
        private View.OnClickListener internetErrorListener = null;


        /**
         * 构建函数
         *
         * @param list
         * @param context
         */
        public CategoriesAdapter(List<Category> list, Context context)
        {
                this.list = list;
                this.mConxt = context;
                this.mInflater = LayoutInflater.from(context);
        }


        @Override
        public int getItemViewType(int position)
        {
                return TYPE_ITEM;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
                //创建控制器
                RecyclerView.ViewHolder holder = null;
                //如果是普通数据类型
                if (viewType == TYPE_ITEM)
                {
                        View view = mInflater.inflate(R.layout.list_item_category, parent, false);
                        holder = new CategoryViewHolder(view);
                        setItemOnClickListener((CategoryViewHolder) holder);

                }
                //如果是footer
                else
                {
                }
                return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
        {
                //需要展示对应子视图的时候
                //如果是普通数据组件
                if (holder instanceof CategoryViewHolder)
                {
                        bindCategoryViewHolder(holder, position);

                }
                else if (holder instanceof FooterViewHolder)
                {
                        AdapterUtils.bindFooterViewHolder(holder, notMoreError, true, internetError, internetErrorListener);
                }

        }

        @Override
        public int getItemCount()
        {
                //获取列表长度
                //因为增加了 footer组件, 所以长度要加上footer占据的数量
                return list.size() + footerRow;
        }


        protected void bindCategoryViewHolder(RecyclerView.ViewHolder viewHolder, int position)
        {
                CategoryViewHolder holder = (CategoryViewHolder) viewHolder;
                //先从列表获取对应位置的数据
                Category Category = list.get(position);
                //为视图设置数据
                holder.getItemButton().setText(Category.getTitle());

        }

        /**
         * 绑定评论框点击事件
         */
        protected void setItemOnClickListener(final CategoryViewHolder holder)
        {
                //绑定评论框点击动作
                holder.getItemButton().setOnClickListener(new View.OnClickListener()
                {
                        @Override
                        public void onClick(View v)
                        {

                                Category category = list.get(holder.getAdapterPosition());
                                CategoryActivity.startAction(mConxt, category);
                        }
                });
        }


        public class CategoryViewHolder extends RecyclerView.ViewHolder
        {

                private MaterialButton itemButton;

                public CategoryViewHolder(@NonNull View itemView)
                {
                        super(itemView);
                        this.itemButton = itemView.findViewById(R.id.item_button);
                }

                public MaterialButton getItemButton()
                {
                        return itemButton;
                }

                public void setItemButton(MaterialButton itemButton)
                {
                        this.itemButton = itemButton;
                }
        }


        /**
         * 设置 没有更多 错误
         *
         * @param notMoreError
         */
        public void setNotMoreError(boolean notMoreError)
        {
                this.notMoreError = notMoreError;
        }

        /**
         * 设置 网络错误 和 重试点击监听器
         *
         * @param internetError
         * @param internetErrorListener
         */
        public void setInternetError(boolean internetError, View.OnClickListener internetErrorListener)
        {
                this.internetError = internetError;
                this.internetErrorListener = internetErrorListener;
        }


}
