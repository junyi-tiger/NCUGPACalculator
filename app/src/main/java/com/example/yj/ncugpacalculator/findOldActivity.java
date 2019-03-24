package com.example.yj.ncugpacalculator;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class findOldActivity extends ListActivity {
    ArrayList<String> getFileNames(){
        File file=new File(getApplicationContext().getFilesDir().getPath()+"/data/");
        if (!file.exists()) {//文件夹不存在，则创建
            file.mkdirs();
        }
        if (!file.isDirectory()){
            Toast.makeText(getApplicationContext(),"不是文件夹",Toast.LENGTH_SHORT).show();
        }else{
            File[] files=file.listFiles();
            ArrayList<String> s=new ArrayList<>();
            for(File file1:files){
                s.add(file1.getName());
            }
            return s;
        }
        return null;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ArrayList<String> arrayList=getFileNames();
        final BaseAdapter adapter=new ArrayAdapter<>(this,R.layout.activity_find_old,arrayList);
        setListAdapter(adapter);
        ListView listView=getListView();
        //允许与用户输入的字符串进行匹配
        listView.setTextFilterEnabled(true);
        //设置选项监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //调用新Activity时只需要传递一个字符串代表文件名
                //code here
                Intent intent=new Intent(getBaseContext(),addNewActivity.class);
                intent.putExtra("filename",((TextView)view).getText());
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, int i, long l) {
                AlertDialog.Builder builder=new AlertDialog.Builder(findOldActivity.this);
                builder.setTitle("确定删除此纪录？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        File file=new File(getFilesDir().getPath()+"/data/"+((TextView)view).getText());
                        if(file.exists()){
                            //删除纪录、进行视图更新
                            file.delete();
                            arrayList.clear();
                            arrayList.addAll(getFileNames());
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
                return true;
            }
        });
    }
}
