package com.lygttpod.monitor.okhttp

import com.quinn.hunter.transform.HunterTransform
import com.quinn.hunter.transform.RunVariant
import org.gradle.api.Project

class OkHttpTransform(project: Project?) : HunterTransform(project) {
    init { this.bytecodeWeaver = OkHttpWeaver() }

    override fun getRunVariant(): RunVariant {
        return RunVariant.DEBUG
    }
}