package org.mikuclub.app.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mikuclub.app.utils.LogUtils;

import mikuclub.app.R;

/**
 * A simple {@link Fragment} subclass.
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
