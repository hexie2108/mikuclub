package org.mikuclub.app.adapters.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import mikuclub.app.R;

/**
 * 文章列表 头部控制器 (显示 置顶文章幻灯片)
 */
public class AuthorHeaderViewHolder extends RecyclerView.ViewHolder{


        //首页幻灯片
        private View container;
        private ImageView authorImg;
        private TextView authorName;
        private TextView authorDescription;
        private Button buttonSendMessage;

        public AuthorHeaderViewHolder(@NonNull View itemView)
        {
                super(itemView);
                //管理器绑定各项 视图
                //获取组件
                container = itemView;
                authorImg = itemView.findViewById(R.id.author_img);
                authorName = itemView.findViewById(R.id.author_name);
                authorDescription = itemView.findViewById(R.id.author_description);
                buttonSendMessage = itemView.findViewById(R.id.button_send_message);
        }

        public Button getButtonSendMessage()
        {
                return buttonSendMessage;
        }

        public void setButtonSendMessage(Button buttonSendMessage)
        {
                this.buttonSendMessage = buttonSendMessage;
        }

        public View getContainer()
        {
                return container;
        }

        public void setContainer(View container)
        {
                this.container = container;
        }

        public ImageView getAuthorImg()
        {
                return authorImg;
        }

        public void setAuthorImg(ImageView authorImg)
        {
                this.authorImg = authorImg;
        }

        public TextView getAuthorName()
        {
                return authorName;
        }

        public void setAuthorName(TextView authorName)
        {
                this.authorName = authorName;
        }

        public TextView getAuthorDescription()
        {
                return authorDescription;
        }

        public void setAuthorDescription(TextView authorDescription)
        {
                this.authorDescription = authorDescription;
        }
}