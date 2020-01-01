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

        //网络请求标签名, 用来区分请求来源, 以便在需要的时候取消请求
//        public static final String TAG_HOME = "home";
//        public static final String TAG_SEARCH = "search";

        //JSON用的日期格式
        public static final String DATE_FORMAT_JSON = "yyyy-MM-dd'T'HH:mm:ss";

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


                public static final String APP_UPDATE = HOST+"app/update.json";
                public static final String CATEGORIES = HOST+"wp-json/utils/v2/get_menu";

        }

        //软件更新检查周期
        public static final long APP_UPDATE_CHECK_CYCLE = 1000; //内测阶段 每次都检查更新
        //public long APP_UPDATE_CHECK_CYCLE = 1000 * 60 * 60 * 24 * 1; //以后上线 可以每天检查一次
        //最后一次检查更新时间的键名
        public static final String APP_UPDATE_CACHE_TIME= "app_update_cache_time";

        //菜单更新检查周期
        public static final long CATEGORIES_CHECK_CYCLE = 1000 * 60 * 60 * 24 * 15; //每半月检查一次
        //最后一次菜单更新时间的键名
        public static final String CATEGORIES_CACHE_TIME="categories_cache_time";
        //储存菜单的键名
        public static final String CATEGORIES_CACHE ="categories_cache";





        //每页显示的文章数量
        public static final int NUMBER_PER_PAGE = 12;
        //每页显示的p评论数量
        public static final int NUMBER_PER_PAGE_OF_COMMENTS = 10;

        //每个列表能存放的最大文章数量
        public static final int MAX_NUMBER_POST_FOR_LIST = NUMBER_PER_PAGE * 3;

        //每页显示的文章数量 (最近发布)
        public static final int NUMBER_FOR_RECENTLY_POSTS_LIST = 10;
        //首页幻灯片的文章数量
        public static final int NUMBER_FOR_SLIDERSHOW = 5;
        //提前多少item触发自动加载
        public static final int PRE_LOAD_ITEM_NUMBER = 3;



}
