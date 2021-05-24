package com.example.a3lbotcontrol.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a3lbotcontrol.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopInformationFragment extends Fragment {


    public ShopInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop_information, container, false);
    }

}
