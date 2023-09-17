package com.example.skindemo

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * 工具类 将打包出来的资源apk直接拷贝进我们的安装目录
 * 直接编译我们当前的项目即可，不用每次都手动拷贝
 * 皮肤apk路径 测试使用  根据apk存放路径修改
 */
/**
 * 换肤apk存放的目录
 */
const val mkdirsApk = "skinapk"

val apkPath = "$mkdirsApk${File.separator}skinapk-debug.apk"

val skinApkPath = copyAssetGetFilePath(apkPath)


/**
 * 将换肤apk直接拷贝到安装目录下
 */
private fun copyAssetGetFilePath(fileName: String): String? {
    try {
        val cacheDir: File = MyApplication.getMyApplication().cacheDir
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }

        val file = File("${cacheDir}${File.separator}$mkdirsApk")
        if (!file.exists()) {
            file.mkdirs()
        }

        val outFile = File("${cacheDir}${File.separator}${fileName}")
        if (!outFile.exists()) {
            val res: Boolean = outFile.createNewFile()
            if (!res) {
                return null
            }

        }
//        else {
        //表示已经写入一次
//            if (outFile.length() > 10) {
//                return outFile.path
//            }
//        }
        val isOpen: InputStream = MyApplication.getMyApplication().assets.open(fileName)
        val fos = FileOutputStream(outFile)
        val buffer = ByteArray(1024)
        var byteCount: Int
        while (isOpen.read(buffer).also { byteCount = it } != -1) {
            fos.write(buffer, 0, byteCount)
        }
        fos.flush()
        isOpen.close()
        fos.close()
        return outFile.path
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        //TODO 资源关闭写在这里

    }
    return null
}
