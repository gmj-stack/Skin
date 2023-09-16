package com.lib.skin;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.lib.skin.utils.SkinResources;
import com.lib.skin.utils.SkinThemeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * 这里面放了所有要换肤的view所对应的属性
 */
public class SkinAttribute {
    private static List<String> mAttributes = new ArrayList<>();

    static {
        mAttributes.add("background");
        mAttributes.add("src");
        mAttributes.add("srcCompat");
        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");
    }

    /**
     * 记录换肤需要操作的View与属性信息
     */
    private WeakHashMap<View, HashMap<String, Integer>> mSkinAttrMap = new WeakHashMap<>();

    /**
     * 记录下一个VIEW身上哪几个属性需要换肤textColor/src
     */
    public void look(View view, AttributeSet attrs) {
        // 用来存储view的属性值和属性名称
        HashMap<String, Integer> stringSkinPairHashMap = new HashMap<>();

        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            // 获得属性名  textColor/background
            String attributeName = attrs.getAttributeName(i);
            if (mAttributes.contains(attributeName)) {
                // #
                // ?722727272
                // @722727272
                String attributeValue = attrs.getAttributeValue(i);
                // 比如color 以#开头表示写死的颜色 不可用于换肤
                // 在xml布局文件中写死
                if (attributeValue.startsWith("#")) {
                    continue;
                }
                int resId;
                // 以 ？开头的表示使用 属性
                if (attributeValue.startsWith("?")) {
                    int attrId = Integer.parseInt(attributeValue.substring(1));
                    resId = SkinThemeUtils.getResId(view.getContext(), new int[] {attrId})[0];
                } else {
                    // 正常以 @ 开头
                    resId = Integer.parseInt(attributeValue.substring(1));
                }
                // 将对应属性值保存
                stringSkinPairHashMap.put(attributeName, resId);
                // 进入当前界面执行换肤操作
                loadSkin(view, attributeName, resId);
            }
        }

        if (!stringSkinPairHashMap.isEmpty() || view instanceof SkinViewSupport) {
            //  自定义view执行换肤操作
            if (view instanceof SkinViewSupport) {
                ((SkinViewSupport) view).applySkin();
            }

            // 将数据添加进集合中
            mSkinAttrMap.put(view, stringSkinPairHashMap);
        }
    }

    /**
     *   停留当前界面 执行了换肤操作 对所有的view中的所有的属性进行修改
     */
    public void applySkin() {

        Set<Map.Entry<View, HashMap<String, Integer>>> entries = mSkinAttrMap.entrySet();

        Iterator<Map.Entry<View, HashMap<String, Integer>>> iterator = entries.iterator();

        while (iterator.hasNext()) {
            Map.Entry<View, HashMap<String, Integer>> next = iterator.next();
            // 拿到view
            View view = next.getKey();
            // 遍历过程中是自定义view 执行换肤的操作
            if (view instanceof SkinViewSupport) {
                ((SkinViewSupport) view).applySkin();
            }
            HashMap<String, Integer> value = next.getValue();
            Set<Map.Entry<String, Integer>> entriesAttributes = value.entrySet();
            Iterator<Map.Entry<String, Integer>> iteratorAttributes = entriesAttributes.iterator();
            while (iteratorAttributes.hasNext()) {
                Map.Entry<String, Integer> nextAttributes = iteratorAttributes.next();
                String attributeName = nextAttributes.getKey();
                Integer resId = nextAttributes.getValue();
                loadSkin(view, attributeName, resId);
            }
        }
    }

    public void loadSkin(View view, String attributeName, Integer resId) {
        Drawable left = null;
        Drawable top = null;
        Drawable right = null;
        Drawable bottom = null;
        switch (attributeName) {
        case "background":
            Object background = SkinResources.getInstance().getBackground(resId);
            // 背景可能是 @color 也可能是 @drawable
            if (background instanceof Integer) {
                view.setBackgroundColor((int) background);
            } else {
                ViewCompat.setBackground(view, (Drawable) background);
            }
            break;
        case "src":
        case "srcCompat":
            background = SkinResources.getInstance().getBackground(resId);
            if (background instanceof Integer) {
                ((ImageView) view).setImageDrawable(new ColorDrawable((Integer) background));
            } else {
                ((ImageView) view).setImageDrawable((Drawable) background);
            }
            break;
        case "textColor":
            ((TextView) view).setTextColor(SkinResources.getInstance().getColorStateList(resId));
            break;
        case "drawableLeft":
            left = SkinResources.getInstance().getDrawable(resId);
            break;
        case "drawableTop":
            top = SkinResources.getInstance().getDrawable(resId);
            break;
        case "drawableRight":
            right = SkinResources.getInstance().getDrawable(resId);
            break;
        case "drawableBottom":
            bottom = SkinResources.getInstance().getDrawable(resId);
            break;
        default:
            break;
        }
        if (null != left || null != right || null != top || null != bottom) {
            ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        }
    }
}
