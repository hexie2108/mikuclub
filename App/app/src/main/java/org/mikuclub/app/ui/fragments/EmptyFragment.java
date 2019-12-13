package org.mikuclub.app.ui.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mikuclub.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmptyFragment extends Fragment
{


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
                // Inflate the layout for this fragment
                return inflater.inflate(R.layout.empty_view, container, false);
        }

}
