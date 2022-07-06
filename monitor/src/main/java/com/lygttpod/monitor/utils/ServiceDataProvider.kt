package com.lygttpod.monitor.utils

import com.lygttpod.monitor.MonitorHelper
import com.lygttpod.monitor.data.MonitorData

object ServiceDataProvider {

    fun getMonitorDataList(limit: Int = 50, offset: Int = 0): MutableList<MonitorData> {
        return MonitorHelper.getMonitorDataList(limit, offset)
    }

    fun getMonitorDataByLastId(lastUpdateDataId: Long): MutableList<MonitorData> {
        return MonitorHelper.getMonitorDataByLastId(lastUpdateDataId)
    }
}