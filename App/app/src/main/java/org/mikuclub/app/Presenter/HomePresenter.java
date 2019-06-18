package org.mikuclub.app.Presenter;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.mikuclub.app.callBack.WrapperCallBack;
import org.mikuclub.app.configs.GlobalConfig;
import org.mikuclub.app.models.PostModel;
import org.mikuclub.app.ui.activities.HomeActivity;
import org.mikuclub.app.utils.httpUtils.Connection;

/**
 * questo classe occuppa tutte le operazioni logici per recuperare i dati e aggiornare UI.
 */
public class HomePresenter
{
        private HomeActivity homeActivity;
        private PostModel postModel;
        private String tag = GlobalConfig.TAG_HOME;

        public HomePresenter(HomeActivity homeActivity){

                this.homeActivity = homeActivity;
                postModel = PostModel.getInstance();
                tag = GlobalConfig.TAG_HOME;
        }

        /*
        get lista di post più recente e aggiornare ui
         */
        public void getRecentlyPostList(){

                WrapperCallBack wrapperCallBack = new WrapperCallBack()
                {
                        @Override
                        public void onSuccess(Object response)
                        {

                                homeActivity.callBackGetRecentPost(response);
                        }
                };

                PostModel.getInstance().getListPost(tag, wrapperCallBack);

        }

        /**
         * cancella la richiesta HTTP in corso
         */
        public void cancelRequest(){
                postModel.cancelRequest(tag);
        }




}
