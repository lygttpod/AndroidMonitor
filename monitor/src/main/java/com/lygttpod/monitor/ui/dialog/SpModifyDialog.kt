package com.lygttpod.monitor.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import com.lygttpod.monitor.MonitorHelper
import com.lygttpod.monitor.R
import com.lygttpod.monitor.data.SpValueInfo
import com.lygttpod.monitor.databinding.DialogSpModifyBinding
import com.lygttpod.monitor.enum.SPValueType

/**
 * <pre>
 *      author  : Allen
 *      date    : 2022/8/7
 *      desc    :
 * </pre>
 */
class SpModifyDialog(context: Context) : Dialog(context, R.style.B2TDialogTheme) {

    companion object {
        fun show(
            context: Context,
            fileName: String?,
            key: String?,
            valueInfo: SpValueInfo?,
            onModifySuccess: (String, SpValueInfo?) -> Unit
        ) {
            SpModifyDialog(context).apply {
                this.spFileName = fileName
                this.spKey = key
                this.spValueInfo = valueInfo
                this.onModifySuccess = onModifySuccess
            }.show()
        }
    }

    private lateinit var binding: DialogSpModifyBinding
    private var spFileName: String? = null
    private var spKey: String? = null
    private var spValueInfo: SpValueInfo? = null

    private var onModifySuccess: ((String, SpValueInfo?) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogSpModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initWindow()
        initView()
    }

    private fun initView() {
        binding.btnModify.setOnClickListener {
            if (spFileName != null && spKey != null) {
                val modifyContent = binding.etContent.text.toString()
                val originType = spValueInfo!!.type
                val realValue = getRealValueByType(modifyContent, originType)
                if (realValue == null) {
                    Toast.makeText(context, "请输入《${originType}》类型的值哦", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                MonitorHelper.updateSpValue(spFileName!!, spKey!!, realValue)
                onModifySuccess?.invoke(spKey!!, SpValueInfo(realValue, originType))
                Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
        binding.tvTitle.text = spFileName
        binding.tvKey.text = spKey
        binding.tvType.text = spValueInfo?.type?.value
        binding.etContent.setText("${spValueInfo?.value}")
    }

    private fun getRealValueByType(content: String, type: SPValueType): Any? {
        return when (type) {
            SPValueType.Boolean -> content.toBoolean()
            SPValueType.Int -> content.toIntOrNull()
            SPValueType.Float -> content.toFloatOrNull()
            SPValueType.Long -> content.toLongOrNull()
            SPValueType.Double -> content.toDoubleOrNull()
            else -> content
        }
    }

    private fun initWindow() {
        val window = window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.BOTTOM)
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            dip2px(context, 300f)
        )
    }

    private fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}