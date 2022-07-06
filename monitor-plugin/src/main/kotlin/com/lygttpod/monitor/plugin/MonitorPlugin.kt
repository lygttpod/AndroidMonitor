package com.lygttpod.monitor.plugin

import com.android.build.gradle.AppExtension
import com.lygttpod.monitor.okhttp.OkHttpTransform
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.*

class MonitorPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        var enableMonitorPlugin = true
        val properties = Properties()
        val file = project.rootProject.file("local.properties")
        if (file.exists()) {
            properties.load(file.inputStream())
            enableMonitorPlugin = properties.getProperty("monitor.enablePlugin", "true")?.toBoolean() ?: true
        }
        println("MonitorPlugin---->enableMonitorPlugin = $enableMonitorPlugin")
        if (!enableMonitorPlugin) return
        try {
            val appException: AppExtension = project.extensions.getByName("android") as AppExtension
            appException.registerTransform(OkHttpTransform(project))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}