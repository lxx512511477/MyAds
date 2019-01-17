package com.example.user.myads.manager;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.myads.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

import java.util.ArrayList;
import java.util.List;


/**
 * 谷歌广告管理类
 * <p>
 * 负责管理谷歌广告请求业务
 */
public class AdmobManager extends DefultManager {

    private Context context;
    //    private List<AdsEntity> adList = new ArrayList<>();
    private List<View> adViewList = new ArrayList<>();


    private AdLoader adLoader;


    public AdmobManager(Context context) {
        this.context = context;
    }

    public void setBanner(String s, final int t) {
        adLoader = new AdLoader.Builder(context, s)
                .forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
                    @Override
                    public void onAppInstallAdLoaded(NativeAppInstallAd appInstallAd) {
                        // Show the app install ad.
                    }
                })
                .forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
                    @Override
                    public void onContentAdLoaded(NativeContentAd contentAd) {
                        handler.removeMessages(COMPLETE);
//
//                        AdsEntity entity = new AdsEntity();
//                        entity.setUrl(contentAd.getImages().get(0).getUri().toString());
//                        entity.setDec(contentAd.getHeadline().toString());

                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        NativeContentAdView adView = (NativeContentAdView) inflater.inflate(R.layout.ad_content, null);
                        populateContentAdView(contentAd, adView);

                        adViewList.add(adView);

//                        adList.add(entity);
//                        msg.obj = adList;
                        Message msg = Message.obtain();
                        msg.obj = adViewList;
                        msg.what = COMPLETE;
                        Log.e("lxxxxxxxxxx", "AdmobManager_请求个数==" + adViewList.size());
                        if (adViewList.size() >= t) {
                            handler.sendMessage(msg);
                        } else {
                            handler.sendMessageDelayed(msg, 5000);
                        }

                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        Log.e("lxxxxxxxxxx", "AdmobManager_onAdFailedToLoad=" + errorCode);
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();
        if (t > 1) {
            adLoader.loadAds(new AdRequest.Builder().build(), t);
        } else {
            adLoader.loadAd(new AdRequest.Builder().build());
        }
    }

    //广告内容填充
    private void populateContentAdView(NativeContentAd contentAd, NativeContentAdView adView) {
        TextView contentad_headline = adView.findViewById(R.id.contentad_headline);
        ImageView contentad_image = adView.findViewById(R.id.contentad_image);
        contentad_headline.setText(contentAd.getHeadline());
        contentad_image.setImageDrawable(contentAd.getImages().get(0).getDrawable());
//        List<NativeAd.Image> images = contentAd.getImages();
//        if (images != null && images.size() >0){
//            contentad_image.setImageDrawable(images.get(0).getDrawable());
//        }
    }


    public void setBanner(String s) {
        setBanner(s, AdsManager.MAX_ADS);
    }

    @Override
    protected void release() {
        super.release();
        adViewList.clear();
    }
}
