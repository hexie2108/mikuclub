package org.mikuclub.app.adapters;

import android.content.Context;

import org.mikuclub.app.adapters.viewHolder.CommentViewHolder;
import org.mikuclub.app.javaBeans.resources.Comment;

import java.util.List;

public class CommentRepliesAdapter extends CommentsAdapter
{


        /**
         * 构建函数
         *
         * @param list
         * @param context
         */
        public CommentRepliesAdapter(List<Comment> list, Context context)
        {
                super(list, context);
        }


        @Override
        protected void setItemOnClickListener(final CommentViewHolder holder)
        {

        }
}
