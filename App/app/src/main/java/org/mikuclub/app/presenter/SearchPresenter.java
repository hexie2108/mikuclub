package org.mikuclub.app.presenter;

import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.configs.PostConstants;
import org.mikuclub.app.javaBeans.parameters.ParametersListPosts;
import org.mikuclub.app.models.PostModel;

/**
 * questo classe occuppa tutte le operazioni logici per recuperare i dati e aggiornare UI.
 */
public class SearchPresenter
{

        private PostModel postModel;
        private String tag;

        public SearchPresenter()
        {

                postModel = PostModel.getInstance();
                tag = GlobalConfig.TAG_SEARCH;
        }


        /*
get lista di post tramite search
 */
        public void getPostListBySearch(String query, int start, WrapperCallBack wrapperCallBack)
        {

                int page = start / GlobalConfig.NUMBER_FOR_PAGE + 1;

                ParametersListPosts parametersListPosts = new ParametersListPosts();
                parametersListPosts.setPage(page);
                parametersListPosts.setPer_page(GlobalConfig.NUMBER_FOR_PAGE);

                parametersListPosts.setSearch(query);
                parametersListPosts.setOrderby(PostConstants.OrderBy.DATE);
                parametersListPosts.setStatus(PostConstants.Status.PUBLISH);
                PostModel.getInstance().selectForList(parametersListPosts.toMap(), tag, wrapperCallBack);

        }



        /**
         * cancella la richiesta HTTP in corso
         */
        public void cancelRequest()
        {
                postModel.cancelRequest(tag);
        }


}
