package com.lygttpod.monitor.utils

import com.lygttpod.monitor.MonitorHelper
import java.util.regex.Matcher
import java.util.regex.Pattern


fun String?.isWhiteContentType(): Boolean {
    val whiteContentTypes = MonitorHelper.whiteContentTypes
    val intercept =
        whiteContentTypes.isNullOrBlank() || !this.isNullOrEmpty() && whiteContentTypes.contains(
            this.split(";")[0]
        )
    println("${MonitorHelper.TAG}---->whiteContentTypes = $whiteContentTypes 当前ContentType = $this   是否在白名单:$intercept")
    return intercept
}

fun String?.isWhiteHosts(): Boolean {
    val whiteHosts = MonitorHelper.whiteHosts
    val intercept = this.isNullOrBlank() || whiteHosts.isNullOrBlank() || whiteHosts.contains(this)
    println("${MonitorHelper.TAG}---->whiteHosts = $whiteHosts  当前host= $this   是否在白名单:$intercept")
    return intercept
}

fun String?.isBlackHosts(): Boolean {
    val blackHosts = MonitorHelper.blackHosts
    if (this.isNullOrBlank()) return false
    val intercept = !blackHosts.isNullOrBlank() && blackHosts.contains(this)
    println("${MonitorHelper.TAG}---->blackHosts = $blackHosts  当前host= $this   是否在黑名单:$intercept")
    return intercept
}

fun String?.isSkipInterceptByHost(): Boolean {
    return !this.isWhiteHosts() && this.isBlackHosts()
}


val IP_ADDRESS_PATTERN: Pattern =
    Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})")

fun String?.isIpAddress(): Boolean {
    if (this.isNullOrBlank()) return false
    val matcher: Matcher = IP_ADDRESS_PATTERN.matcher(this)
    if (!matcher.matches()) return false
    for (i in 1..matcher.groupCount()) {
        try {
            val group = matcher.group(i) ?: return false
            if (group.toInt() > 255) {
                return false
            }
        } catch (e: Exception) {
            return false
        }
    }
    return true
}