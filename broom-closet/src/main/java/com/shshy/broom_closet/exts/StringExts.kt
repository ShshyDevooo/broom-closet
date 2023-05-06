package com.shshy.broom_closet.exts

/**
 * @author  ShiShY
 * @Description:
 * @data  2023/5/6 14:21
 */
fun String?.emptyStandBy(prefix: String? = null, default: String? = null, suffix: String? = null): String? {
    return if (this.isNullOrEmpty()) default
    else "$prefix$this$suffix"
}