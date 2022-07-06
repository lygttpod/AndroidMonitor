package com.lygttpod.monitor.utils

import android.util.Log
import com.lygttpod.monitor.MonitorHelper
import com.lygttpod.monitor.data.PropertiesData
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.util.*

class MonitorProperties {

    companion object {
        private const val TAG = "MonitorHelper"
        private const val KEY_MONITOR_PORT = "monitor.port"
        private const val KEY_MONITOR_DB_NAME = "monitor.dbName"
        private const val KEY_WHITE_CONTENT_TYPES = "monitor.whiteContentTypes"
        private const val KEY_WHITE_HOSTS = "monitor.whiteHosts"
        private const val KEY_BLACK_HOSTS = "monitor.blackHosts"

        private const val ASSETS_FILE_NAME = "monitor.properties"
    }

    fun paramsProperties(): PropertiesData? {
        var propertiesData: PropertiesData? = null
        var inputStream: InputStream? = null
        val p = Properties()

        try {
            val context = MonitorHelper.context
            if (context == null) {
                Log.d(TAG, "初始化获取context失败")
                return propertiesData
            }
            inputStream = context.assets.open(ASSETS_FILE_NAME)
            if (inputStream != null) {
                p.load(inputStream)
                val port = p.getProperty(KEY_MONITOR_PORT)
                val dbName = p.getProperty(KEY_MONITOR_DB_NAME)
                val whiteContentTypes = p.getProperty(KEY_WHITE_CONTENT_TYPES)
                val whiteHosts = p.getProperty(KEY_WHITE_HOSTS)
                val blackHosts = p.getProperty(KEY_BLACK_HOSTS)
                propertiesData =
                    PropertiesData(port, dbName, whiteContentTypes, whiteHosts, blackHosts)
            }
        } catch (e: IOException) {
            if (e is FileNotFoundException) {
                Log.d(TAG, "not found monitor.properties")
            } else {
                e.printStackTrace()
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return propertiesData
    }
}