package org.mikuclub.app.configs;

/**
 * questo classe contiene tutti i variabile modificabile del sistema
 */
public class GlobalConfig
{
        //la dimensione del cache è 100MB
        public static final int CACHE_MAX_SIZE = 100;
        //il tempo di attesa per rimandare la richiesta HTTP  (5s)
        public static final int RETRY_TIME = 5000;
        //il tempo di attesa per rimandare la richiesta HTTP in caso di upload file (30s)
        public static final int RETRY_TIME_FOR_FILE= 5000;

        //tags per individuare la provenienza della richiesta, serve per annullare la richiesta in caso di necessita.
        public static final String TAG_HOME = "home";

        //formato data per JSON
        public static final String DATE_FORMAT_JSON = "yyyy-MM-dd'T'HH:mm:ss";


        //endpoints di backend
       // public static final String SERVER_URL = "http://192.168.1.99/wordpress/wp-json/wp/v2/";
        public static final String SERVER_URL = "https://www.mikuclub.org/wp-json/wp/v2/";
        public static final String POSTS_URL = "posts/";
        public static final String CATEGORIES_URL = "categories/";
        public static final String TAGS_URL = "tags/";
        public static final String COMMENTS_URL = "comments/";
        public static final String MEDIA_URL = "media/";
        public static final String USERS_URL = "users/";



        //number of element for page
        public static final int NUMBER_FOR_RECENTLY_POSTS_LIST = 10;
        //number of sticky posts for slidershow
        public static final int NUMBER_FOR_SLIDERSHOW = 5;


}
