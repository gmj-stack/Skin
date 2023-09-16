package com.lib.skin

import android.content.Context
import android.content.SharedPreferences

/**
 * 记录当前使用的皮肤
 */
class SkinPreference private constructor(context: Context) {
    private val mPref: SharedPreferences

    init {
        mPref = context.getSharedPreferences(SKIN_SHARED, Context.MODE_PRIVATE)
    }

    /**
     * 还原默认皮肤记录
     */
    fun reset() {
        mPref.edit().remove(KEY_SKIN_PATH).apply()
    }

    var skin: String?
        get() = mPref.getString(KEY_SKIN_PATH, null)
        set(skinPath) {
            mPref.edit().putString(KEY_SKIN_PATH, skinPath).apply()
        }

    companion object {
        private const val SKIN_SHARED = "skins"
        private const val KEY_SKIN_PATH = "skin-path"

        @JvmStatic
        @Volatile
        var instance: SkinPreference? = null
            private set

        @JvmStatic
        fun init(context: Context) {
            if (instance == null) {
                synchronized(SkinPreference::class.java) {
                    if (instance == null) {
                        instance = SkinPreference(context.applicationContext)
                    }
                }
            }
        }
    }
}