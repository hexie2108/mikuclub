package org.mikuclub.app.ui.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment
{
        private int layoutId;

        public int getLayoutId()
        {
                return layoutId;
        }

        public void setLayoutId(int layoutId)
        {
                this.layoutId = layoutId;
        }

        //creando fragment con layout id
        public static BaseFragment getInstance(int layoutId){
                BaseFragment baseFragment = new BaseFragment();
                baseFragment.setLayoutId(layoutId);
                return baseFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
                // Inflate the layout for this fragment
                return inflater.inflate(layoutId, container, false);
        }

}
