package com.example.yj.ncugpacalculator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by YJ on 2018/8/27.
 */

public class GPAUtil {
    public static double CalNCUGPA(ArrayList<String> grades,ArrayList<String> points)throws WrongInput{

        double ans=0.0;
        double sum_points=0.0;
        for (int i=0;i<grades.size();i++){
            String stringPoint=points.get(i);
            String stringGrade=grades.get(i);
            if (stringPoint.equals("0"))continue;
            double point=0.0;
            double grade=0.0;
            if (stringGrade.equals("A")){
                grade=90;
            }else if(stringGrade.equals("A-")){
                grade=87;
            } else if (stringGrade.equals("B+")){
                grade=83;
            }else if (stringGrade.equals("B")){
                grade=80;
            }else if (stringGrade.equals("B-")){
                grade=77;
            } else if (stringGrade.equals("C+")){
                grade=73;
            }else if (stringGrade.equals("C")){
                grade=70;
            }else if(stringGrade.equals("C-")){
                grade=65;
            }else if (stringGrade.equals("D")){
                grade=61;
            }else if (stringGrade.equals("D")){
                grade=60;
            }else if (stringGrade.equals("D-")){
                grade=57;
            }else if (stringGrade.equals("F")){
                grade=0;
            }else if(stringGrade.equals("P")){
                grade=75;
            }else{
                grade=Double.parseDouble(stringGrade);
            }
            point=Double.parseDouble(stringPoint);

            if (point<0||grade<0||grade>100)throw new WrongInput();
            sum_points+=point;
            if (grade>=90)ans+=4.0*point;
            else if(grade>=85)ans+=3.7*point;
            else if(grade>=82)ans+=3.3*point;
            else if(grade>=78)ans+=3.0*point;
            else if(grade>=75)ans+=2.7*point;
            else if(grade>=71)ans+=2.3*point;
            else if (grade>=66)ans+=2.0*point;
            else if(grade>=62)ans+=1.7*point;
            else if (grade>=60)ans+=1.3*point;
            else if (grade>55)ans+=1.0*point;
            else ans+=0;
        }
        if (sum_points==0.0)ans=0;
        else ans=ans/sum_points;
        return ans;
    }
    public static void writeFile(String cookie,final String filename) {
        final String name=cookie.substring(0, 10);
        final String value=cookie.substring(11);
        System.out.println("name="+name+" value="+value);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection conn= Jsoup.connect("http://jwc101.ncu.edu.cn/jsxsd/kscj/cjcx_list");
                conn.cookie(name, value);
//                conn.header("Accept", "text/html, application/xhtml+xml, image/jxr, */* q=0.8");
//		        conn.header("Accept-Language","zh-CN");
//	    	    conn.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
//	        	conn.header("Accept-Encoding","gzip, deflate");
//	        	conn.header("Content-Length","77");
//	        	conn.header("Cache-Control","max-age=0");
//	        	conn.header("Connection", "Keep-Alive");
//	        	conn.header("Host","jwc101.ncu.edu.cn");
//		        conn.header("Upgrade-Insecure-Requests","1");
                try {
                    Document doc=conn.method(Connection.Method.GET).post();
//			System.out.println(doc);//获取成功！
			/*
			 * 下面开始获取成绩数据生成一个文件从而启动add_new_Activity
			 */
                    Elements elements=doc.getElementById("dataList").select("tr");
                    File file=new File("/data/data/com.example.yj.ncugpacalculator/files/data/"+filename);
                    if(!file.exists()) {//如果文件不存在。创建
                        File dir = new File("/data/data/com.example.yj.ncugpacalculator/files/data/");
                        if (!dir.exists() && !dir.isDirectory()) {//如果文件夹不存在，创建
                            dir.mkdirs();
                        }
                        BufferedWriter bfw = new BufferedWriter(new FileWriter(file,false));
                        //写入总个数
                        bfw.write(String.format("%d", elements.size()-1));
                        bfw.newLine();
                        for(int i=1;i<elements.size();i++) {//不需要第一行数据
                            Elements values=elements.get(i).children();
                            String coursename=values.get(3).text();
                            String grade=values.get(4).text();
                            String point=values.get(5).text();
                            if (coursename.isEmpty()){
                                coursename="null";//填入一个null
                            }
                            if(grade.isEmpty()){
                                grade="0";
                            }
                            if(point.isEmpty()){
                                point="0";
                            }
                            bfw.write(coursename);
                            bfw.newLine();
                            bfw.write(grade);
                            bfw.newLine();
                            bfw.write(point);
                            bfw.newLine();
//                System.out.println("课程名："+coursename+"\t\t成绩："+grade+"\t学分:"+point);//爬取成功！
                        }
                        bfw.flush();
                        bfw.close();
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
