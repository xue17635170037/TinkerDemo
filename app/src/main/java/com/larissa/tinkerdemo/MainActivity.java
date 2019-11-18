package com.larissa.tinkerdemo;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.DownloadProgressCallBack;
import com.zhouyou.http.exception.ApiException;

public class MainActivity extends AppCompatActivity {

    private Button tv_hello;
    private long mTaskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
        initView();
    }

    private void initView() {

        tv_hello = (Button) findViewById(R.id.tv_hello);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    private void download(){
        EasyHttp.downLoad("http://277uv67493.zicp.vip/index/download")
                .savePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/")
                .saveName("patch_signed_7zip.apk")
                .execute(new DownloadProgressCallBack<String>() {
                    @Override
                    public void update(long bytesRead, long contentLength, boolean done) {
                        int progress = (int) (bytesRead * 100 / contentLength);
                        Log.e("TAG",progress + "% ");

                    }

                    @Override
                    public void onStart() {
                        //开始下载
                    }

                    @Override
                    public void onComplete(String path) {
                        Log.e("TAG",path);
                        //下载完成，path：下载文件保存的完整路径
                    }

                    @Override
                    public void onError(ApiException e) {
                        //下载失败
                        Log.e("TAG",e.getMessage());
                    }
                });


    }
}
