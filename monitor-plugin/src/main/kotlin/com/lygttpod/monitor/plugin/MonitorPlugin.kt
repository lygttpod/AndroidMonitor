package com.lygttpod.monitor.plugin

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
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

        //这里appExtension获取方式与原transform api不同，可自行对比
        val appExtension = project.extensions.getByType(AndroidComponentsExtension::class.java)

        // 注册单个字节码转换器任务
        appExtension.onVariants { variant ->
            //可以通过variant来获取当前编译环境的一些信息，最重要的是可以 variant.name 来区分是debug模式还是release模式编译
            variant.instrumentation.transformClassesWith(OkHttpTransform::class.java, InstrumentationScope.ALL) {
            }
            //InstrumentationScope.ALL 配合 FramesComputationMode.COPY_FRAMES可以指定该字节码转换器在全局生效，包括第三方lib
            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)
        }
    }

}