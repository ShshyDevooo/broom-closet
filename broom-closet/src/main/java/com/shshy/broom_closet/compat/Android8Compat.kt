package com.shshy.broom_closet.compat

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
import android.content.res.TypedArray
import android.os.Build
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * 修复Android8 透明或者对话框中设置orientation导致crash问题
 */
object Android8OrientationFixer {
    /**
     * 判断activity是否是透明主题或者是否是对话框
     * @receiver Context
     * @return Boolean
     */
    private fun Context.isTranslucentOrFloating(): Boolean {
        var isTranslucentOrFloating = false
        try {
            val styleableRes = Class.forName("com.android.internal.R\$styleable").getField("Window")[null] as IntArray
            val ta: TypedArray = obtainStyledAttributes(styleableRes)
            val m: Method = ActivityInfo::class.java.getMethod("isTranslucentOrFloating", TypedArray::class.java)
            m.isAccessible = true
            isTranslucentOrFloating = m.invoke(null, ta) as Boolean
            m.isAccessible = false
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return isTranslucentOrFloating
    }

    /**
     * 判断是否需要修复设置方向，重写activity setRequestedOrientation方法，如果满足该条件直接返回
     * @receiver Activity
     * @return Boolean
     */
    fun Activity.shouldFixOrientation(): Boolean {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()
    }

    /**
     * 在super.onCreate前调用此方法
     * @receiver Activity
     */
    fun Activity.fixOrientation() {
        if (shouldFixOrientation())
            try {
                val field: Field = Activity::class.java.getDeclaredField("mActivityInfo")
                field.isAccessible = true
                (field.get(this) as? ActivityInfo)?.screenOrientation = SCREEN_ORIENTATION_UNSPECIFIED
                field.isAccessible = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }
}

object Android8NotificationCompat {
    fun createChannel(context: Context, channelName: String, channelId: String, importance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun getBasicNotificationBuilder(context: Context, title: String?, content: String?, channelName: String = "default", channelId: String = "default", importance: Int = 3): Notification.Builder {
        createChannel(context, channelName, channelId, importance)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, channelId)
                .setContentText(content)
                .setContentTitle(title)
                .setWhen(System.currentTimeMillis())
        } else {
            Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
        }
    }
}