package org.mikuclub.app.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.mikuclub.app.adapter.base.BaseAdapterWithFooter;
import org.mikuclub.app.javaBeans.response.baseResource.ImagePreview;
import org.mikuclub.app.ui.activity.PostSubmitActivity;
import org.mikuclub.app.utils.AlertDialogUtils;
import org.mikuclub.app.utils.LogUtils;
import org.mikuclub.app.utils.ResourcesUtils;
import org.mikuclub.app.utils.ToastUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;
import org.mikuclub.app.utils.http.HttpCallBack;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;


/**
 * 文章列表适配器
 * 主体显示 文章列表
 * post recyclerView adapter
 * the main body displays a list of post
 */
public class PostSubmitImageAdapter extends BaseAdapterWithFooter
{

        private PostSubmitActivity postSubmitActivity;

        /**
         * 构建函数 default constructor
         *
         * @param context
         * @param list
         */
        public PostSubmitImageAdapter(Context context, List<ImagePreview> list)
        {
                super(context, list);
                this.postSubmitActivity = (PostSubmitActivity) context;

        }

        @Override
        protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent)
        {
                View view = getAdpterInflater().inflate(R.layout.list_item_post_submit_image, parent, false);
                //创建item控制器
                PostSubmitImageViewHolder holder = new PostSubmitImageViewHolder(view);
                //绑定动作
                setItemOnClickListener(holder);
                return holder;
        }

        @Override
        protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position)
        {
                //当前位置, 排除头部影响
                int fixedPosition = position - getHeaderRow();

                PostSubmitImageViewHolder viewHolder = (PostSubmitImageViewHolder) holder;
                //先从列表获取对应位置的数据
                ImagePreview imagePreview = (ImagePreview) getAdapterListElementWithHeaderRowFix(holder.getAdapterPosition());
                //因为是从1开始计算数量
                viewHolder.getItemRanking().setText(String.valueOf(fixedPosition+1));

                //为视图设置各项数据
                //修复标题中可能存在的被html转义的特殊符号
                //String title = GeneralUtils.unescapeHtml(media.getTitle().getRendered());

                String imgUrl = imagePreview.getSource_url();
                //加载远程图片
                GlideImageUtils.get(getAdapterContext(), viewHolder.getItemImage(), imgUrl);


                //如果是第一位元素
                if (fixedPosition == 0)
                {
                        //隐藏向上按钮
                        viewHolder.getButtonUpward().setVisibility(View.INVISIBLE);
                }
                else
                {
                        //显示向上按钮
                        viewHolder.getButtonUpward().setVisibility(View.VISIBLE);
                }

                //如果列表只有一个元素, 如果是最后一个元素
                if (getAdapterList().size() == 1 || fixedPosition == getAdapterList().size() - 1)
                {
                        //隐藏向下按钮
                        viewHolder.getButtonDownward().setVisibility(View.INVISIBLE);
                }
                else
                {
                        //显示向下按钮
                        viewHolder.getButtonDownward().setVisibility(View.VISIBLE);
                }

        }

        /**
         * 设置主体元素的点击事件监听器
         *
         * @param holder 元素控制器
         */
        private void setItemOnClickListener(final PostSubmitImageViewHolder holder)
        {
                holder.getButtonDelete().setOnClickListener(v -> {

                        deleteAction(holder.getAdapterPosition());
                });
                holder.getButtonUpward().setOnClickListener(v -> {
                        moveAction(holder.getAdapterPosition(), holder.getAdapterPosition() - 1);
                });
                holder.getButtonDownward().setOnClickListener(v -> {
                        moveAction(holder.getAdapterPosition(), holder.getAdapterPosition() + 1);
                });

        }

        /**
         * 上下移动的动作
         */
        private void moveAction(int startPosition, int endPosition)
        {
                LogUtils.e("点击触发");
                int listStartPosition = startPosition - getHeaderRow();
                int listEndPosition = endPosition - getHeaderRow();

                //如果目标位置 在有效列表index范围内
                if (listEndPosition >= 0 && listEndPosition < getAdapterList().size())
                {
                        //交换列表里的元素
                        Collections.swap(getAdapterList(), listStartPosition, listEndPosition);
                        //通知UI列表更新
                        notifyItemChanged(startPosition);
                        notifyItemChanged(endPosition);
                        //notifyItemMoved(startPosition, endPosition);

                }

        }


        /**
         * 删除图片动作
         *
         * @param position
         */
        private void deleteAction(int position)
        {

                ImagePreview imagePreview = (ImagePreview) getAdapterListElementWithHeaderRowFix(position);
                HttpCallBack httpCallBack = new HttpCallBack()
                {
                        @Override
                        public void onSuccess(String response)
                        {
                                //移除元素
                                removeAdapterListElementWithHeaderRowFix(position);
                                //通知列表删除
                                //notifyItemRemoved(position);
                                //列表更新
                                notifyDataSetChanged();
                                //显示弹窗信息
                                ToastUtils.shortToast(ResourcesUtils.getString(R.string.delete_successful));
                        }

                        @Override
                        public void onFinally()
                        {
                                //隐藏进度条
                                postSubmitActivity.getProgressDialog().dismiss();
                        }

                        @Override
                        public void onCancel()
                        {
                                //隐藏进度条
                                postSubmitActivity.getProgressDialog().dismiss();
                        }
                };

                DialogInterface.OnClickListener onClickListener = ((dialog, which) -> {
                        //显示进度条
                        postSubmitActivity.getProgressDialog().show();
                        //发送删除图片请求
                        postSubmitActivity.getMediaDelegate().deleteMedia(httpCallBack, imagePreview.getId());
                });

                //创建确认弹窗
                AlertDialog confirmDialog = AlertDialogUtils.createDeleteConfirmDialog(getAdapterContext(), onClickListener);
                confirmDialog.show();
        }


        public static class PostSubmitImageViewHolder extends RecyclerView.ViewHolder
        {

                private TextView itemRanking;
                private ImageView itemImage;
                private MaterialButton buttonDelete;
                private MaterialButton buttonUpward;
                private MaterialButton buttonDownward;


                public PostSubmitImageViewHolder(@NonNull View itemView)
                {
                        super(itemView);
                        itemRanking = itemView.findViewById(R.id.item_ranking);
                        itemImage = itemView.findViewById(R.id.item_image);
                        buttonDelete = itemView.findViewById(R.id.button_delete);
                        buttonUpward = itemView.findViewById(R.id.button_upward);
                        buttonDownward = itemView.findViewById(R.id.button_downward);
                }

                public TextView getItemRanking()
                {
                        return itemRanking;
                }

                public void setItemRanking(TextView itemRanking)
                {
                        this.itemRanking = itemRanking;
                }

                public MaterialButton getButtonUpward()
                {
                        return buttonUpward;
                }

                public void setButtonUpward(MaterialButton buttonUpward)
                {
                        this.buttonUpward = buttonUpward;
                }

                public MaterialButton getButtonDownward()
                {
                        return buttonDownward;
                }

                public void setButtonDownward(MaterialButton buttonDownward)
                {
                        this.buttonDownward = buttonDownward;
                }

                public ImageView getItemImage()
                {
                        return itemImage;
                }

                public void setItemImage(ImageView itemImage)
                {
                        this.itemImage = itemImage;
                }

                public MaterialButton getButtonDelete()
                {
                        return buttonDelete;
                }

                public void setButtonDelete(MaterialButton buttonDelete)
                {
                        this.buttonDelete = buttonDelete;
                }
        }


}
