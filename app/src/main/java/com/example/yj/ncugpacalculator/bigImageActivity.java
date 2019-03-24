package com.example.yj.ncugpacalculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

public class bigImageActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_big_image);
        ImageView img =findViewById(R.id.large_image );
        Toast toast = Toast.makeText(this, "点击图片即可返回",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
        img.setOnClickListener(new View.OnClickListener() { // 点击返回
            public void onClick(View paramView) {
                finish();
            }
        });
    }
}
