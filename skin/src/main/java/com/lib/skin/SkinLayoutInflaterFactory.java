package com.lib.skin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatToggleButton;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

/**
 * 用来接管系统的view的生产过程
 */
public class SkinLayoutInflaterFactory implements LayoutInflater.Factory2, Observer {

    private static String[] mClassPrefixList = {"android.widget.", "android.webkit.", "android.view."};

    /**
     * 记录对应VIEW的构造函数
     */
    private static Class<?>[] mConstructorSignature = new Class[] {Context.class, AttributeSet.class};

    private static HashMap<String, Constructor<? extends View>> mConstructorMap =
        new HashMap<String, Constructor<? extends View>>();
    private final Object[] mConstructorArgs = new Object[2];

    private static LinkedList<String> resourceId = new LinkedList<>();

    /**
     * 当选择新皮肤后需要替换View与之对应的属性
     * 页面属性管理器
     */
    private SkinAttribute skinAttribute;


    public SkinLayoutInflaterFactory( ) {
        skinAttribute = new SkinAttribute();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        // 换肤就是在需要时候替换 View的属性(src、background等)
        // 所以这里创建 View,从而修改View属性
        View view = createViewSdk(context, name, attrs);
        if (null == view) {
            view = createViewFromTag(context, name, attrs);
        }

        // 在完成初始化后
        // 这就是我们加入的逻辑
        if (null != view) {
            skinAttribute.look(view, attrs);
        }
        return view;
    }

    /**
     * 自定义view
     *
     * @param context
     * @param name
     * @param attrs
     * @return
     */
    public View createViewFromTag(Context context, String name, AttributeSet attrs) {
        if ("view".equals(name)) {
            name = attrs.getAttributeValue(null, "class");
        }

        try {
            mConstructorArgs[0] = context;
            mConstructorArgs[1] = attrs;

            if (-1 == name.indexOf('.')) {
                for (int i = 0; i < mClassPrefixList.length; i++) {
                    final View view = createView(context, name, mClassPrefixList[i]);
                    if (view != null) {
                        return view;
                    }
                }
                return null;
            } else {
                return createView(context, name, null);
            }
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        } finally {
            // Don't retain references on context.
            mConstructorArgs[0] = null;
            mConstructorArgs[1] = null;
        }
    }

    /**
     * @param context
     * @param name
     * @param prefix
     * @return
     * @throws ClassNotFoundException
     * @throws InflateException
     */
    private View createView(Context context, String name, String prefix)
        throws ClassNotFoundException, InflateException {
        Constructor<? extends View> constructor = mConstructorMap.get(name);

        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                Class<? extends View> clazz =
                    context.getClassLoader().loadClass(prefix != null ? (prefix + name) : name).asSubclass(View.class);

                constructor = clazz.getConstructor(mConstructorSignature);
                mConstructorMap.put(name, constructor);
            }
            constructor.setAccessible(true);
            return constructor.newInstance(mConstructorArgs);
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        }
    }

    /**
     * 减少反射
     * @param context
     * @param name
     * @param attrs
     * @return
     */
    public View createViewSdk(@NonNull Context context, String name, @NonNull AttributeSet attrs) {
        View view = null;
        switch (name) {
        case "TextView":
        case "androidx.appcompat.widget.AppCompatTextView":
            view = createTextView(context, attrs);
            verifyNotNull(view, name);
            break;
        case "ImageView":
        case "androidx.appcompat.widget.AppCompatImageView":
            view = createImageView(context, attrs);
            verifyNotNull(view, name);
            break;
        case "Button":
            view = createButton(context, attrs);
            verifyNotNull(view, name);
            break;
        case "EditText":
            view = createEditText(context, attrs);
            verifyNotNull(view, name);
            break;
        case "Spinner":
            view = createSpinner(context, attrs);
            verifyNotNull(view, name);
            break;
        case "ImageButton":
            view = createImageButton(context, attrs);
            verifyNotNull(view, name);
            break;
        case "CheckBox":
            view = createCheckBox(context, attrs);
            verifyNotNull(view, name);
            break;
        case "RadioButton":
            view = createRadioButton(context, attrs);
            verifyNotNull(view, name);
            break;
        case "CheckedTextView":
            view = createCheckedTextView(context, attrs);
            verifyNotNull(view, name);
            break;
        case "AutoCompleteTextView":
            view = createAutoCompleteTextView(context, attrs);
            verifyNotNull(view, name);
            break;
        case "MultiAutoCompleteTextView":
            view = createMultiAutoCompleteTextView(context, attrs);
            verifyNotNull(view, name);
            break;
        case "RatingBar":
            view = createRatingBar(context, attrs);
            verifyNotNull(view, name);
            break;
        case "SeekBar":
            view = createSeekBar(context, attrs);
            verifyNotNull(view, name);
            break;
        case "ToggleButton":
            view = createToggleButton(context, attrs);
            verifyNotNull(view, name);
            break;
        }
        return view;
    }

    @NonNull
    protected AppCompatTextView createTextView(Context context, AttributeSet attrs) {
        return new AppCompatTextView(context, attrs);
    }

    @NonNull
    protected AppCompatImageView createImageView(Context context, AttributeSet attrs) {
        return new AppCompatImageView(context, attrs);
    }

    @NonNull
    protected AppCompatButton createButton(Context context, AttributeSet attrs) {
        return new AppCompatButton(context, attrs);
    }

    @NonNull
    protected AppCompatEditText createEditText(Context context, AttributeSet attrs) {
        return new AppCompatEditText(context, attrs);
    }

    @NonNull
    protected AppCompatSpinner createSpinner(Context context, AttributeSet attrs) {
        return new AppCompatSpinner(context, attrs);
    }

    @NonNull
    protected AppCompatImageButton createImageButton(Context context, AttributeSet attrs) {
        return new AppCompatImageButton(context, attrs);
    }

    @NonNull
    protected AppCompatCheckBox createCheckBox(Context context, AttributeSet attrs) {
        return new AppCompatCheckBox(context, attrs);
    }

    @NonNull
    protected AppCompatRadioButton createRadioButton(Context context, AttributeSet attrs) {
        return new AppCompatRadioButton(context, attrs);
    }

    @NonNull
    protected AppCompatCheckedTextView createCheckedTextView(Context context, AttributeSet attrs) {
        return new AppCompatCheckedTextView(context, attrs);
    }

    @NonNull
    protected AppCompatAutoCompleteTextView createAutoCompleteTextView(Context context, AttributeSet attrs) {
        return new AppCompatAutoCompleteTextView(context, attrs);
    }

    @NonNull
    protected AppCompatMultiAutoCompleteTextView createMultiAutoCompleteTextView(Context context, AttributeSet attrs) {
        return new AppCompatMultiAutoCompleteTextView(context, attrs);
    }

    @NonNull
    protected AppCompatRatingBar createRatingBar(Context context, AttributeSet attrs) {
        return new AppCompatRatingBar(context, attrs);
    }

    @NonNull
    protected AppCompatSeekBar createSeekBar(Context context, AttributeSet attrs) {
        return new AppCompatSeekBar(context, attrs);
    }

    @NonNull
    protected AppCompatToggleButton createToggleButton(Context context, AttributeSet attrs) {
        return new AppCompatToggleButton(context, attrs);
    }

    private void verifyNotNull(View view, String name) {
        if (view == null) {
            throw new IllegalStateException(
                this.getClass().getName() + " asked to inflate view for <" + name + ">, but returned null");
        }
    }

    // 如果有人发送通知，这里就会执行
    @Override
    public void update(Observable o, Object arg) {
        skinAttribute.applySkin();
    }
}
