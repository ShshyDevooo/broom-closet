package com.shshy.broom_closet.exts

import java.text.SimpleDateFormat
import java.util.*

fun Date?.toFormattedString(format: String, locale: Locale = Locale.getDefault()): String? {
    return if (this == null) null
    else {
        val simpleDateFormat = SimpleDateFormat(format, locale)
        simpleDateFormat.format(this)
    }
}