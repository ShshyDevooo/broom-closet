package com.shshy.broom_closet.exts

import java.text.SimpleDateFormat
import java.util.*

fun String?.emptyStandBy(prefix: String? = null, default: String? = null, suffix: String? = null): String? {
    return if (this.isNullOrEmpty()) default
    else "$prefix$this$suffix"
}

fun String?.toDate(format: String = "yyyy-MM-dd HH:mm:ss", locale: Locale = Locale.getDefault()): Date? {
    return if (this.isNullOrEmpty()) null
    else {
        val simpleDateFormat = SimpleDateFormat(format, locale)
        simpleDateFormat.parse(this)
    }
}