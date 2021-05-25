package com.binzeefox.foxdevframe_kotlin.utils.device

import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.binzeefox.foxdevframe_kotlin.FoxCore
import com.binzeefox.foxdevframe_kotlin.utils.LogUtil

/**
 * 捷径工具类
 *
 * 长按图标后显示的捷径工具类，动态生成的捷径也可以一直存在。
 * 无需静态配置，当然静态配置好也可以
 *
 * @author tong.xw
 * 2021/01/28 11:20
 */
@RequiresApi(api = Build.VERSION_CODES.N_MR1)
object ShortcutUtil {

    // 管理器
    private val shortcutManager: ShortcutManager?
        get() = FoxCore.appContext.getSystemService(ShortcutManager::class.java)

    // 动态快捷方式列表
    val dynamicShortcuts: List<ShortcutInfo>
        get() = shortcutManager?.dynamicShortcuts ?: emptyList()

    /**
     * 添加动态快捷方式列表
     */
    fun addDynamicShortcuts(infoList: List<ShortcutInfo>): Boolean {
        if (dynamicShortcuts.size + infoList.size > shortcutManager?.maxShortcutCountPerActivity?:0) {
            // 快捷方式容量满了
            LogUtil("ShortcutUtil").setMessage("setDynamicShortcut: 无法添加快捷方式，已达上限").e()
            return false
        }
        shortcutManager?.addDynamicShortcuts(infoList)
        return true
    }

    /**
     * 设置动态快捷方式列表
     */
    fun setDynamicShortcuts(infoList: List<ShortcutInfo>): Boolean {
        if (dynamicShortcuts.size + infoList.size > shortcutManager?.maxShortcutCountPerActivity?:0) {
            // 快捷方式容量满了
            LogUtil("ShortcutUtil").setMessage("setDynamicShortcut: 无法添加快捷方式，已达上限").e()
            return false
        }
        shortcutManager?.dynamicShortcuts = infoList
        return true
    }

    /**
     * 禁用快捷方式
     *
     * @param items   被禁用的快捷方式id
     * @param message 点击被禁用按钮的提示
     */
    fun disableShortcuts(items: List<String>, message: CharSequence) = shortcutManager?.disableShortcuts(items, message)
    fun disableShortcuts(items: List<String>) = shortcutManager?.disableShortcuts(items)

    /**
     * 启用快捷方式
     */
    fun enableShortcuts(items: List<String>) = shortcutManager?.enableShortcuts(items)

    /**
     * 移除所有快捷方式
     */
    fun removeAllShortcuts() = shortcutManager?.removeAllDynamicShortcuts()

    /**
     * 更新
     */
    fun updateShortcuts(items: List<ShortcutInfo>) = shortcutManager?.updateShortcuts(items)

    /**
     * 获取指定ID的快捷方式
     */
    fun getShortcut(id: String): ShortcutInfo? {
        if (id.isEmpty()) return null
        for (info in dynamicShortcuts)
            if (id == info.id) return info
        return null
    }
}