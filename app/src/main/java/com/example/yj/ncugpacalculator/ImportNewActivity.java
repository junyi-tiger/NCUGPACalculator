package com.example.yj.ncugpacalculator;

import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class ImportNewActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_new);
        webView=findViewById(R.id.webview);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient(){
            String cookie=null;
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,String url){
                if (url.equals("http://jwc101.ncu.edu.cn/jsxsd/framework/xsMain.jsp")){//请求跳转到主界面时，拦截
//                    Toast.makeText(getApplicationContext(),"获取到cookie:"+cookie,Toast.LENGTH_LONG).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent=new Intent(ImportNewActivity.this,addNewActivity.class);
                            GPAUtil.writeFile(cookie,"12333");
                            intent.putExtra("filename","12333");
                            startActivity(intent);
                            finish();
                        }
                    }).start();
                    try {
                        Toast.makeText(getApplicationContext(),"请等待数据加载完成...",Toast.LENGTH_LONG).show();
                        Thread.currentThread().sleep(1000);//阻断2秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"加载失败！请重试",Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
                return super.shouldOverrideUrlLoading(view,url);
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
                handler.proceed();//接受所有证书
            }
            @Override
            public void onPageFinished(WebView view,String url){
                if (url.equals("http://jwc101.ncu.edu.cn/jsxsd/")){
                    CookieManager cookieManager=CookieManager.getInstance();
                    cookie=cookieManager.getCookie(url);
                }
                super.onPageFinished(view,url);
            }
        });
        WebSettings webSettings=webView.getSettings();
        //开启javascrpt支持
        webSettings.setJavaScriptEnabled(true);
        //开启缩放工具
        webSettings.setSupportZoom(true);
        //设置缓存方式
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //打开本地缓存供JS调用
        webSettings.setDomStorageEnabled(true);
        //启动概述模式浏览界面，当页面宽度超过WebView显示宽度时，缩小页面适应WebView(默认false)
        webSettings.setLoadWithOverviewMode(true);
        //支持手势缩放（如webView中需要手动输入用户名、密码等，则webview必须设置支持获取手势焦点）
        webView.requestFocusFromTouch();
//                Connection connection = Jsoup.connect("http://jwc101.ncu.edu.cn/jsxsd/");
                //验证码网址： http://jwc101.ncu.edu.cn/jsxsd/verifycode.servlet
        try{
            webView.loadUrl("http://jwc101.ncu.edu.cn/jsxsd/");//加载url
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy(){
        //销毁webview，避免内在泄漏
        if (webView != null) {// 移除webView确保Detach
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null){
                parent.removeView(webView);
            }
            webView.removeAllViews();
            webView.clearHistory();
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
