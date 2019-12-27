package org.mikuclub.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.adapters.viewHolder.CommentViewHolder;
import org.mikuclub.app.adapters.viewHolder.FooterViewHolder;
import org.mikuclub.app.javaBeans.resources.Comment;
import org.mikuclub.app.ui.fragments.FloatCommentRepliesFragment;
import org.mikuclub.app.utils.HttpUtils;
import org.mikuclub.app.utils.http.GlideImageUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import me.wcy.htmltext.OnTagClickListener;
import mikuclub.app.R;

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
