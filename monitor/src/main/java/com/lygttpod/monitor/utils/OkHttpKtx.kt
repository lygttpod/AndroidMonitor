package com.lygttpod.monitor.utils

import com.lygttpod.monitor.data.HttpHeader
import okhttp3.Headers
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.internal.http.StatusLine
import okio.Buffer
import java.io.EOFException
import java.net.HttpURLConnection
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets


fun Headers?.toJsonString(): String {
    return if (this != null) {
        val httpHeaders = ArrayList<HttpHeader>()
        var i = 0
        val count = this.size
        while (i < count) {
            httpHeaders.add(HttpHeader(this.name(i), this.value(i)))
            i++
        }
        GsonHelper.toJson(httpHeaders)
    } else {
        ""
    }
}

fun Response.promisesBody(): Boolean {
    // HEAD requests never yield a body regardless of the response headers.
    if (request.method == "HEAD") {
        return false
    }

    val responseCode = code
    if ((responseCode < StatusLine.HTTP_CONTINUE || responseCode >= 200) && responseCode != HttpURLConnection.HTTP_NO_CONTENT && responseCode != HttpURLConnection.HTTP_NOT_MODIFIED) {
        return true
    }

    // If the Content-Length or Transfer-Encoding headers disagree with the response code, the
    // response is malformed. For best compatibility, we honor the headers.
    if (headersContentLength() != -1L || "chunked".equals(
            header("Transfer-Encoding"),
            ignoreCase = true
        )
    ) {
        return true
    }

    return false
}

/** Returns the Content-Length as reported by the response headers. */
fun Response.headersContentLength(): Long {
    return headers["Content-Length"]?.toLongOrDefault(-1L) ?: -1L
}


fun String.toLongOrDefault(defaultValue: Long): Long {
    return try {
        toLong()
    } catch (_: NumberFormatException) {
        defaultValue
    }
}

internal fun Buffer.isProbablyUtf8(): Boolean {
    try {
        val prefix = Buffer()
        val byteCount = size.coerceAtMost(64)
        copyTo(prefix, 0, byteCount)
        for (i in 0 until 16) {
            if (prefix.exhausted()) {
                break
            }
            val codePoint = prefix.readUtf8CodePoint()
            if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                return false
            }
        }
        return true
    } catch (_: EOFException) {
        return false // Truncated UTF-8 sequence.
    }
}

fun RequestBody.readString(): String {
    var result = ""
    try {
        val buffer = Buffer()
        this.writeTo(buffer)
        val charset: Charset =
            this.contentType()?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
        if (buffer.isProbablyUtf8()) {
            result = buffer.readString(charset)
        }
    } catch (e: Exception) {

    }
    return result
}
