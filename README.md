# 自定义版本更新弹窗
#### 目录介绍
- 1.本库优势亮点
- 2.使用介绍
- 3.注意要点
- 4.效果展示
- 5.其他介绍



### 1.本库优势亮点
- 支持后台下载，支持断点下载。支持监听下载过程，下载成功，失败，异常，下载中，暂停等多种状态
- 用户可以设置是否支持强制更新，还支持用户设置版本更新内容，当内容过长，可以实现滚动模式
- 支持进度条显示，对话框进度条，并且下载中支持通知栏进度条展示，解决8.0通知栏不显示问题
- 由于下载apk到本地需要权限，固在lib中已经处理这个逻辑，只有当有读写权限时才会下载文件，没有权限则跳转设置页面打开权限
- 调用十分简单，相比AppUpdate，CheckVersionLib等库的特点是不用设置http的get或者post请求，只需要传入下载链接就可以
- 相比GitHub上几个主流的版本更新库，我这个lib代码量少很多，我觉得最少最精简的代码完成需要的功能就最好
- 适配 Android 7.0 FileProvider，处理了7.0以上安装apk异常问题，在lib中已经配置了fileProvider，直接使用就可以
- 使用dialogFragment替换了dialog，处理了重建后逻辑，[dialogFragment深入分析博客](https://www.jianshu.com/p/e4b213a07415)
- 下载完成后自动安装，对于错误的下载链接地址，会下载异常，也可以查看异常的日志
- 当下载完成后，再次弹窗，则会先判断本地是否已经下载，如果下载则直接提示安装
- 支持设置自定义下载文件路径，如果不设置，则直接使用lib中的路径【sd/apk/downApk目录下】
- 当apk下载失败，异常，错误等状态，支持重启下载任务。功能十分强大，已经用于正式app多时，你采用拿来主义使用即可，欢迎提出问题。
- 弹窗DialogFragment异常时调用onSaveInstanceState保存状态，重启时取出状态



### 2.使用介绍
#### 2.1 关于库导入
- 可以直接引入lib库


#### 2.2 使用说明
- 代码如下所示，就是这么简单
```
//设置自定义下载文件路径
UpdateUtils.APP_UPDATE_DOWN_APK_PATH = "apk" + File.separator + "downApk";
String  desc = getResources().getString(R.string.update_content_info);
/*
 * @param isForceUpdate             是否强制更新
 * @param desc                      更新文案
 * @param url                       下载链接
 * @param apkFileName               apk下载文件路径名称
 * @param packName                  包名
 */
UpdateFragment.showFragment(MainActivity.this,
        false,firstUrl,apkName,desc,BuildConfig.APPLICATION_ID);
```

#### 2.3 lib库中解决了代码中安装 APK文件异常问题【注意lib已经解决该问题】
- 直接调用工具类UpdateUtils.installNormal方法
- 关于在代码中安装 APK 文件，在 Android N 以后，为了安卓系统为了安全考虑，不能直接访问软件，需要使用 fileProvider 机制来访问、打开 APK 文件。里面if 语句，就是区分软件运行平台，来对 intent 设置不同的属性。

```
/**
 * 关于在代码中安装 APK 文件，在 Android N 以后，为了安卓系统为了安全考虑，不能直接访问软件
 * 需要使用 fileProvider 机制来访问、打开 APK 文件。
 * 普通安装
 * @param context                   上下文
 * @param apkPath                   path，文件路径
 * @param pathName                  你的包名
 */
public static void installNormal(Context context, String apkPath , String pathName) {
    if(apkPath==null || pathName==null){
        return;
    }
    Intent intent = new Intent(Intent.ACTION_VIEW);
    File apkFile = new File(apkPath);
    // 由于没有在Activity环境下启动Activity,设置下面的标签
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    //版本在7.0以上是不能直接通过uri访问的
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
        Uri apkUri = FileProvider.getUriForFile(context, pathName+".fileProvider", apkFile);
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
    } else {
        Uri uri = Uri.fromFile(apkFile);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
    }
    context.startActivity(intent);
}
```

- 清单文件添加代码如下所示：

```
<provider
    android:name=".VersionFileProvider"
    android:authorities="${applicationId}.provider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```

- 在res/xml下增加文件：file_paths.xml

```
<?xml version="1.0" encoding="utf-8"?>
<paths>
        <external-path
            name="external_files"
            path="." />
        <root-path
            name="root_path"
            path="." />
</paths>
```


### 3.注意要点
- 注意需要申请读写权限，如果你要使用，可以自定定义通知栏下载UI布局，可以自己设置弹窗UI。这里就不适用正式项目中的UI和图标，图标是使用别人的，请勿商用。
- 针对8.0以后已经解决了通知栏不显示问题，[Notification封装库](https://github.com/yangchong211/YCNotification)，Notification通知栏用法介绍及部分源码解析，https://blog.csdn.net/m0_37700275/article/details/78745024
- 目前针对下载中的状态，有很多种，这里我只是选用了几种主要的状态处理版本更新逻辑。关于下载更多逻辑，可以参考这个下载库：[FileDownloader
](https://github.com/lingochamp/FileDownloader)
    ```
     * 下载状态
     * START            开始下载
     * UPLOADING.       下载中
     * FINISH           下载完成，可以安装
     * ERROR            下载错误
     * PAUSED           下载暂停中，继续
    ```
- 关于断点下载逻辑。由于前项目主要是实现版本更新的逻辑，时间人力有限，因此直接使用FileDownloader库，无比强大。


### 4.效果展示
#### 4.1 版本更新流程图，正式项目的流程，仅供参考
![image](https://upload-images.jianshu.io/upload_images/4432347-e7ba321e3201564c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


#### 4.2 效果图展示
![image](https://upload-images.jianshu.io/upload_images/4432347-e1d32a7fd02832f0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-1879cbf17fbe05fd.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-3ea6614052d7e54f.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-5ac2ce1fbc538880.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-06b8bed3d839ae0f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-6ddebd88af2947b8.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



### 5.其他介绍
#### 关于其他内容介绍
![image](https://upload-images.jianshu.io/upload_images/4432347-7100c8e5a455c3ee.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


#### 版本更新说明
- v1.0.0 更新于2017年8月13日
- v1.0.1 更新于2017年12月9日
- v1.0.2 更新于2018年11月21日
    - 针对弹窗是DialogFragment，因此当宿主activity异常重启时，会导致弹窗偶发性崩溃。用commitAllowingStateLoss替换commit方法
    - 异常时调用onSaveInstanceState保存状态，重启时取出状态
    - 当下载失败，异常，错误时，点击按钮重新创建下载任务



#### 关于博客汇总链接
- 1.[技术博客汇总](https://www.jianshu.com/p/614cb839182c)
- 2.[开源项目汇总](https://blog.csdn.net/m0_37700275/article/details/80863574)
- 3.[生活博客汇总](https://blog.csdn.net/m0_37700275/article/details/79832978)
- 4.[喜马拉雅音频汇总](https://www.jianshu.com/p/f665de16d1eb)
- 5.[其他汇总](https://www.jianshu.com/p/53017c3fc75d)


#### 其他推荐
- 博客笔记大汇总【15年10月到至今】，包括Java基础及深入知识点，Android技术博客，Python学习笔记等等，还包括平时开发中遇到的bug汇总，当然也在工作之余收集了大量的面试题，长期更新维护并且修正，持续完善……开源的文件是markdown格式的！同时也开源了生活博客，从12年起，积累共计47篇[近20万字]，转载请注明出处，谢谢！
- 链接地址：https://github.com/yangchong211/YCBlogs
- 如果觉得好，可以star一下，谢谢！当然也欢迎提出建议，万事起于忽微，量变引起质变！


#### 关于LICENSE
```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

