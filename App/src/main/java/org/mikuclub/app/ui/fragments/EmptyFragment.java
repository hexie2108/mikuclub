package org.mikuclub.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import mikuclub.app.R;

/**
 * deprecated
 * empty fragment
 */
public class EmptyFragment extends Fragment
{

        @Override
        public void onAttach(@NonNull Context context)
        {

                super.onAttach(context);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {

                // Inflate the layout for this fragment
                return inflater.inflate(R.layout.test, container, false);
        }

}
