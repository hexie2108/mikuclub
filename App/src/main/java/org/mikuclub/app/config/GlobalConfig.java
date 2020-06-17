package org.mikuclub.app.config;

/**
 * 全局静态变量配置
 * Global static variable configuration
 */
public class GlobalConfig
{

        //网络请求重试间隔 (毫秒)
        public static final int RETRY_TIME = 3000;
        //网络请求(上传大文件) 重试间隔 (毫秒)
        public static final int RETRY_TIME_FOR_FILE = 3000;

        //JSON用的日期格式
        public static final String DATE_FORMAT_JSON = "yyyy-MM-dd'T'HH:mm:ss";
        public static final String DATE_FORMAT_JSON_CUSTOM_ENDPOINTS = "yyyy-MM-dd HH:mm:ss";

        //显示的日期格式
        public static final String DISPLAY_DATE_FORMAT = "yy-MM-dd HH:mm";

        //浮动弹窗的高度屏幕占比%
        public static final float HEIGHT_PERCENTAGE_OF_FLOAT_WINDOWS_VERTICAL = 0.65f;
        public static final float HEIGHT_PERCENTAGE_OF_FLOAT_WINDOWS_HORIZONTAL = 0.75f;
        //横屏状态下 文章页面 顶部可折叠容器的初始高度
        public static final float HEIGHT_PERCENTAGE_OF_COLLAPSING_TOOLBAR_HORIZONTAL = 0.7f;

        //缩微图大小
        public static final String THUMBNAIL_SIZE = "-500x280";

        //用户头像的大小 px
        public static final int USER_AVATAR_SIZE = 100;

        //wordpress管理员ID
        public static final int ADMIN_USER_ID = 1;
        //魔法区分类id
        public static final int CATEGORY_ID_MOFA = 1120;
        //赞助页 文章ID
        public static final int SPONSOR_POST_ID = 150107;

        //一天的毫秒数
        public static final long DAY_IN_MILLISECONDS = 1000 * 60 * 60 * 24;

        //每页显示的文章数量
        public static final int NUMBER_PER_PAGE = 12;
        //每页显示的评论数量
        public static final int NUMBER_PER_PAGE_OF_COMMENTS = 10;
        //每页显示的消息数量
        public static final int NUMBER_PER_PAGE_OF_MESSAGE = 12;
        //首页幻灯片的文章数量
        public static final int NUMBER_PER_PAGE_OF_SLIDERS = 5;
        //提前多少item触发自动加载
        public static final int PRE_LOAD_ITEM_NUMBER = 3;

        //投稿的最大预览图片数量
        public static final int MAX_IMAGE_PREVIEWS_COUNT = 8;


        /**
         * 第三方应用的schema接口和网络地址
         */
        public static class ThirdPartyApplicationInterface
        {
                public static final String HTTPS_SCHEME = "https://";

                //百度网盘地址判断
                public static final String BAIDU_PAN_URL_VALIDATE_PATH = "pan.baidu.com/s/";
                //百度网盘 传递URL的参数名称, 因为百度app策略, 分享id里的第一位数字1, 需要被手动去除, 否则无效
                public static final String BAIDU_PAN_PARAMETER_NAME = "surl";
                //百度网盘app url唤醒格式
                public static final String BAIDU_PAN_APP_WAKE_URL = "bdnetdisk://n/action.SHARE_LINK";

                //b站视频识别码
                public static final String BILIBILI_AV = "av";
                //b站新视频识别码
                public static final String BILIBILI_BV = "BV";
                //b站视频前缀地址
                public static final String BILIBILI_HOST = "https://www.bilibili.com/video/";
                //bilibili app url唤醒格式
                public static final String BILIBILI_APP_WAKE_URL = "bilibili://video/";

                public static final String TAOBAO_SCHEME = "taobao://";
                public static final String TAOBAO_SHOP_HOME = "mikuclub.taobao.com/";

                public static final String TAOBAO_APK_URL = TAOBAO_SCHEME + TAOBAO_SHOP_HOME;
                public static final String TAOBAO_WEB_URL = HTTPS_SCHEME + TAOBAO_SHOP_HOME;

        }


        //后台服务器地址
        public class Server
        {

                // public static final String ROOT = "http://192.168.1.99/wordpress/wp-json/wp/v2/";
                public static final String HOST = "https://www.mikuclub.org/";
                public static final String ROOT = HOST + "wp-json/wp/v2/";
                public static final String POSTS = "posts/";

                public static final String TAGS = "tags/";
                public static final String COMMENTS = "comments/";
                public static final String MEDIA = "media/";
                public static final String USERS = "users/";

                //忘记密码地址
                public static final String FORGOTTEN_PASSWORD = HOST + "wp-login.php?action=lostpassword";

                //JWT 接口
                private static final String JWT = "wp-json/jwt-auth/v1/";

                //POST 登陆接口
                public static final String LOGIN = HOST + JWT + "token/";
                //POST 令牌有效性检查
                public static final String TOKEN_VALIDATE = HOST + JWT + "token/validate/";

                //自定义UTLS接口
                private static final String UTILS = "wp-json/utils/v2/";

                //GET 检查应用更新
                public static final String APP_UPDATE = HOST + UTILS + "app_update/";
                //GET 获取分类信息
                public static final String CATEGORIES = HOST + UTILS + "get_menu/";
                //POST  设置文章点赞计数
                public static final String POST_LIKE_COUNT = HOST + UTILS + "post_like_count/";
                //GET  设置文章分享计数
                public static final String POST_SHARING_COUNT = HOST + UTILS + "post_sharing_count/";
                //GET  设置文章查看计数
                public static final String POST_VIEW_COUNT = HOST + UTILS + "post_view_count/";
                //GET  设置文章失效计数
                public static final String POST_FAIL_DOWN_COUNT = HOST + UTILS + "fail_down/";

                //POST 发送私信, GET 获取私信, DELETE 删除私信
                public static final String PRIVATE_MESSAGE = HOST + UTILS + "message/";
                //GET 获取私信数量
                public static final String PRIVATE_MESSAGE_COUNT = HOST + UTILS + "message_count/";

                //GET 获取回复评论, DELETE 删除评论
                public static final String REPLY_COMMENTS = HOST + UTILS + "comments/";
                //GET 获取回复评论数量
                public static final String REPLY_COMMENTS_COUNT = HOST + UTILS + "comments_count/";

                //GET 获取作者信息, 自定义接口,因为官方接口有权限问题
                public static final String GET_AUTHOR = HOST + UTILS + "author/";

                //GET 新发布文章计数
                public static final String NEW_POST_COUNT = HOST + UTILS + "new_post_count/";

                //POST 更新文章和附件的meta元数据
                public static final String POST_META = HOST + UTILS + "post_meta/";

                //POST 更新用户信息
                public static final String UPDATE_USER = HOST + "wp-json/wp/v2/users/me/";

                //GET 获取站点信息通知
                public static final String SITE_COMMUNICATION = HOST + UTILS + "get_app_communication/";

                //GET 获取收藏夹, POST设置收藏夹, DELETE删除收藏夹ID
                public static final String POST_FAVORITE = HOST + UTILS + "favorite/";

        }

        /**
         * 资源的元数据
         */
        public class Metadata
        {

                public class Attachment
                {
                        //用来注明该附件图片 是头像, 和储存作者ID
                        public static final String _WP_ATTACHMENT_WP_USER_AVATAR = "_wp_attachment_wp_user_avatar";

                }

                public class User
                {
                        //储存 用户使用的头像 的post id
                        public static final String MM_USER_AVATAR = "mm_user_avatar";
                }


        }

        /**
         * sharePreference专用变量名
         */
        public static class Preferences
        {


                //每次更新检查的有效截止时间
                public static final String APP_UPDATE_EXPIRE = "app_update_cache_expire";
                //每次更新检查的有效周期
                public static final long APP_UPDATE_EXPIRE_TIME = DAY_IN_MILLISECONDS ; // 每天检查一次更新

                //网站通知缓存
                public static final String SITE_COMMUNICATION = "app_communication";
                //网站通知的有效时间
                public static final String SITE_COMMUNICATION_EXPIRE = "app_communication_expire";
                //每次更新检查的有效周期
                public static final long SITE_COMMUNICATION_EXPIRE_TIME = DAY_IN_MILLISECONDS; // 每天检查一次更新

                //菜单缓存
                public static final String CATEGORIES_CACHE = "categories_cache";
                //菜单缓存的时间
                public static final String CATEGORIES_CACHE_EXPIRE = "categories_cache_expire_1";
                //菜单缓存的有效期
                public static final long CATEGORIES_CACHE_EXPIRE_TIME = DAY_IN_MILLISECONDS ; //每1天检查一次

                //点赞过的文章ID数组
                public static final String POST_LIKED_ARRAY = "post_liked";
                //点赞过的文章ID数组长度
                public static final int POST_LIKED_ARRAY_SIZE = 300;
                //最近的访问时间
                public static final String LATEST_ACCESS_TIME = "latest_access_time";

                //收藏的文章ID数组
                public static final String POST_FAVORITE = "post_favorite";


        }

        public static class Post
        {
                public static class OrderBy
                {
                        public static final String DATE = "date";
                        public static final String INCLUDE= "include";
                }

                public static class Order
                {
                        public static final String ASC = "asc";
                        public static final String DESC = "desc";
                }

                public static class Status
                {
                        public static final String PUBLISH = "publish";
                        public static final String DRAFT = "draft";
                        public static final String PENDING = "pending";
                        //投稿管理页面的的文章状态
                        public static final String[] POST_MANAGE_STATUS = {PUBLISH, DRAFT, PENDING};
                }


        }


}
