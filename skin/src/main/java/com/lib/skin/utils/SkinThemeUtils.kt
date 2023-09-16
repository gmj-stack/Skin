package com.lib.skin.utils

import android.content.Context

object SkinThemeUtils {

    /**
     * 获得theme中的属性中定义的 资源id
     *
     * @param context
     * @param attrs
     * @return
     */
    @JvmStatic
    fun getResId(context: Context, attrs: IntArray): IntArray {
        val resIds = IntArray(attrs.size)
        val a = context.obtainStyledAttributes(attrs)
        for (i in attrs.indices) {
            resIds[i] = a.getResourceId(i, 0)
        }
        a.recycle()
        return resIds
    }

}