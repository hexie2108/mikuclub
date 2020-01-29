package org.mikuclub.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import org.mikuclub.app.adapter.base.BaseAdapterWithFooter;
import org.mikuclub.app.javaBeans.response.baseResource.Category;
import org.mikuclub.app.ui.activity.CategoryActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 主页分类页面的列表适配器
 * 主体显示 分类列表
 * home category page: recyclerView adapter
 * the main body displays a list of category
 */
public class CategoryAdapter extends BaseAdapterWithFooter
{

        /**
         * 构建函数 default constructor
         *
         * @param list
         * @param context
         */
        public CategoryAdapter(List<Category> list, Context context)
        {
                super(list, context);
        }


        @Override
        protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent)
        {
                View view = getAdpterInflater().inflate(R.layout.list_item_category, parent, false);
                CategoryViewHolder holder = new CategoryViewHolder(view);
                setItemOnClickListener(holder);
                return holder;
        }


        @Override
        protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position)
        {
                CategoryViewHolder viewHolder = (CategoryViewHolder) holder;
                //先从列表获取对应位置的数据
                Category Category = (Category) getAdapterList().get(position);
                //为视图设置数据
                viewHolder.getItemButton().setText(Category.getTitle());
        }

        /**
         * 设置主体元素的点击事件监听器
         * @param holder 元素控制器
         */
        protected void setItemOnClickListener(final CategoryViewHolder holder)
        {
                //绑定评论框点击动作
                holder.getItemButton().setOnClickListener(v -> {
                       //获取对应位置的数据 , 修复可能的position偏移
                        Category category = (Category) getAdapterListElementWithHeaderRowFix(holder.getAdapterPosition());
                        CategoryActivity.startAction(getAdapterContext(), category);
                });
        }

        /**
         * 主页分类列表的控制器
         * category list view holder
         */
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

}
