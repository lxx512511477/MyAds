package com.example.user.myads;

import android.app.Application;

import com.example.user.myads.manager.AdsManager;
import com.mintegral.msdk.MIntegralConstans;
import com.mintegral.msdk.MIntegralSDK;
import com.mintegral.msdk.out.MIntegralSDKFactory;

import java.util.HashMap;
import java.util.Map;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MIntegralSDK sdk = MIntegralSDKFactory.getMIntegralSDK();
        Map<String, String> map = sdk.getMTGConfigurationMap("107336", "730b561a6663d09a34e5862da596ffdc");
        sdk.init(map, this);

        Map<String, Object> preloadMap = new HashMap<String, Object>();
        preloadMap.put(MIntegralConstans.PROPERTIES_LAYOUT_TYPE, MIntegralConstans.LAYOUT_APPWALL);//设置广告形式为native，其中AppWall为LAYOUT_APPWALL
        preloadMap.put(MIntegralConstans.PROPERTIES_UNIT_ID, "61087");
        preloadMap.put(MIntegralConstans.PROPERTIES_AD_NUM, 1);
        sdk.preload(preloadMap);



        //AdMob 初始化
//        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        //facebook初始化
//        AudienceNetworkAds.initialize(this);

        //Mobpower 初始化
//        SDK.setUploadDataLevel(this, SDK.UPLOAD_DATA_ALL);
//        SDK.init(this, "1000392", "7dbe40bbfa594bc3fc4f9d6e1d4de151");


       new AdsManager.Builder()
               .initAdMob("ca-app-pub-3940256099942544~3347511713")
               .initFacebookAds()
               .initMobPower( "1000392", "7dbe40bbfa594bc3fc4f9d6e1d4de151")
               .maxAdsNum(5)
               .builder();
    }

}
