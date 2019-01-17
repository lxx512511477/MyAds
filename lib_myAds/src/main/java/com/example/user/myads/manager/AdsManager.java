package com.example.user.myads.manager;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.mobpower.core.api.SDK;

import java.util.ArrayList;
import java.util.List;

/**
 * 广告总管理类
 * <p>
 * <p>
 * 调度所有单体广告管理类
 * <p>
 * <p>
 * todo
 * 目前实现基本效果，但存在明显问题  就是单个广告平台请求如果存在队列请求中某个回调慢或者失败 无法做出优化调度
 * 理想情况为 任何广告平台存在队列请求中单个请求失败往后顺延到下个平台进行请求
 */
public class AdsManager {

    private static final int ADMOB = 1;
    private static final int FACEBOOK = 2;
    private static final int MOBPOWER = 3;

    private static AdsManager manager;
    //    private Context mContext;
    //admob初始化参数
    private static String ADMOB_KEY;
    //mobpower初始化参数
    private static String MOBPOWER_KEY;
    private static String MOBPOWER_ID;
    //是否初始化facebook
    private static boolean hasFacebook;

    private List<Object> sortList;//根据排序的manager列表

    //展示广告最大值
    public static int MAX_ADS = 3;

    private AdsListener listener;

    //最终广告位数据
//    public List<AdsEntity> adsList = new ArrayList<>();
    public List<View> adsViewList = new ArrayList<>();

    private Object currentManager;//当前正在运作的manager

    private static AdmobManager admobManager;
    private static MobpowerManager mobpowerManager;
    private static FaceBookManager faceBookManager;

    public static AdsManager getManager() {
        return manager;
    }

    private AdsManager() {
    }

    private void initialize(Context mContext) {
        if (!TextUtils.isEmpty(ADMOB_KEY)) {
            MobileAds.initialize(mContext, ADMOB_KEY);
            admobManager = new AdmobManager(mContext);
        }

        if (hasFacebook) {
            AudienceNetworkAds.initialize(mContext);
            faceBookManager = new FaceBookManager(mContext);
        }

        if (!TextUtils.isEmpty(MOBPOWER_ID) && !TextUtils.isEmpty(MOBPOWER_KEY)) {
            SDK.setUploadDataLevel(mContext, SDK.UPLOAD_DATA_ALL);
            SDK.init(mContext, MOBPOWER_ID, MOBPOWER_KEY);
            mobpowerManager = new MobpowerManager(mContext);
        }
    }

    //不直接给manager拿application 拿当前activity
    public void init(Activity act) {
        //各平台初始化
        initialize(act);
        sortManager(new ArrayList<Integer>());
    }

    //请求单个广告后台完成
//    public void setCompleteData(List<AdsEntity> list) {
//        adsList = list;
//        if (list.size() < MAX_ADS) {
//            int i = MAX_ADS;
//            doNextManager(i - list.size());
//        } else {
//            if (listener != null) {
//                listener.onAdsComplete(list);
//            }
//        }
//    }

    public void setViewCompleteData(List<View> list) {
        adsViewList.addAll(list);
        Log.e("lxxxxxxxxxx", "已完成" + adsViewList.size() + "个广告");
        if (adsViewList.size() < MAX_ADS) {
            int i = MAX_ADS;
            doNextManager(i - list.size());
        } else {
            if (listener != null) {
                listener.onAdsViewComplete(adsViewList);
            }
        }
    }

    //广告数量不足，继续请求下一个广告平台
    public void doNextManager(int t) {
        if (sortList.size() > 0) {
            currentManager = sortList.get(0);
        } else {
            if (listener != null) {
//                listener.onAdsLess(adsList);
                listener.onAdsViewLess(adsViewList);
            }
            return;
        }

        if (currentManager instanceof AdmobManager) {

            Log.e("lxxxxxxxxxx", "当前选中平台==AdmobManager");
            getAdmobManager().setBanner("ca-app-pub-3940256099942544/2247696110", t);
            sortList.remove(0);
        } else if (currentManager instanceof FaceBookManager) {
            Log.e("lxxxxxxxxxx", "当前选中平台==FaceBookManager");
            getFaceBookManager().setBanner("", t);
            sortList.remove(0);
        } else if (currentManager instanceof MobpowerManager) {
            Log.e("lxxxxxxxxxx", "当前选中平台==MobpowerManager");
            getMobpowerManager().setBanner("1032524", t);
            sortList.remove(0);
        }

    }

    public interface AdsListener {

//        void onAdsComplete(List<AdsEntity> list);
//        void onAdsLess(List<AdsEntity> list);

        void onAdsViewComplete(List<View> list);

        void onAdsViewLess(List<View> list);

    }

    public void setAdsListener(AdsListener listener) {
        this.listener = listener;
    }

    public void sortManager(List<Integer> list) {
        sortList = new ArrayList<>();
        if (list == null || list.size() == 0) {
//            sortList.add(admobManager);
            sortList.add(faceBookManager);
            sortList.add(mobpowerManager);
        } else {
            for (int t : list) {

                if (t == 1) {
                    sortList.add(admobManager);
                } else if (t == 2) {
                    sortList.add(faceBookManager);
                } else if (t == 3) {
                    sortList.add(mobpowerManager);
                }
            }
        }

    }

    public void destory() {
        sortList.clear();
        adsViewList.clear();
        admobManager.release();
        mobpowerManager.release();
        faceBookManager.release();
    }

    public AdmobManager getAdmobManager() {
        return admobManager;
    }

    public MobpowerManager getMobpowerManager() {
        return mobpowerManager;
    }

    public FaceBookManager getFaceBookManager() {
        return faceBookManager;
    }

    /***尝试构造器**/
    public static class Builder {

        public Builder initFacebookAds() {
            hasFacebook = true;
            return this;
        }

        public Builder initAdMob(String admobkey) {
            ADMOB_KEY = admobkey;
            return this;
        }

        public Builder maxAdsNum(int t) {
            MAX_ADS = t;
            return this;
        }


        public Builder initMobPower(String id, String mobpower) {
            MOBPOWER_ID = id;
            MOBPOWER_KEY = mobpower;
            return this;
        }

        public Builder() {
        }

        public AdsManager builder() {
            manager = new AdsManager();
            return manager;
        }
    }
}
