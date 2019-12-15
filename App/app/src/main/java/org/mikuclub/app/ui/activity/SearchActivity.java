package org.mikuclub.app.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;

import org.mikuclub.app.callBack.FragmentCallBack;
import org.mikuclub.app.delegates.PostDelegate;
import org.mikuclub.app.ui.fragments.SearchMainFragment;

import mikuclub.app.R;

/**
 * la classe Activity gestisce solo la interfaccia utente
 */
public class SearchActivity extends AppCompatActivity implements FragmentCallBack
{

        public static final int TAG = 3;

        private PostDelegate searchPresenter;
        private TextView textView;
        private SearchMainFragment searchMainFragment;


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);

                setContentView(R.layout.search_activity);

                searchPresenter = new PostDelegate(TAG);

        }

        @Override
        public void callBack(Object object)
        {
                searchMainFragment = (SearchMainFragment) getSupportFragmentManager().findFragmentById(R.id.searchFragment);
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
