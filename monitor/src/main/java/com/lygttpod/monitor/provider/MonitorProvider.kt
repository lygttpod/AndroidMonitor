package com.lygttpod.monitor.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.lygttpod.monitor.MonitorHelper

class MonitorProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        val context = context
        if (context == null) {
            Log.e("MonitorProvider", "MonitorProvider初始化context失败")
        } else {
            MonitorHelper.init(context)
        }
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? = null

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun getType(uri: Uri): String? = null
}