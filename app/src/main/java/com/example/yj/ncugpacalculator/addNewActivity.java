package com.example.yj.ncugpacalculator;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class addNewActivity extends FragmentActivity {
    String filename=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        filename=this.getIntent().getStringExtra("filename");
        //以下代码测试参数是否正确携带
//        if(filename==null){
//            Toast.makeText(getApplicationContext(),"不带参数启动",Toast.LENGTH_LONG).show();
//        }
//        else{
//            Toast.makeText(getApplicationContext(),"带参数启动:"+filename,Toast.LENGTH_LONG).show();
//        }
        Fragment1 fragment1=new Fragment1();
        getSupportFragmentManager().beginTransaction().add(R.id.create_new_page,fragment1,null).commit();
    }
    //获得文件列表
    public String getFileName(){
        return filename;
    }
}
