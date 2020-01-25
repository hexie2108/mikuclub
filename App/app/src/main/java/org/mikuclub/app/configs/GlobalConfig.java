package org.mikuclub.app.configs;

import java.util.ArrayList;

/**
 * 全局配置
 */
public class GlobalConfig
{
        //图片缓存大小 (MB)
        public static final int CACHE_MAX_SIZE = 200;
        //网络请求重试间隔 (毫秒)
        public static final int RETRY_TIME = 5000;
        //网络请求(上传大文件) 重试间隔 (毫秒)
        public static final int RETRY_TIME_FOR_FILE= 3000;

        //JSON用的日期格式
        public static final String DATE_FORMAT_JSON = "yyyy-MM-dd'T'HH:mm:ss";
        public static final String DATE_FORMAT_JSON_CUSTOM_ENDPOINTS = "yyyy-MM-dd HH:mm:ss";
        public static final String DATE_FORMAT = "yy-MM-dd HH:mm";

        //浮动弹窗的高度屏幕占比%
        public static final float HEIGHT_PERCENTAGE_OF_FLOAT_WINDOWS = 0.6f;

        //缩微图大小
        public static final String THUMBNAIL_SIZE = "-500x280";

        //wordpress管理员ID
        public static final int ADMIN_USER_ID = 1;


        //百度网盘地址判断
        public static final String BAIDU_PAN_URL_VALIDATE_PATH = "pan.baidu.com/s/";
        //百度网盘 传递URL的参数名称, 因为百度app策略, 分享id里的第一位数字1, 需要被手动去除, 否则无效
        public static final String BAIDU_PAN_PARAMETER_NAME = "surl";
        //百度网盘app url唤醒格式
        public static final String BAIDU_PAN_APP_WAKE_URL = "bdnetdisk://n/action.SHARE_LINK";

        //b站视频前缀地址
        public static final String BILIBILI_HOST = "https://www.bilibili.com/video/";
        //bilibili app url唤醒格式
        public static final String BILIBILI_APP_WAKE_URL = "bilibili://video/";

        //后台服务器地址
        public class Server{

                // public static final String ROOT = "http://192.168.1.99/wordpress/wp-json/wp/v2/";
                public static final String HOST = "https://www.mikuclub.org/";
                public static final String ROOT = HOST+"wp-json/wp/v2/";
                public static final String POSTS = "posts/";

                public static final String TAGS = "tags/";
                public static final String COMMENTS = "comments/";
                public static final String MEDIA = "media/";
                public static final String USERS = "users/";

                //忘记密码地址
                public static final String FORGOTTEN_PASSWORD = HOST+"wp-login.php?action=lostpassword";

                //JWT 接口
                private static final String JWT = "wp-json/jwt-auth/v1/";

                //POST 登陆接口
                public static final String LOGIN = HOST+JWT+"token/";
                //POST 令牌有效性检查
                public static final String TOKEN_VALIDATE = HOST+JWT+"token/validate/";

                //自定义UTLS接口
                private static final String UTILS = "wp-json/utils/v2/";

                //GET 检查应用更新
                public static final String APP_UPDATE = HOST+UTILS+"app_update/";
                //GET 获取分类信息
                public static final String CATEGORIES = HOST+UTILS+"get_menu/";
                //POST  设置文章点赞计数
                public static final String POST_LIKE_COUNT = HOST+UTILS+"post_like_count/";
                //GET  设置文章分享计数
                public static final String POST_SHARING_COUNT= HOST+UTILS+"post_sharing_count/";
                //GET  设置文章查看计数
                public static final String POST_VIEW_COUNT= HOST+UTILS+"post_view_count/";
                //GET  设置文章失效计数
                public static final String POST_FAIL_DOWN_COUNT= HOST+UTILS+"fail_down/";

                //POST 发送私信, GET 获取私信, DELETE 删除私信
                public static final String PRIVATE_MESSAGE = HOST+UTILS+"message/";
                //GET 获取私信数量
                public static final String PRIVATE_MESSAGE_COUNT = HOST+UTILS+"message_count/";

                //GET 获取回复评论, DELETE 删除评论
                public static final String REPLY_COMMENTS = HOST+UTILS+"comments/";
                //GET 获取回复评论数量
                public static final String REPLY_COMMENTS_COUNT = HOST+UTILS+"comments_count/";

        }

        /**
         * sharePreference专用变量名
         */
        public class Preferences{
                //一天的毫秒数
                public static final long DAY_IN_MILLISECONDS = 1000 * 60 *60 *24;

                //每次更新检查的有效戒指时间
                public static final String APP_UPDATE_EXPIRE = "app_update_cache_expire";
                //每次更新检查的有效周期
                public static final long APP_UPDATE_EXPIRE_TIME = 1000; //内测阶段 每1秒过期
                //public long APP_UPDATE_EXPIRE_TIME = DAY_IN_MILLISECONDS * 1; //以后上线 可以每天检查一次


                //菜单缓存
                public static final String CATEGORIES_CACHE ="categories_cache";
                //菜单缓存的时间
                public static final String CATEGORIES_CACHE_EXPIRE ="categories_cache_expire";
                //菜单缓存的有效期
                public static final long CATEGORIES_CACHE_EXPIRE_TIME = DAY_IN_MILLISECONDS * 15; //每15天检查一次





                //点赞过的文章ID数组
                public static final String POST_LIKED_ARRAY="user_token_time";
                //点赞过的文章ID数组长度
                public static final int POST_LIKED_ARRAY_SIZE = 300;
        }


        //魔法区分类id
        public static final int CATEGORY_ID_MOFA = 1120;


        //每页显示的文章数量
        public static final int NUMBER_PER_PAGE = 12;
        //每页显示的评论数量
        public static final int NUMBER_PER_PAGE_OF_COMMENTS = 10;
        //每页显示的消息数量
        public static final int NUMBER_PER_PAGE_OF_MESSAGE = 12;
        //首页幻灯片的文章数量
        public static final int NUMBER_PER_PAGE_OF_SLIDERSHOW = 5;
        //提前多少item触发自动加载
        public static final int PRE_LOAD_ITEM_NUMBER = 3;



        public class OrderBy{
                public static final String DATE = "date";
        }

        public class Order{
                public static final String ASC = "asc";
                public static final String DESC = "desc";
        }
        public class Status{
                public static final String PUBLISH = "publish";
        }



}
