package com.lygttpod.monitor.data

/**
 * <pre>
 *      author  : Allen
 *      date    : 2022/8/6
 *      desc    :
 * </pre>
 */
data class SpData(
    val fileName: String?,
    val subList: List<SpSubData>? = null
)

data class SpSubData(val keyName: String?, var keyValue: SpValueInfo?)
