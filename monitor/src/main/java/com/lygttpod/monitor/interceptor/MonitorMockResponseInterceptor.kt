package com.lygttpod.monitor.interceptor

import android.net.Uri
import com.lygttpod.monitor.mock.MockHelper
import okhttp3.Interceptor
import okhttp3.Response

class MonitorMockResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()
        val path = Uri.parse(url).path
        if (MockHelper.isMockByCustomResponse(path)) {
            return MockHelper.mockResponseBody(chain)
        }
        return chain.proceed(request)
    }
}