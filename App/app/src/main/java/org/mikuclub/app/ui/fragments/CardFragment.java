package org.mikuclub.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import mikuclub.app.R;

public class CardFragment extends Fragment
{
        private static final String ARG_COUNT = "param1";
        private Integer counter;
        private int[] COLOR_MAP = {
                R.color.colorAccent, R.color.page_gray_cccc, R.color.colorPrimaryDark, R.color.defaultBackground, R.color.design_default_color_primary,
                R.color.page_black_cc, R.color.colorAccent, R.color.colorPrimaryDark, R.color.page_loop_bottom, R.color.page_white,
                R.color.page_black_cc, R.color.page_white
        };

        public CardFragment()
        {
                // Required empty public constructor
        }

        public static CardFragment newInstance(Integer counter)
        {
                CardFragment fragment = new CardFragment();
                Bundle args = new Bundle();
                args.putInt(ARG_COUNT, counter);
                fragment.setArguments(args);
                return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                if (getArguments() != null)
                {
                        counter = getArguments().getInt(ARG_COUNT);
                }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
                // Inflate the layout for this fragment
                return inflater.inflate(R.layout.test, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
                super.onViewCreated(view, savedInstanceState);
                view.setBackgroundColor(ContextCompat.getColor(getContext(), COLOR_MAP[counter]));
                TextView textViewCounter = view.findViewById(R.id.tvTitle);
                textViewCounter.setText("Fragment No " + (counter + 1));
        }

}
