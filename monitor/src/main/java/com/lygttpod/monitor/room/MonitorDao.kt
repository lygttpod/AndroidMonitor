package com.lygttpod.monitor.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lygttpod.monitor.data.MonitorData

@Dao
interface MonitorDao {
    @Query("SELECT * FROM monitor WHERE id > :lastId ORDER BY id DESC")
    fun queryByLastIdForAndroid(lastId: Long): LiveData<MutableList<MonitorData>>

    @Query("SELECT * FROM monitor ORDER BY id DESC LIMIT :limit OFFSET :offset")
    fun queryByOffsetForAndroid(limit: Int, offset: Int): LiveData<MutableList<MonitorData>>

    @Query("SELECT * FROM monitor")
    fun queryAllForAndroid(): LiveData<MutableList<MonitorData>>

    @Query("SELECT * FROM monitor WHERE id > :lastId ORDER BY id DESC")
    fun queryByLastId(lastId: Long): MutableList<MonitorData>

    @Query("SELECT * FROM monitor ORDER BY id DESC LIMIT :limit OFFSET :offset")
    fun queryByOffset(limit: Int, offset: Int): MutableList<MonitorData>

    @Query("SELECT * FROM monitor")
    fun queryAll(): MutableList<MonitorData>

    @Insert
    fun insert(data: MonitorData)

    @Update
    fun update(data: MonitorData)

    @Query("DELETE FROM monitor")
    fun deleteAll()
}