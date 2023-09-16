package com.lib.skin.utils

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.text.TextUtils

class SkinResources private constructor(context: Context) {
    private var mSkinPkgName: String? = null
    private var isDefaultSkin = true

    // app原始的resource
    private val mAppResources: Resources

    // 皮肤包的resource
    private var mSkinResources: Resources? = null

    init {
        mAppResources = context.resources
    }

    fun reset() {
        mSkinResources = null
        mSkinPkgName = ""
        isDefaultSkin = true
    }

    fun applySkin(resources: Resources?, pkgName: String?) {
        mSkinResources = resources
        mSkinPkgName = pkgName
        //是否使用默认皮肤
        isDefaultSkin = TextUtils.isEmpty(pkgName) || resources == null
    }

    /**
     * 1.通过原始app中的resId(R.color.XX)获取到自己的 名字
     * 2.根据名字和类型获取皮肤包中的ID
     */
    private fun getIdentifier(resId: Int): Int {
        if (isDefaultSkin) {
            return resId
        }
        val resName = mAppResources.getResourceEntryName(resId)
        val resType = mAppResources.getResourceTypeName(resId)
        return mSkinResources!!.getIdentifier(resName, resType, mSkinPkgName)
    }

    /**
     * 输入主APP的ID，到皮肤APK文件中去找到对应ID的颜色值
     * @param resId
     * @return
     */
    fun getColor(resId: Int): Int {
        if (isDefaultSkin) {
            return mAppResources.getColor(resId)
        }
        val skinId = getIdentifier(resId)
        return if (skinId == 0) {
            mAppResources.getColor(resId)
        } else mSkinResources!!.getColor(skinId)
    }

    /**
     * 字体大小
     */
    fun getSize(dimens: Int): Float {
        if (isDefaultSkin) {
            return mAppResources.getDimension(dimens)
        }
        val skinId = getIdentifier(dimens)
        return if (skinId == 0) {
            mAppResources.getDimension(dimens)
        } else mSkinResources!!.getDimension(skinId)

    }

    fun getColorStateList(resId: Int): ColorStateList {
        if (isDefaultSkin) {
            return mAppResources.getColorStateList(resId)
        }
        val skinId = getIdentifier(resId)
        return if (skinId == 0) {
            mAppResources.getColorStateList(resId)
        } else mSkinResources!!.getColorStateList(skinId)
    }

    fun getDrawable(resId: Int): Drawable {
        if (isDefaultSkin) {
            return mAppResources.getDrawable(resId,null)
        }
        //通过 app的resource 获取id 对应的 资源名 与 资源类型
        //找到 皮肤包 匹配 的 资源名资源类型 的 皮肤包的 资源 ID
        val skinId = getIdentifier(resId)
        return if (skinId == 0) {
            mAppResources.getDrawable(resId,null)
        } else mSkinResources!!.getDrawable(skinId,null)
    }

    /**
     * 可能是Color 也可能是drawable
     *
     * @return
     */
    fun getBackground(resId: Int): Any {
        val resourceTypeName = mAppResources.getResourceTypeName(resId)
        return if ("color" == resourceTypeName) {
            getColor(resId)
        } else {
            // drawable
            getDrawable(resId)
        }
    }

    companion object {
        //单例
        @JvmStatic
        @Volatile
        var instance: SkinResources? = null
            private set

        @JvmStatic
        fun init(context: Context) {
            if (instance == null) {
                synchronized(SkinResources::class.java) {
                    if (instance == null) {
                        instance = SkinResources(context)
                    }
                }
            }
        }
    }
}