package com.example.skindemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.skindemo.databinding.ActivityMainBinding
import com.lib.skin.SkinManager
import com.lib.skin.utils.SkinResources

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)


        mainBinding.tvSkin.setOnClickListener {
            // 直接加载皮肤包的路径，达到换肤效果
            SkinManager.instance?.loadSkin(skinApkPath)
        }

        mainBinding.tvSkinReset.setOnClickListener {
            // 路径为空的时候,就是重置
            SkinManager.instance?.loadSkin("")
        }
    }
}