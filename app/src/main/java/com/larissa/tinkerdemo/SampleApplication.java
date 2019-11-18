package com.larissa.tinkerdemo;

import android.app.Application;
import android.util.Log;

import com.tencent.tinker.entry.ApplicationLike;
import com.tencent.tinker.lib.service.PatchResult;
import com.tinkerpatch.sdk.TinkerPatch;
import com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike;
import com.tinkerpatch.sdk.server.callback.ConfigRequestCallback;
import com.tinkerpatch.sdk.server.callback.RollbackCallBack;
import com.tinkerpatch.sdk.tinker.callback.ResultCallBack;
import com.zhouyou.http.EasyHttp;

import java.util.HashMap;

/**
 * Created by Administrator on 2019/11/16.
 */

public class SampleApplication extends Application {
    public static final String TAG = "Tinker";
    public static String TINKER_PATH;
    @Override
    public void onCreate() {
        super.onCreate();
        EasyHttp.init(this);//默认初始化
        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        EasyHttp.getInstance()

                //可以全局统一设置全局URL
                .setBaseUrl("http://277uv67493.zicp.vip/")//设置全局URL  url只能是域名 或者域名+端口号

                // 打开该调试开关并设置TAG,不需要就不要加入该行
                // 最后的true表示是否打印内部异常，一般打开方便调试错误
                .debug("EasyHttp", true);

        // 我们可以从这里获得Tinker加载过程的信息
        ApplicationLike tinkerPatchApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();

        // 初始化TinkerPatch SDK, 更多配置可参照API章节中的,初始化SDK
             TinkerPatch.init(tinkerPatchApplicationLike)
                     //是否自动反射Library路径,无须手动加载补丁中的So文件 注意,调用在反射接口之后才能生效,你也可以使用Tinker的方式加载Library
                     .reflectPatchLibrary()
                     //向后台获取是否有补丁包更新,默认的访问间隔为3个小时，若参数为true,即每次调用都会真正的访问后台配置
                     //你也可以在用户登录或者APP启动等一些关键路径，使用fetchPatchUpdate(true)强制检查更新
                     .fetchPatchUpdate(true)
                     //设置访问后台补丁包更新配置的时间间隔,默认为3个小时
                     .setFetchPatchIntervalByHours(3)
                     //向后台获得动态配置,默认的访问间隔为3个小时
                     //若参数为true,即每次调用都会真正的访问后台配置
                     .fetchDynamicConfig(new ConfigRequestCallback() {
                         @Override public void onSuccess(HashMap<String, String> hashMap) { }
                         @Override public void onFail(Exception e) { }
                     }, true)
                     //设置访问后台动态配置的时间间隔,默认为3个小时
                     .setFetchDynamicConfigIntervalByHours(3)
                     //设置补丁合成成功后,锁屏重启程序,默认是等应用自然重启
                     .setPatchRestartOnSrceenOff(true)
                     //我们可以通过ResultCallBack设置对合成后的回调,例如弹框什么
                     .setPatchResultCallback(new ResultCallBack() {
                         @Override public void onPatchResult(PatchResult patchResult) {
                             String rawPatchFilePath = patchResult.rawPatchFilePath;
                             Log.e(TAG,rawPatchFilePath);
                             TINKER_PATH = rawPatchFilePath;
                             Log.e(TAG, "onPatchResult callback here");
                         }
                     })
                     //设置收到后台回退要求时,锁屏清除补丁,默认是等主进程重启时自动清除
                     .setPatchRollbackOnScreenOff(true)
                     //我们可以通过RollbackCallBack设置对回退时的回调
                     .setPatchRollBackCallback(new RollbackCallBack() {
                         @Override public void onPatchRollback() {
                             Log.e(TAG, "onPatchRollback callback here");
                         }
                     });
    }
}
