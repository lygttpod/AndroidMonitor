package com.lygttpod.monitor.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lygttpod.monitor.data.MonitorData

@Database(entities = [MonitorData::class], version = 1, exportSchema = false)
abstract class MonitorDatabase : RoomDatabase() {

    abstract fun monitorDao(): MonitorDao

}