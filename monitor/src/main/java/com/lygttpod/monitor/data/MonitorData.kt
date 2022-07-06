package com.lygttpod.monitor.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "monitor")
class MonitorData : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "duration")
    var duration: Long = 0

    @ColumnInfo(name = "method")
    var method: String? = null

    @ColumnInfo(name = "url")
    var url: String? = null

    @ColumnInfo(name = "host")
    var host: String? = null

    @ColumnInfo(name = "path")
    var path: String? = null

    @ColumnInfo(name = "scheme")
    var scheme: String? = null

    @ColumnInfo(name = "protocol")
    var protocol: String? = null

    @ColumnInfo(name = "requestTime")
    var requestTime: String? = null

    @ColumnInfo(name = "requestHeaders")
    var requestHeaders: String? = null

    @ColumnInfo(name = "requestBody")
    var requestBody: String? = null

    @ColumnInfo(name = "requestContentType")
    var requestContentType: String? = null

    @ColumnInfo(name = "responseCode")
    var responseCode: Int = 0

    @ColumnInfo(name = "responseTime")
    var responseTime: String? = null

    @ColumnInfo(name = "responseHeaders")
    var responseHeaders: String? = null

    @ColumnInfo(name = "responseBody")
    var responseBody: String? = null

    @ColumnInfo(name = "responseMessage")
    var responseMessage: String? = null

    @ColumnInfo(name = "responseContentType")
    var responseContentType: String? = null

    @ColumnInfo(name = "responseContentLength")
    var responseContentLength: Long? = null

    @ColumnInfo(name = "errorMsg")
    var errorMsg: String? = null

    @ColumnInfo(name = "source")
    var source: String? = null
}
