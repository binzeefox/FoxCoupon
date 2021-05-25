package com.binzeefox.foxdevframe_kotlin.utils.device.resources

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.binzeefox.foxdevframe_kotlin.FoxCore
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 * Uri工具
 *
 * @author tong.xw
 * 2021/01/28 16:01
 */
object UriUtil {

    ///////////////////////////////////////////////////////////////////////////
    // Uri拓展方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 通过Uri获取输出流
     */
    val Uri.outputStream: OutputStream?
        get() = FoxCore.appContext.contentResolver.openOutputStream(this)

    /**
     * 通过Uri获取输入流
     */
    val Uri.inputStream: InputStream?
        get() = FoxCore.appContext.contentResolver.openInputStream(this)

    /**
     * 获取文件类型
     */
    val Uri.mimeType: String?
        get() = FoxCore.appContext.contentResolver.getType(this)

    /**
     * 通过Uri获取文件路径
     */
    val Uri.filePath: String?
        get() {
            return when {
                scheme == null -> this.path // 前缀为空，直接获取路径
                ContentResolver.SCHEME_FILE == scheme -> this.path  //文件前缀
                ContentResolver.SCHEME_CONTENT == scheme -> { //Content前缀
                    var tempStr: String? = null
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        tempStr = convertImageUri(this)
                    if (tempStr.isNullOrEmpty())
                        tempStr = convertContentUri(this)
                    tempStr
                }
                else -> null
            }
        }

    /**
     * 指定文件类别开启文件
     */
    fun Uri.openFile() {
        Intent(Intent.ACTION_VIEW).let {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            it.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)   //临时授权
            it.setDataAndType(this, mimeType)
            val resolveInfoList = FoxCore.packageManager
                    .queryIntentActivities(it, PackageManager.MATCH_DEFAULT_ONLY)
            for (info in resolveInfoList) {
                val packageName = info.activityInfo.packageName
                FoxCore.appContext.grantUriPermission(packageName, this
                        , Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }

            FoxCore.appContext.startActivity(it)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 文件拓展方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 文件获取Uri
     */
    fun File.toUri(authority: String = "${FoxCore.packageName}.authority"): Uri =
            FileProvider.getUriForFile(FoxCore.appContext, authority, this)

    /**
     * 文件获取图片Uri
     */
    val File.toImageContentUri: Uri?
        get() {
            val cursor = FoxCore.appContext.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Images.Media._ID),
                    MediaStore.Images.Media.DATA + "=?", arrayOf(path), null
            )?.also {
                // 已经存在于Provider，则用id生成Uri
                if (!it.moveToFirst()) return@also
                val id = it.getInt(it.getColumnIndex(MediaStore.MediaColumns._ID))
                val baseUri = Uri.parse("content://media/external/images/media")
                it.close()
                return Uri.withAppendedPath(baseUri, "$id")
            }
            return if (exists()) {
                // 图片文件存在
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, path)
                FoxCore.appContext.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else null //文件不存在
        }

    ///////////////////////////////////////////////////////////////////////////
    // 私有方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 处理图片Uri
     *
     * @return 文件路径
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun convertImageUri(uri: Uri): String? {
        val cursor = FoxCore.appContext.contentResolver.query(uri, arrayOf(MediaStore.Images.ImageColumns.DATA), null, null)
        cursor?: return null
        if (!cursor.moveToFirst()) return null
        val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        if (index > -1) {
            cursor.close()
            return cursor.getString(index)
        }
        cursor.close()
        return null
    }

    /**
     * 处理ContentUri
     */
    private fun convertContentUri(uri: Uri): String? {
        val packs = FoxCore.packageManager.getInstalledPackages(PackageManager.GET_PROVIDERS)   //所有包
        val fileProviderClassName = FileProvider::class.java.name
        for (pack in packs) {
            val providers = pack.providers
            providers ?: continue
            for (provider in providers) {
                //遍历Providers找到相同授权的Provider

                //遍历Providers找到相同授权的Provider
                if (!TextUtils.equals(uri.authority, provider.authority)) continue
                if (provider.name.equals(fileProviderClassName, ignoreCase = true)) {
                    val fileProviderClass = FileProvider::class.java
                    val getPathStrategy = fileProviderClass.getDeclaredMethod("getPathStrategy", Context::class.java, String::class.java)
                    getPathStrategy.isAccessible = true
                    val invoke = getPathStrategy
                            .invoke(null, FoxCore.appContext, uri.authority)
                    if (invoke != null) {
                        val pathStrategyStringClass = FileProvider::class.java.name + "\$PathStrategy"
                        val pathStrategy = Class.forName(pathStrategyStringClass)
                        val getFileForUri = pathStrategy.getDeclaredMethod("getFileForUri", Uri::class.java)
                        getFileForUri.isAccessible = true
                        val invoke1 = getFileForUri.invoke(invoke, uri)
                        if (invoke1 is File) {
                            return invoke1.absolutePath
                        }
                    }
                    break
                }
                break
            }
        }
        return null
    }
}