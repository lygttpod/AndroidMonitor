package com.lygttpod.monitor.okhttp

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

class OkHttpMethodAdapter(methodVisitor: MethodVisitor?, access: Int, name: String?, descriptor: String?) : AdviceAdapter(Opcodes.ASM7, methodVisitor, access, name, descriptor) {

    override fun onMethodExit(opcode: Int) {
        super.onMethodExit(opcode)
        mv?.let {
            it.visitVarInsn(ALOAD, 0)
            it.visitFieldInsn(GETFIELD, "okhttp3/OkHttpClient\$Builder", "interceptors", "Ljava/util/List;")
            it.visitFieldInsn(GETSTATIC, "com/lygttpod/monitor/MonitorHelper", "INSTANCE", "Lcom/lygttpod/monitor/MonitorHelper;")
            it.visitMethodInsn(INVOKEVIRTUAL, "com/lygttpod/monitor/MonitorHelper", "getHookInterceptors", "()Ljava/util/List;", false)
            it.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "addAll", "(Ljava/util/Collection;)Z", true)
            it.visitInsn(POP)
        }
    }
}