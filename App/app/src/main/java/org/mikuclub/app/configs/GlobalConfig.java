package org.mikuclub.app.configs;

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
        public static final int RETRY_TIME_FOR_FILE= 5000;

        //网络请求标签名, 用来区分请求来源, 以便在需要的时候取消请求
//        public static final String TAG_HOME = "home";
//        public static final String TAG_SEARCH = "search";

        //JSON用的日期格式
        public static final String DATE_FORMAT_JSON = "yyyy-MM-dd'T'HH:mm:ss";


        //后台服务器地址
        public class Server{

                // public static final String ROOT = "http://192.168.1.99/wordpress/wp-json/wp/v2/";
                public static final String ROOT = "https://www.mikuclub.org/wp-json/wp/v2/";
                public static final String POSTS = "posts/";
                public static final String CATEGORIES = "categories/";
                public static final String TAGS = "tags/";
                public static final String COMMENTS = "comments/";
                public static final String MEDIA = "media/";
                public static final String USERS = "users/";

        }





        //每页显示的文章数量
        public static final int NUMBER_FOR_PAGE = 16;
        //每页显示的文章数量 (最近发布)
        public static final int NUMBER_FOR_RECENTLY_POSTS_LIST = 10;
        //首页幻灯片的文章数量
        public static final int NUMBER_FOR_SLIDERSHOW = 5;
        //列表开始自动加载的 高度百分比 (默认游览超过50%就开始加载)
        public static final float LIST_PRE_LOAD_HEIGHT_RATION = 0.50f;


}
