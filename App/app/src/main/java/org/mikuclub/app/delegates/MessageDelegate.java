package org.mikuclub.app.delegates;

import org.mikuclub.app.callBack.HttpCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.javaBeans.parameters.BaseParameters;
import org.mikuclub.app.javaBeans.parameters.CreatePrivateMessageParameters;
import org.mikuclub.app.javaBeans.parameters.LoginParameters;
import org.mikuclub.app.utils.http.Request;
import org.mikuclub.app.utils.storage.UserUtils;

import java.util.HashMap;
import java.util.Map;

import static org.mikuclub.app.utils.DataUtils.putIfNotNull;

/**
 * 根据需要生成对应资源的请求
 */
public class MessageDelegate extends BaseDelegate
{

        public MessageDelegate(int tag)
        {
                super(tag);
        }



        /**
         * 发送私信
         *
         * @param httpCallBack
         * @param bodyParameters
         */
        public void sendPrivateMessage(HttpCallBack httpCallBack, CreatePrivateMessageParameters bodyParameters)
        {

                BaseParameters baseParameters = new BaseParameters();
                Request.post(GlobalConfig.Server.PRIVATE_MESSAGE, baseParameters.toMap(), bodyParameters.toMap(), UserUtils.createLoggedUserHeader(), getTag(), httpCallBack);

        }

        /**
         * 获取私信计数
         *
         * @param httpCallBack
         * @param unread       是否是要计算未读私信信息
         * @param selfMessage  计算自己回复的数量
         */
        public void countPrivateMessage(HttpCallBack httpCallBack, boolean unread, boolean selfMessage)
        {
                Map<String, Object> queryParameters = new HashMap();
                putIfNotNull(queryParameters, "_envelope", "1");
                putIfNotNull(queryParameters, "unread", unread);
                putIfNotNull(queryParameters, "self", selfMessage);

                Request.get(GlobalConfig.Server.PRIVATE_MESSAGE_COUNT, queryParameters, UserUtils.createLoggedUserHeader(), getTag(), httpCallBack);

        }

        /**
         * 获取私信
         *
         * @param httpCallBack
         * @param page      页数
         * @param distinct  是否要去除同个发信人的重复私信
         * @param sender_id  是否只要当前用户和sender之间互相写的私信, 0为不要
         */
        public void getPrivateMessage(HttpCallBack httpCallBack,  int page, boolean distinct, Integer sender_id)
        {
                Map<String, Object> queryParameters = new HashMap();
                putIfNotNull(queryParameters, "_envelope", "1");
                putIfNotNull(queryParameters, "distinct", distinct);
                putIfNotNull(queryParameters, "sender_id", sender_id);
                putIfNotNull(queryParameters, "paged", page);
                //如果是正常私信请求
                if(sender_id == null)
                {
                        //使用正常私信数量
                        putIfNotNull(queryParameters, "number", GlobalConfig.NUMBER_PER_PAGE_OF_MESSAGE);
                }
                //如果是请求 互相私信
                else{
                        //因为只请求一次, 所以要用双倍数量
                        putIfNotNull(queryParameters, "number", GlobalConfig.NUMBER_PER_PAGE_OF_MESSAGE*2);
                }

                Request.get(GlobalConfig.Server.PRIVATE_MESSAGE, queryParameters, UserUtils.createLoggedUserHeader(), getTag(), httpCallBack);

        }



        /**
         * 获取私信计数
         *
         * @param httpCallBack
         * @param unread       是否是要计算未读评论
         */
        public void countReplyComment(HttpCallBack httpCallBack, boolean unread)
        {
                Map<String, Object> queryParameters = new HashMap();
                putIfNotNull(queryParameters, "_envelope", "1");
                putIfNotNull(queryParameters, "unread", unread);
                Request.get(GlobalConfig.Server.REPLY_COMMENTS_COUNT, queryParameters, UserUtils.createLoggedUserHeader(), getTag(), httpCallBack);

        }

        /**
         * 获取评论回复
         *
         * @param httpCallBack
         * @param page      页数
         */
        public void getReplyComment(HttpCallBack httpCallBack,  int page)
        {
                Map<String, Object> queryParameters = new HashMap();
                putIfNotNull(queryParameters, "_envelope", "1");
                putIfNotNull(queryParameters, "paged", page);
                putIfNotNull(queryParameters, "number", GlobalConfig.NUMBER_PER_PAGE_OF_MESSAGE);

                Request.get(GlobalConfig.Server.REPLY_COMMENTS, queryParameters, UserUtils.createLoggedUserHeader(), getTag(), httpCallBack);

        }



}
