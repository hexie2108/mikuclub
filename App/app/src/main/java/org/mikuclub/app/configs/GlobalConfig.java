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
        public static final String DATE_FORMAT = "yy-MM-dd HH:mm";

        //浮动弹窗的高度屏幕占比%
        public static final float HEIGHT_PERCENTAGE_OF_FLOAT_WINDOWS = 0.6f;

        //缩微图大小
        public static final String THUMBNAIL_SIZE = "-500x280";

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


                //GET 检查应用更新
                public static final String APP_UPDATE = HOST+"wp-json/utils/v2/app_update/";
                //GET 获取缓存评论
                public static final String CATEGORIES = HOST+"wp-json/utils/v2/get_menu/";
                //POST 登陆接口
                public static final String LOGIN = HOST+"wp-json/jwt-auth/v1/token/";
                //POST 令牌有效性检查
                public static final String TOKEN_VALIDATE = HOST+"wp-json/jwt-auth/v1/token/validate/";

                //POST  设置文章点赞
                public static final String LIKE_POST = HOST+"wp-json/utils/v2/post_like_count/";

                //忘记密码地址
                public static final String FORGOTTEN_PASSWORD = HOST+"wp-login.php?action=lostpassword";
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


                //用户登陆信息
                public static final String USER_LOGIN ="user_login";
                //用户令牌
                public static final String USER_TOKEN="user_token";


                //点赞过的文章ID数组
                public static final String POST_LIKED_ARRAY="user_token_time";
                //点赞过的文章ID数组长度
                public static final int POST_LIKED_ARRAY_SIZE = 300;
        }


        //魔法区分类id
        public static final int CATEGORY_ID_MOFA = 1120;


        //每页显示的文章数量
        public static final int NUMBER_PER_PAGE = 12;
        //每页显示的p评论数量
        public static final int NUMBER_PER_PAGE_OF_COMMENTS = 10;
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
