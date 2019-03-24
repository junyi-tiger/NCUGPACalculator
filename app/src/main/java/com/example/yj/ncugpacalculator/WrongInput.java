package com.example.yj.ncugpacalculator;

/**
 * Created by YJ on 2018/8/27.
 */

public class WrongInput extends Exception{
    public String warnMess(){
        return "发生错误！请检查您的输入是否有误。";
    }
}
