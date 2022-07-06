package com.lygttpod.monitor.okhttp

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class OkHttpClassVisitor : ClassVisitor {

    private var className: String? = null

    constructor(api: Int, classVisitor: ClassVisitor?) : super(api, classVisitor)
    constructor(classVisitor: ClassVisitor?) : this(Opcodes.ASM7, classVisitor)

    override fun visit(version: Int, access: Int, name: String?, signature: String?, superName: String?, interfaces: Array<out String>?) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.className = name
    }

    override fun visitMethod(access: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor? {
        val methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions)
        return if (className == "okhttp3/OkHttpClient\$Builder" && name == "<init>") {
            if (methodVisitor == null) null else OkHttpMethodAdapter(methodVisitor, access, name, descriptor)
        } else {
            methodVisitor
        }
    }
}