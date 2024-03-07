package com.lygttpod.monitor.okhttp

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.objectweb.asm.ClassVisitor

abstract class OkHttpTransform :
    AsmClassVisitorFactory<InstrumentationParameters.None> {
    override fun createClassVisitor(classContext: ClassContext,
        nextClassVisitor: ClassVisitor): ClassVisitor {
        //指定真正的ASM转换器
        return OkHttpClassVisitor(nextClassVisitor)
    }

    // 通过classData中的当前类的信息，用来过滤哪些类需要执行字节码转换，
    // 这里支持通过类名，包名，注解，接口，父类等属性来组合判断
    override fun isInstrumentable(classData: ClassData): Boolean {
        //可以设classData过滤条件
        return true
    }
}