package org.mikuclub.app.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.mikuclub.app.Presenter.HomePresenter;
import org.mikuclub.app.javaBeans.Post;
import org.mikuclub.app.javaBeans.User;

import java.util.ArrayList;

import mikuclub.app.R;

/**
 * la classe Activity gestisce solo la interfaccia utente
 */
public class HomeActivity extends AppCompatActivity
{

        private HomePresenter homePresenter;
        private TextView textView;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                homePresenter= new HomePresenter(this);
                textView = findViewById(R.id.text);




        }

        @Override
        protected void onStop()
        {
                super.onStop();
                //cancella tutte le richieste incorso
                homePresenter.cancelRequest();
        }

        public void onClickHandleGetRecentPost(View view){

                homePresenter.getRecentlyPostList();
        }

        public void callBackGetRecentPost(Object response){

                ArrayList<Post> posts = (ArrayList) response;



        }

        /*
        public void onClickHandlerOnImage(View view){
                ImageLoader imageLoader = new ImageLoader(Volley.newRequestQueue(HomeActivity.this), new ImageFileCache());
                NetworkImageView networkImageView = findViewById(R.id.img1);
                networkImageView.setDefaultImageResId(R.drawable.a);
                networkImageView.setErrorImageResId(R.drawable.c);
                networkImageView.setImageUrl( "https://static.mikuclub.org/wp-content/uploads/2019/06/20190603081513-500x280.jpg", imageLoader);
        }*/




}
