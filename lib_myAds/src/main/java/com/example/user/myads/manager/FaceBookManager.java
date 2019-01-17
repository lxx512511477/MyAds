package com.example.user.myads.manager;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.myads.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdView;
import com.facebook.ads.NativeAdsManager;

import java.util.ArrayList;
import java.util.List;


/**
 * facebook 广告管理者
 * <p>
 * 负责facebook广告业务请求  以及 对数据处理
 */
public class FaceBookManager extends DefultManager {

    private NativeAdsManager nativeAds;
    private NativeAd nativeAd;

    //    private List<AdsEntity> adList = new ArrayList<>();
    private List<View> adViewList = new ArrayList<>();
    private Context context;

    private LinearLayout adView;
    private LayoutInflater inflater;

    public FaceBookManager(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setBanner(String s, final int t) {
        if (t > 1) {
            nativeAds = new NativeAdsManager(context, "YOUR_PLACEMENT_ID", t);
            nativeAds.setListener(new NativeAdsManager.Listener() {
                @Override
                public void onAdsLoaded() {
                    int count = nativeAds.getUniqueNativeAdCount();
                    while (count > 0) {
//                    AdsEntity entity = new AdsEntity();
                        count--;
                        adView = (LinearLayout) inflater.inflate(R.layout.ad_facebook, null);
                        initAdView(nativeAds.nextNativeAd());
//                        View render = NativeAdView.render(context, nativeAds.nextNativeAd(), NativeAdView.Type.HEIGHT_300);

                        adViewList.add(adView);
                    }

                    Message msg = Message.obtain();
                    msg.obj = adViewList;
                    msg.what = COMPLETE;

                    Log.e("lxxxxxxxxxx", "FaceBookManager_请求个数==" + adViewList.size());
                    if (adViewList.size() >= t) {
                        handler.sendMessage(msg);
                    } else {
                        handler.sendMessageDelayed(msg, 5000);
                    }
                }

                @Override
                public void onAdError(AdError adError) {
                    Log.e("lxxxxxx", "FaceBookManager_onAdError");
                }
            });

            // Request an ad
            nativeAds.loadAds();
        } else {
            nativeAd = new NativeAd(context, "YOUR_PLACEMENT_ID");
            nativeAd.setAdListener(new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {

                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    Log.e("lxxxxxx", "FaceBookManager_onAdError");
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    Log.e("lxxxxxxxxxx", "FaceBookManager_onAdLoaded");
                    View render = NativeAdView.render(context, nativeAd, NativeAdView.Type.HEIGHT_300);
                    adViewList.add(render);
                    Message msg = Message.obtain();
                    msg.obj = adViewList;
                    msg.what = COMPLETE;
                    handler.sendMessage(msg);
                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            });
            nativeAd.loadAd();
        }

    }

    private void initAdView(NativeAd ad) {
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(ad.getAdvertiserName());
        nativeAdBody.setText(ad.getAdBodyText());
        nativeAdSocialContext.setText(ad.getAdSocialContext());
        nativeAdCallToAction.setVisibility(ad.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(ad.getAdCallToAction());

        ad.registerViewForInteraction(
                adView,
                nativeAdMedia);

    }

    @Override
    protected void release() {
        super.release();
        if (nativeAd != null) {
            nativeAd.destroy();
        }

        adViewList.clear();
    }
}
