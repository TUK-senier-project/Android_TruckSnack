package com.example.icontest2

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class KakaoInit : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, getString(R.string.kakao_native_key))
    }
}