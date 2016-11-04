# android-navi-multiroute-display
多路径规划、导航示例
## 前述 ##
- [高德官网申请Key](http://lbs.amap.com/dev/#/).
- 阅读[参考手册](http://a.amap.com/lbs/static/unzip/Android_Map_Doc/index.html).
- [科大讯飞开放平台申请Appid](http://www.xfyun.cn/services/online_tts).
- 工程基于Android 导航 SDK、科大讯飞在线语音合成SDK实现


## 扫一扫安装##
![Screenshot]( https://github.com/amap-demo/android-navi-multiroute-display/raw/master/resource/download.png )


##多路径规划、导航示例
## 使用方法##
###1:配置搭建AndroidSDK工程###
- [Android Studio工程搭建方法](http://lbs.amap.com/api/android-sdk/guide/creat-project/android-studio-creat-project/#add-jars).
- [通过maven库引入SDK方法](http://lbsbbs.amap.com/forum.php?mod=viewthread&tid=18786).


###2:运行示例配置项###
- AndroidManifest.xml 配置高德开放平台key
```java
 <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <meta-data
            android:name="com.amap.api.v2.apikey"
            android:value="高德开放平台android key" />

        <activity android:name="com.amap.multiroute.CalculateRouteActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity android:name="com.amap.multiroute.RouteNaviActivity" />
        <activity android:name="com.amap.multiroute.StrategyChooseActivity"></activity>
    </application>
```
- string.xml 配置科大讯飞Appid
```java
 <string name="app_id">科大讯飞Appid</string>
```
- CalculateRouteActivity 设置导航起终点
```java
 private NaviLatLng endLatlng; //导航终点
 private NaviLatLng startLatlng; //导航起点
```
- CalculateRouteActivity 选择模拟\真实导航
```java
 /**
     * 开始导航
     */
    private void startNavi() {
        Intent gpsintent = new Intent(getApplicationContext(), RouteNaviActivity.class);
        gpsintent.putExtra("gps", false); // gps 为true为真实导航，为false为模拟导航
        startActivity(gpsintent);
    }
```

##效果展示##
####多路径规划
![Screenshot]( https://github.com/amap-demo/android-navi-multiroute-display/raw/master/resource/route_snapshot.png )

####驾车偏好设置
![Screenshot]( https://github.com/amap-demo/android-navi-multiroute-display/raw/master/resource/strategy_snapshot.png )

####导航
![Screenshot]( https://github.com/amap-demo/android-navi-multiroute-display/raw/master/resource/navi_snapshot.png )




