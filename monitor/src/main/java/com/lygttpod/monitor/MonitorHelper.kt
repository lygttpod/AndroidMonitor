package com.lygttpod.monitor

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.android.local.service.core.ALSHelper
import com.android.local.service.core.data.ServiceConfig
import com.lygttpod.monitor.data.MonitorData
import com.lygttpod.monitor.data.SpValueInfo
import com.lygttpod.monitor.enum.SPValueType
import com.lygttpod.monitor.interceptor.MonitorInterceptor
import com.lygttpod.monitor.interceptor.MonitorMockInterceptor
import com.lygttpod.monitor.interceptor.MonitorMockResponseInterceptor
import com.lygttpod.monitor.interceptor.MonitorWeakNetworkInterceptor
import com.lygttpod.monitor.room.MonitorDao
import com.lygttpod.monitor.room.MonitorDatabase
import com.lygttpod.monitor.service.MonitorService
import com.lygttpod.monitor.utils.MonitorProperties
import com.lygttpod.monitor.utils.SPUtils
import com.lygttpod.monitor.utils.defaultContentTypes
import com.lygttpod.monitor.utils.lastUpdateDataId
import com.google.gson.Gson
import java.io.File
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.concurrent.thread


@SuppressLint("StaticFieldLeak")
object MonitorHelper {

    const val TAG = "MonitorHelper"

    var context: Context? = null
    var monitorDb: MonitorDatabase? = null

    var port = 0

    //有从来ASM修改字节码对OKHTTP进行hook用的
    val hookInterceptors = listOf(
        MonitorMockInterceptor(),
        MonitorInterceptor(),
        MonitorWeakNetworkInterceptor(),
        MonitorMockResponseInterceptor()
    )

    var whiteContentTypes: String? = null
    var whiteHosts: String? = null
    var blackHosts: String? = null

    var isOpenMonitor = true

    private var singleThreadExecutor: ExecutorService? = null

    private fun threadExecutor(action: () -> Unit) {
        if (singleThreadExecutor == null || singleThreadExecutor?.isShutdown == true) {
            singleThreadExecutor = Executors.newSingleThreadExecutor()
        }
        singleThreadExecutor?.execute(action)
    }

    fun init(context: Context) {
        MonitorHelper.context = context
        thread {
            val propertiesData = MonitorProperties().paramsProperties()
            val dbName: String = propertiesData?.dbName ?: "monitor_room_db"
            val contentTypes = propertiesData?.whiteContentTypes
            whiteContentTypes = if (contentTypes.isNullOrBlank()) defaultContentTypes else contentTypes
            whiteHosts = propertiesData?.whiteHosts
            blackHosts = propertiesData?.blackHosts
            port = propertiesData?.port?.toInt() ?: 0
            initMonitorDataDao(context, dbName)
            initPCService(context, port)
        }
    }

    private fun initPCService(context: Context, port: Int = 0) {
        ALSHelper.init(context)
        ALSHelper.startService(
            if (port > 0) ServiceConfig(
                MonitorService::class.java,
                port
            ) else ServiceConfig(MonitorService::class.java)
        )
        MonitorHelper.port = ALSHelper.serviceList.firstOrNull()?.port ?: 0
    }

    private fun initMonitorDataDao(context: Context, dbName: String) {
        if (monitorDb == null) {
            monitorDb = Room
                .databaseBuilder(context.applicationContext, MonitorDatabase::class.java, dbName)
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    private fun getMonitorDataDao(): MonitorDao? {
        return monitorDb?.monitorDao()
    }

    fun insert(monitorData: MonitorData) {
        lastUpdateDataId = monitorData.id
        getMonitorDataDao()?.insert(monitorData)
    }

    fun insertAsync(map: Map<String, Any>?) {
        if (map == null || map.isEmpty()) return
        threadExecutor {
            try {
                val monitor = Gson().fromJson(Gson().toJson(map), MonitorData::class.java)
                insert(monitor)
            } catch (e: Exception) {
                Log.d(TAG, "insertAsync--${e.message}")
            }
        }
    }

    fun update(monitorData: MonitorData) {
        getMonitorDataDao()?.update(monitorData)
    }

    fun deleteAll() {
        lastUpdateDataId = 0L
        getMonitorDataDao()?.deleteAll()
    }

    fun getMonitorDataListForAndroid(
        limit: Int = 50,
        offset: Int = 0
    ): LiveData<MutableList<MonitorData>>? {
        return getMonitorDataDao()?.queryByOffsetForAndroid(limit, offset)
    }

    fun getMonitorDataList(limit: Int = 50, offset: Int = 0): MutableList<MonitorData> {
        return getMonitorDataDao()?.queryByOffset(limit, offset) ?: mutableListOf()
    }

    fun getMonitorDataByLastIdForAndroid(lastUpdateDataId: Long): LiveData<MutableList<MonitorData>>? {
        return getMonitorDataDao()?.queryByLastIdForAndroid(lastUpdateDataId)
    }

    fun getMonitorDataByLastId(lastUpdateDataId: Long): MutableList<MonitorData> {
        return getMonitorDataDao()?.queryByLastId(lastUpdateDataId) ?: mutableListOf()
    }

    fun getSharedPrefsFilesData(): HashMap<String, HashMap<String, SpValueInfo?>> {
        val ctx = context ?: return hashMapOf()
        val map = hashMapOf<String, HashMap<String, SpValueInfo?>>()
        val targetFile = File("${ctx.cacheDir.parentFile?.absolutePath}/shared_prefs")
        if (!targetFile.exists()) return hashMapOf()
        if (targetFile.isDirectory) {
            targetFile.listFiles()?.forEach { spFile ->
                Log.d(TAG, "getSharedPrefsFiles: " + spFile.name)
                val fileName = spFile.name
                val name = if (fileName.endsWith(".xml")) fileName.split(".xml")[0] else fileName
                map[name] = getSpFile(name)
            }
        }
        return map
    }

    fun getSpFile(name: String): HashMap<String, SpValueInfo?> {
        val map = hashMapOf<String, SpValueInfo?>()
        context?.getSharedPreferences(name, Context.MODE_PRIVATE)?.all?.entries?.forEach {
            val valueType = when (it.value) {
                is Int -> SPValueType.Int
                is Double -> SPValueType.Double
                is Float -> SPValueType.Float
                is Long -> SPValueType.Long
                is Boolean -> SPValueType.Boolean
                is String -> SPValueType.String
                else -> SPValueType.String
            }
            map[it.key] = SpValueInfo(it.value, valueType)
        }
        return map
    }

    fun updateSpValue(fileName: String, key: String, value: Any?) {
        SPUtils.saveValue(context ?: return, fileName, key, value)
    }
}