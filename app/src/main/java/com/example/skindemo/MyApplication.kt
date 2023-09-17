package com.example.skindemo

import android.app.Application
import com.lib.skin.SkinManager

class MyApplication : Application() {
    companion object {
        private var instance: MyApplication? = null
        fun getMyApplication(): MyApplication = instance!!
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // 必须初始化 必做步骤
        SkinManager.init(this)
    }
}