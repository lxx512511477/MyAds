package com.example.user.myads.manager;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.myads.R;
import com.mobpower.core.api.Ad;
import com.mobpower.core.api.AdError;
import com.mobpower.nativeads.api.NativeAds;

import java.util.ArrayList;
import java.util.List;


/**
 * mobpower广告管理者
 * <p>
 * 负责对mobpwer广告业务请求  以及对数据处理
 */
public class MobpowerManager extends DefultManager {
    private Context context;

    private List<View> adViewList = new ArrayList<>();
    private NativeAds nativeAds;


    public MobpowerManager(Context context) {
        this.context = context;
    }

    public void setBanner(String s, final int t) {
        nativeAds = new NativeAds(context, s, t);
        nativeAds.setListener(new com.mobpower.core.api.AdListener() {
            @Override
            public void onLoadError(AdError adError) {
                Log.e("lxxxxxxxxxx", "MobpowerManager_onLoadError");
            }

            @Override
            public void onAdfilled() {
                Log.e("lxxxxxxxxxx", "MobpowerIndex_onAdfilled");
                nativeAds.loadAd();
            }

            @Override
            public void onAdLoaded(List<Ad> list) {
                for (Ad ad : list) {
                    adViewList.add(initView(ad));
                }

                Message msg = Message.obtain();
                msg.what=COMPLETE;
                msg.obj=adViewList;
                Log.e("lxxxxxxxxxx", "MobpowerIndex_请求个数=="+adViewList.size());
                if (adViewList.size() >= t) {
                    handler.sendMessage(msg);
                } else {
                    handler.sendMessageDelayed(msg,5000);
                }
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onAdClickStart(Ad ad) {

            }

            @Override
            public void onAdClickEnd(Ad ad) {

            }

            @Override
            public void installedCallback() {

            }
        });
        nativeAds.fill();
    }

    private View initView(Ad ad) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View adView = inflater.inflate(R.layout.view_ads_common, null);

        TextView tv_name = adView.findViewById(R.id.tv_name);
        TextView tv_dec = adView.findViewById(R.id.tv_dec);
        ImageView iv_big = adView.findViewById(R.id.iv_big);
        tv_name.setText(ad.getTitle());
        tv_dec.setText(ad.getBody());
        Glide.with(context)
                .load(ad.getImageUrl())
                .into(iv_big);

        return adView;
    }


    @Override
    protected void release() {
        super.release();
        if (nativeAds!=null){
            nativeAds.release();
        }

        adViewList.clear();
    }
}
