package com.example.skindemo

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.lib.skin.SkinViewSupport
import com.lib.skin.utils.SkinResources

/**
 * 自定义view 必须使用SkinResources去加载资源
 * 必须实现SkinViewSupport接口
 * 有自定义属性的时候  我们必须保证加载的资源id必须是一样的
 */
class CustomView : AppCompatTextView, SkinViewSupport {


    constructor(context: Context, attribute: AttributeSet) : super(context, attribute) {
        val array = context.obtainStyledAttributes(attribute, R.styleable.TextView)

        val drawable = array.getDrawable(R.styleable.TextView_backimg)

        array.recycle()

        setBackgroundDrawable(drawable)


    }


    override fun applySkin() {
        setTextColor(SkinResources.instance!!.getColor(R.color.text_color))
        setBackgroundDrawable(SkinResources.instance!!.getDrawable(R.mipmap.img))
    }
}