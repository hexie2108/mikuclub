package org.mikuclub.app.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import org.mikuclub.app.javaBeans.response.baseResource.Category;
import org.mikuclub.app.ui.activity.CategoryActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

public class CategoriesAdapter extends BaseAdapterWithFooter
{

        /**
         * 构建函数
         *
         * @param list
         * @param context
         */
        public CategoriesAdapter(List<Category> list, Context context)
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
         * 绑定评论框点击事件
         */
        protected void setItemOnClickListener(final CategoryViewHolder holder)
        {
                //绑定评论框点击动作
                holder.getItemButton().setOnClickListener(v -> {
                       //获取对应位置的数据 , 修复可能的position偏移
                        Category category = (Category) getAdapterList().get(holder.getAdapterPosition()-getHeaderRow());
                        CategoryActivity.startAction(getAdapterContext(), category);
                });
        }

        /**
         * 分类控制器 类
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
