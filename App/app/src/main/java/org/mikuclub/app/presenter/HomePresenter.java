package org.mikuclub.app.presenter;

import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.configs.PostConstants;
import org.mikuclub.app.javaBeans.parameters.ParametersListPosts;
import org.mikuclub.app.models.PostModel;

/**
 * questo classe occuppa tutte le operazioni logici per recuperare i dati e aggiornare UI.
 */
public class HomePresenter
{

        private PostModel postModel;
        private String tag ;

        public HomePresenter(){

                postModel = PostModel.getInstance();
                tag = GlobalConfig.TAG_HOME;
        }


        /*
get lista di post più recente e aggiornare ui
 */
        public void getStickyPostList(int start, WrapperCallBack wrapperCallBack){

                int page = start / GlobalConfig.NUMBER_FOR_RECENTLY_POSTS_LIST +1;
                ParametersListPosts parametersListPosts = new ParametersListPosts();
                parametersListPosts.setSticky(true);
                parametersListPosts.setOrderby(PostConstants.OrderBy.DATE);
                parametersListPosts.setPer_page(GlobalConfig.NUMBER_FOR_SLIDERSHOW);
                parametersListPosts.setStatus(PostConstants.Status.PUBLISH);
                PostModel.getInstance().selectForList(parametersListPosts.toMap(), tag, wrapperCallBack);

        }

        /*
        get lista di post più recente e aggiornare ui
         */
        public void getRecentlyPostList(int start, WrapperCallBack wrapperCallBack){

                int page = start / GlobalConfig.NUMBER_FOR_RECENTLY_POSTS_LIST +1;
                ParametersListPosts parametersListPosts = new ParametersListPosts();
                parametersListPosts.setOrderby(PostConstants.OrderBy.DATE);
                parametersListPosts.setPer_page(GlobalConfig.NUMBER_FOR_RECENTLY_POSTS_LIST);
                parametersListPosts.setStatus(PostConstants.Status.PUBLISH);
                PostModel.getInstance().selectForList(parametersListPosts.toMap(), tag, wrapperCallBack);

        }

        /**
         * cancella la richiesta HTTP in corso
         */
        public void cancelRequest(){
                postModel.cancelRequest(tag);
        }




}
