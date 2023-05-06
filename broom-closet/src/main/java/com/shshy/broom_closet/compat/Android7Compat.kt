package com.shshy.broom_closet.compat

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

/**
 * Android7使用fileProvider为文件生成uri
 * @param context Context
 * @param file File
 * @return Uri
 */
fun android7CompatFileUri(context: Context, file: File): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        FileProvider.getUriForFile(context, "${context.packageName}.fileProvider", file)
    else
        Uri.fromFile(file)
}