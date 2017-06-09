package com.hwz.zhbj.util;

import android.content.Context;

/**
 * Created by huwang on 2017/6/6.
 */

public class DensityUtils {
    public static int dp2px(Context paramContext, float paramFloat)
    {
        return (int)(0.5F + paramFloat * paramContext.getResources().getDisplayMetrics().density);
    }

    public static float px2dp(Context paramContext, int paramInt)
    {
        float f = paramContext.getResources().getDisplayMetrics().density;
        return paramInt / f;
    }
}
