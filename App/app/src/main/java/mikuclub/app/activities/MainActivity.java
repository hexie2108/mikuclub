package mikuclub.app.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import mikuclub.app.R;
import mikuclub.app.utils.ImageFileCache;

public class MainActivity extends AppCompatActivity
{



        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);


        }

        public void onClickHandler(View view){
                ImageLoader imageLoader = new ImageLoader(Volley.newRequestQueue(MainActivity.this), new ImageFileCache());
                NetworkImageView networkImageView = findViewById(R.id.img1);
                networkImageView.setDefaultImageResId(R.drawable.a);
                networkImageView.setErrorImageResId(R.drawable.c);
                networkImageView.setImageUrl( "https://static.mikuclub.org/wp-content/uploads/2019/06/20190603081513-500x280.jpg", imageLoader);
        }


}
