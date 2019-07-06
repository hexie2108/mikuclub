package org.mikuclub.app.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.mikuclub.app.callBack.FragmentCallBack;
import org.mikuclub.app.presenter.SearchPresenter;
import org.mikuclub.app.ui.fragments.SearchMainFragment;

import mikuclub.app.R;

/**
 * la classe Activity gestisce solo la interfaccia utente
 */
public class SearchActivity extends AppCompatActivity implements FragmentCallBack
{

        private SearchPresenter searchPresenter;
        private TextView textView;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);

                setContentView(R.layout.search_activity);

                searchPresenter = new SearchPresenter();

        }

        @Override
        public void callBack(Object object){

                SearchMainFragment  searchMainFragment= (SearchMainFragment) getSupportFragmentManager().findFragmentById(R.id.searchFragment);
                searchMainFragment.setQuery((String) object);
                searchMainFragment.callBackForRecyclerView();

        }




        @Override
        protected void onStop()
        {
                super.onStop();
                //cancella tutte le richieste incorso
                //homePresenter.cancelRequest();
        }





}
