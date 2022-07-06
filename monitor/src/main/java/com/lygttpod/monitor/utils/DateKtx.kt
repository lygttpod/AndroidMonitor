package com.lygttpod.monitor.utils

import java.text.SimpleDateFormat
import java.util.*

val TIME_SHORT = SimpleDateFormat("HH:mm:ss SSS", Locale.CHINA)

val TIME_LONG = SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS", Locale.CHINA)

fun Date?.formatData(format: SimpleDateFormat = TIME_SHORT): String {
    return if (this == null) {
        ""
    } else format.format(this)
}
