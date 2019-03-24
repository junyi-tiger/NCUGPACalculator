package com.example.yj.ncugpacalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by YJ on 2018/8/24.
 */

public class Fragment_home extends Fragment{
    View view;
    Button button1;
    Button button2;
    Button button3;
    @Override
    public void onCreate(Bundle savedInstanceStates){
        super.onCreate(savedInstanceStates);
        LayoutInflater inflater=LayoutInflater.from(getContext());
        view=inflater.inflate(R.layout.home_page,null);
        System.out.println(getContext().getFilesDir().getPath());
        button1=view.findViewById(R.id.bt_create_new);
        button2=view.findViewById(R.id.bt_import_new);
        button3=view.findViewById(R.id.bt_find_old);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity().getBaseContext(),addNewActivity.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity().getBaseContext(),ImportNewActivity.class);
                startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity().getBaseContext(),findOldActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //加载这个Fragment的布局文件
        return view;
    }

}
