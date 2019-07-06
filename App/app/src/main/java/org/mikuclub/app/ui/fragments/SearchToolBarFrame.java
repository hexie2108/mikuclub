package org.mikuclub.app.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import org.mikuclub.app.callBack.FragmentCallBack;
import org.mikuclub.app.ui.activity.SearchActivity;

import mikuclub.app.R;

public class SearchToolBarFrame extends Fragment
{

        private Toolbar toolbar;
        private Button cancelsButton;
        private Button searchButton;
        private EditText searchQuery;
        private FragmentCallBack fragmentCallBack;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {


                // Inflate the layout for this fragment
                return inflater.inflate(R.layout.search_fragment_tool_bar, container, false);
        }


        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
                super.onViewCreated(view, savedInstanceState);

                toolbar = getView().findViewById(R.id.toolbar);


                cancelsButton = getView().findViewById(R.id.cancels_button);
                searchButton = getView().findViewById(R.id.search_button);
                searchQuery = getView().findViewById(R.id.search_query);

                //set clickListener on cancels
                cancelsButton.setOnClickListener(new View.OnClickListener()
                {
                        @Override
                        public void onClick(View v)
                        {
                                getActivity().finish();
                        }
                });

                searchButton.setOnClickListener(new View.OnClickListener()
                {
                        @Override
                        public void onClick(View v)
                        {
                                sendQuery();
                        }
                });

                searchQuery.setOnEditorActionListener(new TextView.OnEditorActionListener()
                {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
                        {
                                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                                {
                                        sendQuery();
                                }
                                return false;
                        }
                });
                //listerner to hidden keyboard when lost the foocus
                searchQuery.setOnFocusChangeListener(new View.OnFocusChangeListener()
                {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus)
                        {
                                if(!hasFocus)
                                {
                                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                }
                        }
                });


        }

        private void sendQuery(){
                String query = searchQuery.getText().toString();
                searchQuery.clearFocus();
                fragmentCallBack.callBack(query);
        }


        @Override
        public void onAttach(Context context)
        {
                super.onAttach(context);
                // Ensure that the container Activity implements the callback interface, otherwise throw an exception warning
                try
                {
                        fragmentCallBack = (FragmentCallBack) context;
                }
                catch (ClassCastException e)
                {
                        throw new ClassCastException(context.toString() + " must implement FragmentCallBack");
                }


        }

}
