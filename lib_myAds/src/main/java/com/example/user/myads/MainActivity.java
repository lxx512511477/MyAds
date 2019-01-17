package com.example.user.myads;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.myads.manager.AdsManager;
import com.facebook.ads.AdIconView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.mintegral.msdk.MIntegralConstans;
import com.mintegral.msdk.out.MtgWallHandler;
import com.mobpower.appwallad.api.AppwallAd;
import com.mobpower.appwallad.api.AppwallConfig;

import java.util.List;
import java.util.Map;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 测试广告聚合
 * <p>
 * <p>
 * Mintegral power存在应用墙
 * 暂时只有FB，admob，power存在banner
 */
public class MainActivity extends AppCompatActivity {
    Button tv_appwall_mintegral;
    Button tv_appwall_mobpower;
    TextView tv_title_mob;
    ImageView iv_src_mob;

    private BGABanner banner;

    LinearLayout fl_face;
    //        private AdLoader adLoader;
//    private NativeAds nativeAds;
    private NativeAd nativeAd;


    private LinearLayout adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        banner = findViewById(R.id.banner_content);


        AdsManager.getManager().init(this);
        AdsManager.getManager().doNextManager(AdsManager.MAX_ADS);
        AdsManager.getManager().setAdsListener(new AdsManager.AdsListener() {
            @Override
            public void onAdsViewComplete(List<View> list) {
                Log.e("lxxxxxxxx", "onAdsViewComplete===" + list.size());

                banner.setData(list);
            }

            @Override
            public void onAdsViewLess(List<View> list) {
                Log.e("lxxxxxxxx", "onAdsViewLess");
            }

        });


        //Admob banner广告源测试
//        AdmobIndex();

        tv_title_mob = findViewById(R.id.tv_title_mob);
        iv_src_mob = findViewById(R.id.iv_src_mob);

        //mobpower 广告源测试
//        nativeAds = new NativeAds(this, "1032524", 1);
//        MobpowerIndex();


        //Facebook广告源测试
        fl_face = findViewById(R.id.fl_face);
        nativeAd = new NativeAd(this, "YOUR_PLACEMENT_ID");
        FacebookIndex();

        //1.Mintegral SDK 应用墙初始化   （已确认只需要应用墙）
        tv_appwall_mintegral = findViewById(R.id.tv_appwall_mintegral);
        tv_appwall_mintegral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appWall_mintegral();
            }
        });

        //Mobpower 应用墙初始化
        tv_appwall_mobpower = findViewById(R.id.tv_appwall_mobpower);
        tv_appwall_mobpower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appWall_Mobpower();
            }
        });

    }

    private void FacebookIndex() {

        LayoutInflater inflater = LayoutInflater.from(this);
        adView  = (LinearLayout) inflater.inflate(R.layout.ad_facebook, fl_face, false);

        nativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {

                Log.e("lxxxxxxxxxx", "FacebookIndex_onError");
            }

            @Override
            public void onAdLoaded(com.facebook.ads.Ad ad) {
                Log.e("lxxxxxxxxxx", "FacebookIndex_onAdLoaded");

                nativeAd.unregisterView();
                initFacebook();
//                View render = NativeAdView.render(MainActivity.this, nativeAd, NativeAdView.Type.HEIGHT_300);
                fl_face.addView(adView);
            }

            @Override
            public void onAdClicked(com.facebook.ads.Ad ad) {

            }

            @Override
            public void onLoggingImpression(com.facebook.ads.Ad ad) {

            }

            @Override
            public void onMediaDownloaded(com.facebook.ads.Ad ad) {

            }
        });

        // Request an ad
        nativeAd.loadAd();

    }


    private void initFacebook(){
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());

        nativeAd.registerViewForInteraction(
                adView,
                nativeAdMedia);
    }


    private void MobpowerIndex() {
//        nativeAds.setListener(new com.mobpower.core.api.AdListener() {
//            @Override
//            public void onLoadError(AdError adError) {
//
//                Log.e("lxxxxxxxxxx", "onLoadError");
//            }
//
//            @Override
//            public void onAdfilled() {
//
//                Log.e("lxxxxxxxxxx", "MobpowerIndex_onAdfilled");
//                nativeAds.loadAd();
//            }
//
//            @Override
//            public void onAdLoaded(List<Ad> list) {
//                Log.e("lxxxxxxxxxx", "MobpowerIndex_onAdLoaded");
//                Ad adReg = list.get(0);
//                tv_title_mob.setText(adReg.getTitle());
//                String url = adReg.getImageUrl();
//                if (TextUtils.isEmpty(url)) {
//                    url = adReg.getIconUrl();
//                }
//                Glide.with(MainActivity.this)
//                        .load(url)
//                        .into(iv_src_mob);
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//
//            }
//
//            @Override
//            public void onAdClickStart(Ad ad) {
//
//            }
//
//            @Override
//            public void onAdClickEnd(Ad ad) {
//
//            }
//
//            @Override
//            public void installedCallback() {
//
//            }
//        });
//        nativeAds.fill();
    }

    /**
     * admob 广告初始化
     */
    private void AdmobIndex() {
//        adLoader = new AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
//                .forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
//                    @Override
//                    public void onAppInstallAdLoaded(NativeAppInstallAd appInstallAd) {
//                        // Show the app install ad.
//                    }
//                })
//                .forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
//                    @Override
//                    public void onContentAdLoaded(NativeContentAd contentAd) {
//
//                        Log.e("lxxxxxxxxxx", "onContentAdLoaded");
//                        // Show the content ad.
//                        FrameLayout frameLayout = findViewById(R.id.fl_adplaceholder);
//                        // Assumes that your ad layout is in a file call ad_unified.xml
//                        // in the res/layout folder
//                        NativeContentAdView adView = (NativeContentAdView) getLayoutInflater().inflate(R.layout.ad_content, null);
//                        // This method sets the text, images and the native ad, etc into the ad
//                        // view.
//                        populateContentAdView(contentAd, adView);
//                        frameLayout.removeAllViews();
//                        frameLayout.addView(adView);
//
//                    }
//                })
//                .withAdListener(new AdListener() {
//                    @Override
//                    public void onAdFailedToLoad(int errorCode) {
//                        Log.e("lxxxxxxxxxx", "onAdFailedToLoad=" + errorCode);
//                        // Handle the failure by logging, altering the UI, and so on.
//                    }
//                })
//                .withNativeAdOptions(new NativeAdOptions.Builder()
//                        // Methods in the NativeAdOptions.Builder class can be
//                        // used here to specify individual options settings.
//                        .build())
//                .build();
//        adLoader.loadAd(new AdRequest.Builder().build());
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

    //Mintegral应用墙 UI定义
    private void appWall_mintegral() {
        Map<String, Object> properties = MtgWallHandler.getWallProperties("61087");
        MtgWallHandler mtgHandler = new MtgWallHandler(properties, this);
        // use color resId as the appwall title
        properties.put(MIntegralConstans.PROPERTIES_WALL_TITLE_BACKGROUND_COLOR, R.color.mintegral_bg_main);
        // wall main background must be color
        properties.put(MIntegralConstans.PROPERTIES_WALL_MAIN_BACKGROUND_ID, R.color.mintegral_bg_main);
        // wall tab background must be color
        properties.put(MIntegralConstans.PROPERTIES_WALL_TAB_BACKGROUND_ID, R.color.mintegral_bg_main);
        // use bitmap or text as the appwall title
//        properties.put(MIntegralConstans.PROPERTIES_WALL_TITLE_LOGO_TEXT, "text");
        mtgHandler.startWall();
    }

    //Mobpower应用墙 UI定义
    private void appWall_Mobpower() {
        AppwallAd appwallAd = new AppwallAd(this, "1032522");
        AppwallConfig config = new AppwallConfig();
        config.setmBackRes(R.drawable.mobpower_appwall_back_bg);//返回按钮定制
        config.setmTitleBackgroundColor(R.color.mobpower_appwall_tab_bg);//appwall标题背景 定制
        config.setmTitleText(R.string.mobpower_appwall_title);//appwall标题定制
        config.setmTitleColor(R.color.mobpower_appwall_title_color);//appwall标题颜⾊色定制
        config.setmTabBackgroundColor(R.color.mobpower_appwall_tab_bg);//定制appwall tab背 景颜⾊色
        config.setmTabTextColor(R.color.mobpower_appwall_tab_bg);//定制appwall 当前tab标题颜 ⾊色
        config.setmTabTextColorNormal(android.R.color.black);//定制appwall 未选择tab标题颜⾊色
        config.setmUnderLineColor(R.color.mobpower_appwall_tab_bg);//定制appwall tab下划线 颜⾊色
        config.setmDownloadColor(R.color.mobpower_appwall_download_btn_color);//定制appwal l下载按钮颜⾊色
/** * param: * 1.android.content.res.Configuration.ORIENTATION_LANDSCAPE
 * * 2.android.content.res.Configuration.ORIENTATION_PORTRAIT **/
//        config.setmOrientation(param);//appwall orientation
        appwallAd.setConfig(config);
        appwallAd.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (adLoader != null) {
//
//        }
//        if (nativeAds != null) {
//            nativeAds.release();
//        }
//        if (nativeAd != null) {
//            nativeAd.destroy();
//        }
        AdsManager.getManager().destory();
    }
}
