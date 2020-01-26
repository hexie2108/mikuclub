package org.mikuclub.app.utils.storage;

/**
 * 操作消息相关的储存参数
 */
public class MessageUtils
{

        private static final String PRIVATE_MESSAGE_COUNT = "private_message_count";
        private static final String REPLY_COMMENT_COUNT = "reply_comment_count";



        /**
         * 获取未读私信数量
         */
        public static int getPrivateMessageCount()
        {
               return PreferencesUtils.getMessagePreference().getInt(PRIVATE_MESSAGE_COUNT, 0);
        }


        /**
         * 设置未读私信数量
         */
        public static void setPrivateMessageCount(int count)
        {
                //储存登陆信息
                PreferencesUtils.getMessagePreference()
                        .edit()
                        .putInt(PRIVATE_MESSAGE_COUNT, count)
                        .apply();
        }


        /**
         * 获取未读评论数量
         */
        public static int getReplyCommentCount()
        {
                return PreferencesUtils.getMessagePreference().getInt(REPLY_COMMENT_COUNT, 0);
        }

        /**
         * 设置未读评论数量
         */
        public static void setReplyCommentCount(int count)
        {
                //储存登陆信息
                PreferencesUtils.getMessagePreference()
                        .edit()
                        .putInt(REPLY_COMMENT_COUNT, count)
                        .apply();
        }





}
