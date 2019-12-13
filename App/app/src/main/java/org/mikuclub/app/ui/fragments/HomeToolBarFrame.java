package org.mikuclub.app.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import org.mikuclub.app.ui.activity.SearchActivity;

import mikuclub.app.R;

public class HomeToolBarFrame extends Fragment
{

        private Toolbar toolbar;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {


                // Inflate the layout for this fragment
                return inflater.inflate(R.layout.home_fragment_tool_bar, container, false);
        }


        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
                super.onViewCreated(view, savedInstanceState);

                toolbar = getView().findViewById(R.id.toolbar);
                toolbar.inflateMenu(R.menu.home_tool_bar_menu);

                toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
                {
                        @Override
                        public boolean onMenuItemClick(MenuItem item)
                        {

                                switch (item.getItemId())
                                {
                                        case R.id.item_search:
                                                startSearchActivity();
                                                break;
                                }
                                return true;
                        }
                });


        }


        private void startSearchActivity()
        {

                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);


        }

}
