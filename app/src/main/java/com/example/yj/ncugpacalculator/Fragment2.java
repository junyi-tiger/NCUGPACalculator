package com.example.yj.ncugpacalculator;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.BufferedWriter;

/**
 * Created by YJ on 2018/8/24.
 */

public class Fragment2 extends Fragment{
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceStates){
        super.onCreate(savedInstanceStates);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.fragment2, null);
        Button bt_about=view.findViewById(R.id.bt_about);
        bt_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity().getBaseContext(),aboutActivity.class);
                startActivity(intent);
            }
        });
        Button bt_bigimage=view.findViewById(R.id.bt_bigimage);
        bt_bigimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity().getBaseContext(),bigImageActivity.class);
                startActivity(intent);
            }
        });
    }
}
