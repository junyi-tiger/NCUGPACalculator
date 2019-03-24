package com.example.yj.ncugpacalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by YJ on 2018/8/26.
 */

public class Fragment1 extends Fragment {
    LinearLayout linearLayout;//添加容器
    Button bt_add;//添加按钮
    Button bt_cal;//计算按钮
    TextView show_cal_answer;//结果显示
    Button bt_save;//保存按钮
    View view;//根试图
    AlertDialog.Builder builder;//对话框
    String receviedFileName;
    int screenWidth;
    /*
    获取控件、初始化
     */
    void InitViews(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.fragment1, null);
        //通过这个view找到对应的可以添加东西的控件
        linearLayout = view.findViewById(R.id.addItem);
        bt_add = view.findViewById(R.id.bt_addRow);
        bt_cal=view.findViewById(R.id.bt_start_calculate);
        show_cal_answer=view.findViewById(R.id.show_cal_answer);
        bt_save=view.findViewById(R.id.bt_save);
        builder = new AlertDialog.Builder(getContext());
    }
    /*
    添加监听器
     */
    void addListeners(){
        //添加课程
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LinearLayout newRow = new LinearLayout(getContext());
                newRow.setOrientation(LinearLayout.HORIZONTAL);
                AutoCompleteTextView autoCompleteTextView=new AutoCompleteTextView(newRow.getContext());
                EditText editText1 = new EditText(newRow.getContext());
                EditText editText2 = new EditText(newRow.getContext());
                Button bt_cancel=new Button(newRow.getContext());
                autoCompleteTextView.setHint("课程名");
                autoCompleteTextView.setGravity(Gravity.CENTER);
                autoCompleteTextView.setMaxWidth(screenWidth*2/5);
//                    autoCompleteTextView.setMaxHeight(60);
                autoCompleteTextView.setWidth(screenWidth*2/5);
                editText1.setHint("成绩");
                editText1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                editText1.setGravity(Gravity.CENTER);
                editText1.setWidth(screenWidth/5);
//                    editText1.setMaxHeight(60);
                editText2.setHint("学分");
                editText2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                editText2.setGravity(Gravity.CENTER);
                editText2.setMaxWidth(screenWidth/5);
//                    editText2.setMaxHeight(60);
                bt_cancel.setText("删除");
                bt_cancel.setGravity(Gravity.CENTER);
                bt_cancel.setWidth(screenWidth/5);
//                    bt_cancel.setMaxHeight(60);
                newRow.addView(autoCompleteTextView);
                newRow.addView(editText1);
                newRow.addView(editText2);
                newRow.addView(bt_cancel);
                bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        linearLayout.removeView(newRow);
                    }
                });
                linearLayout.addView(newRow);
                autoCompleteTextView.requestFocus();
            }
        });
        //为“开始计算”按钮添加处理事件
        bt_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup theRow;
                ArrayList<String> courseNames=new ArrayList<>();
                ArrayList<String> grades=new ArrayList<>();
                ArrayList<String> points=new ArrayList<>();

                for(int i=0;i<linearLayout.getChildCount();i++){
                    theRow=(ViewGroup)linearLayout.getChildAt(i);
                    String courseName=((AutoCompleteTextView)theRow.getChildAt(0)).getText().toString();
                    String stringGrade=((EditText)theRow.getChildAt(1)).getText().toString();
                    String stringPoint=((EditText)theRow.getChildAt(2)).getText().toString();
                    courseNames.add(courseName);
                    grades.add(stringGrade);
                    points.add(stringPoint);
                }
                try{
                    double ans=GPAUtil.CalNCUGPA(grades,points);
                    String ANS=String.format(Locale.US,"%.3f",ans);
                    show_cal_answer.setText("计算结果如下：\n"+ANS);
                    show_cal_answer.setVisibility(View.VISIBLE);
                    bt_save.setVisibility(View.VISIBLE);
                }catch (WrongInput e){
                    show_cal_answer.setText(e.warnMess());
                    show_cal_answer.setVisibility(View.VISIBLE);
                }
            }
        });
        //为保存按钮添加监听器
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("为此纪录设置标题：");
                final EditText title=new EditText(getContext());
                title.setHint("输入标题");
                builder.setView(title);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(),"开始保存",Toast.LENGTH_SHORT).show();
                        String str_title=title.getText().toString();
                        if (str_title.equals("")){
                            Toast.makeText(getContext(),"标题不能为空！",Toast.LENGTH_SHORT).show();
                        }else{
                            try{
                                //创建一个文件
                                File file=new File(getContext().getFilesDir().getPath()+"/data/"+str_title);
                                if(!file.exists()){//如果文件不存在。创建
                                    File dir=new File(getActivity().getFilesDir().getPath()+"/data/");
                                    if (!dir.exists()&&!dir.isDirectory()){//如果文件夹不存在，创建
                                        dir.mkdirs();
                                    }
//                                    FileOutputStream fos=getActivity().openFileOutput(str_title, Context.MODE_PRIVATE);
                                    BufferedWriter bfw=new BufferedWriter(new FileWriter(file,false));
                                    //写入总个数
                                    bfw.write(String.format("%d",linearLayout.getChildCount()));
                                    bfw.newLine();
                                    for (int j=0;j<linearLayout.getChildCount();j++){
                                        LinearLayout theRow=(LinearLayout)linearLayout.getChildAt(j);
                                        //课程名
                                        String coursename=((AutoCompleteTextView)theRow.getChildAt(0)).getText().toString();
                                        //成绩
                                        String stringGrade=((EditText)theRow.getChildAt(1)).getText().toString();
                                        //学分
                                        String stringPoint=((EditText)theRow.getChildAt(2)).getText().toString();
                                        if (coursename.isEmpty()){
                                            coursename="null";//填入一个null
                                        }
                                        if(stringGrade.isEmpty()){
                                            stringGrade="0";
                                        }
                                        if(stringPoint.isEmpty()){
                                            stringPoint="0";
                                        }
                                        bfw.write(coursename);
                                        bfw.newLine();
                                        bfw.write(stringGrade);
                                        bfw.newLine();
                                        bfw.write(stringPoint);
                                        bfw.newLine();
                                    }
                                    //保证输出缓冲区中的所有内容
                                    bfw.flush();
                                    bfw.close();
                                    Toast.makeText(getContext(), "写入完成",Toast.LENGTH_LONG).show();
                                }
                                else{//文件已经存在了
                                    Toast.makeText(getContext(),"相同标题的纪录已存在！无法完成",Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e){
                                Toast.makeText(getContext(),"写入发生错误",Toast.LENGTH_LONG).show();
                            }
                        }

                        //输入完点击确定后隐藏系统键盘
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(title.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //加载这个Fragment的布局文件
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceStates) {
        super.onCreate(savedInstanceStates);
        //获得保存的文件名
        receviedFileName=((addNewActivity)getActivity()).getFileName();
        //获取控件并初始化
        InitViews();
        //动态获取屏幕宽、高
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth=displayMetrics.widthPixels;

        //为添加课程按钮添加处理器
        addListeners();

        if (receviedFileName==null){
            //添加一个新行
            bt_add.performClick();
        }
        else{
            try{
                System.out.println("文件名："+receviedFileName);
                BufferedReader bfr=new BufferedReader(new FileReader
                        (new File(getActivity().getFilesDir()+"/data/"+receviedFileName)));
                String s_count=bfr.readLine();
                int count=Integer.parseInt(s_count);
                String s1=null;
                while((s1=bfr.readLine())!=null){
                    String s2=bfr.readLine();
                    String s3=bfr.readLine();
//                    Toast.makeText(getContext(),"s1:"+s1+" s2:"+s2+" s3:"+s3,Toast.LENGTH_LONG).show();
                    final LinearLayout newRow = new LinearLayout(linearLayout.getContext());
                    newRow.setOrientation(LinearLayout.HORIZONTAL);
                    AutoCompleteTextView autoCompleteTextView=new AutoCompleteTextView(newRow.getContext());
                    EditText editText1 = new EditText(newRow.getContext());
                    EditText editText2 = new EditText(newRow.getContext());
                    Button bt_cancel=new Button(newRow.getContext());

                    System.out.println(s1+" "+s2+" "+s3);
                    //新增：设置内容为从文件中读取的内容
                    if (s1.compareTo("null")!=0){
                        autoCompleteTextView.setText(s1);
                    }
                    editText1.setText(s2);
                    editText2.setText(s3);

                    autoCompleteTextView.setHint("课程名");
                    autoCompleteTextView.setGravity(Gravity.CENTER);
                    autoCompleteTextView.setMaxWidth(screenWidth*2/5);
//                    autoCompleteTextView.setMaxHeight(60);
                    autoCompleteTextView.setWidth(screenWidth*2/5);
                    editText1.setHint("成绩");
                    editText1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    editText1.setGravity(Gravity.CENTER);
                    editText1.setWidth(screenWidth/5);
//                    editText1.setMaxHeight(60);
                    editText2.setHint("学分");
                    editText2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    editText2.setGravity(Gravity.CENTER);
                    editText2.setWidth(screenWidth/5);
//                    editText2.setMaxHeight(60);
                    bt_cancel.setText("删除");
                    bt_cancel.setGravity(Gravity.CENTER);
                    bt_cancel.setWidth(screenWidth/5);
//                    bt_cancel.setMaxHeight(60);

                    newRow.addView(autoCompleteTextView);
                    newRow.addView(editText1);
                    newRow.addView(editText2);
                    newRow.addView(bt_cancel);
                    bt_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            linearLayout.removeView(newRow);
                        }
                    });
                    linearLayout.addView(newRow);
                    autoCompleteTextView.requestFocus();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            File file=new File(getActivity().getFilesDir()+"/data/"+receviedFileName);
            if (file.exists()&&receviedFileName.equals("12333")){
                file.delete();
            }
        }
    }
}
