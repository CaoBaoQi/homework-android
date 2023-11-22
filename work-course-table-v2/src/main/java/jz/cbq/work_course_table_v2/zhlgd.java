package jz.cbq.work_course_table_v2;
/**
 * 智慧理工大webview
 * @version 1.1.1
 */

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class zhlgd extends MainActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhlgd);
        WebView zhlgdwebview = findViewById(R.id.zhlgd_webview);//在webview中显示智慧理工大
        zhlgdwebview.getSettings().setJavaScriptEnabled(true);
        zhlgdwebview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
                super.shouldOverrideUrlLoading(view, request);
                if(request.getUrl().toString().startsWith("http:")||request.getUrl().toString().startsWith("http:s")){
                    return false;
                }
                Intent webviewintent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                startActivity(webviewintent);
                return true;
            }
        });
        zhlgdwebview.loadUrl("http://zhlgd.whut.edu.cn/tpass/login?service=http%3A%2F%2Fzhlgd.whut.edu.cn%2Ftp_up%2F");
    }
}
