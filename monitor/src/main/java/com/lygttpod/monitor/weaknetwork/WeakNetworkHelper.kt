package com.lygttpod.monitor.weaknetwork

import android.os.SystemClock
import com.lygttpod.monitor.enum.WeakNetworkType
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody


object WeakNetworkHelper {
    private var weakNetworkType = WeakNetworkType.TIME_OUT

    var isOpen = false

    //读取速度 默认 1024byte/s = 1k/s
    var responseSpeedByte: Long = 1024L

    fun setWeakType(type: WeakNetworkType) {
        weakNetworkType = type
    }

    fun configWeak(typeString: String?) {
        isOpen = typeString == "超时" || typeString == "断网" || typeString == "限速"
        when(typeString) {
            "超时" -> setWeakType(WeakNetworkType.TIME_OUT)
            "断网" -> setWeakType(WeakNetworkType.NO_NETWORK)
            "限速" -> setWeakType(WeakNetworkType.SPEED_LIMIT)
        }
    }

    fun weakNetType() = weakNetworkType

    fun mockTimeout(chain: Interceptor.Chain): Response {
        val timeOutMillis = chain.connectTimeoutMillis()
        val host = chain.request().url.host
        SystemClock.sleep(timeOutMillis.toLong())
        val response = chain.proceed(chain.request())
        val responseBody = "".toResponseBody(response.body?.contentType())
        return response.newBuilder()
            .code(400)
            .message("模拟超时：failed to connect to $host after ${timeOutMillis}ms")
            .body(responseBody)
            .build()
    }

    fun mockNoNetwork(chain: Interceptor.Chain): Response {
        val host = chain.request().url.host
        val response = chain.proceed(chain.request())
        val contentType = response.body?.contentType()
        val responseBody = "".toResponseBody(contentType)
        return response.newBuilder()
            .code(400)
            .message("模拟断网：Unable to resolve $host : No address associated with hostname")
            .body(responseBody)
            .build()
    }

    fun mockSpeedLimit(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        //大于0使用限速的body 否则使用原始body
        val responseBody = response.body
        val newResponseBody = if (responseSpeedByte > 0) SpeedLimitResponseBody(
            responseSpeedByte,
            responseBody
        ) else responseBody
        return response.newBuilder().body(newResponseBody).build()
    }

}