package com.example.user.myads.manager;


import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.util.List;

/**
 * 管理类通用接口
 */
public class DefultManager {

    public static final int COMPLETE = 0x0001;
    public static final int DO_NEXT_COMPLETE = 0x0002;

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COMPLETE:
                    AdsManager.getManager().setViewCompleteData((List<View>) msg.obj);
//                    AdsManager.getManager().setCompleteData((List<AdsEntity>) msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    };


    protected void release() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
