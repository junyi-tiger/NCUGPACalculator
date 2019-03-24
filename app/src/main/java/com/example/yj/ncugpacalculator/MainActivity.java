package com.example.yj.ncugpacalculator;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
    //FragmentTabHost
    private FragmentTabHost mTabHost;
    //布局填充器
    private LayoutInflater mLayoutInflater;
    //Fragment数组界面
    private Class mFragmentArray[]={Fragment_home.class, Fragment2.class};
    //存放图片数组
    private int mImageArray[]={R.drawable.tab_home_btn,R.drawable.tab_more_btn};
    //选项卡文字
    private String mTextArray[]={"首页","更多"};
    private long lastTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView(){
        mLayoutInflater=LayoutInflater.from(this);

        //找到TabHost
        mTabHost=findViewById(android.R.id.tabhost);
        mTabHost.setup(this,getSupportFragmentManager(),R.id.realTabContent);
        //得到fragment的个数
        int count=mFragmentArray.length;
        for(int i=0;i<count;i++){
            //给每个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec= mTabHost.newTabSpec(mTextArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec,mFragmentArray[i],null);
        }
        //设置没有分割线
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
    }
    /*
    给每个Tab按钮设置图标和文字
     */
    private View getTabItemView(int index){
        View view =mLayoutInflater.inflate(R.layout.tab_item_view,null);
        ImageView imageView=view.findViewById(R.id.imageView);
        imageView.setImageResource(mImageArray[index]);
        TextView textView=view.findViewById(R.id.textView);
        textView.setText(mTextArray[index]);
        return view;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - lastTime <= 2000) {
                finish();
            }else{
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                lastTime = System.currentTimeMillis();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
