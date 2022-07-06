package com.lygttpod.monitor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lygttpod.monitor.R
import com.lygttpod.monitor.data.MonitorData
import com.lygttpod.monitor.databinding.ItemMonitorBinding

class MonitorListAdapter : RecyclerView.Adapter<MonitorListAdapter.MonitorListHolder>() {

    var itemClick: ((MonitorData) -> Unit)? = null

    private var monitorList: MutableList<MonitorData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonitorListHolder {
        return MonitorListHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_monitor, parent, false)
        )
    }

    override fun getItemCount(): Int = monitorList.size

    override fun onBindViewHolder(holder: MonitorListHolder, position: Int) {
        holder.bindData(monitorList[position])
    }

    fun setData(list: MutableList<MonitorData>?) {
        this.monitorList = list ?: mutableListOf()
        notifyItemChanged(0, itemCount)
    }

    inner class MonitorListHolder(var containerView: View) :
        RecyclerView.ViewHolder(containerView) {

        private val binding: ItemMonitorBinding = ItemMonitorBinding.bind(containerView)

        var monitorData: MonitorData? = null

        init {
            containerView.setOnClickListener {
                itemClick?.invoke(
                    monitorData ?: return@setOnClickListener
                )
            }
        }

        fun bindData(monitorData: MonitorData) {
            this.monitorData = monitorData

            binding.tvHost.text = monitorData.host
            binding.tvPath.text = monitorData.path
            binding.tvRequestDate.text = monitorData.requestTime
            binding.tvDuration.visibility = if (monitorData.duration <= 0) View.GONE else View.VISIBLE
            binding.tvDuration.text = "${monitorData.duration}ms"
            binding.tvCode.text = monitorData.responseCode.toString()
            binding.tvMethod.text = monitorData.method
            binding.tvSource.text = monitorData.source
            binding.tvSource.visibility = if (monitorData.source.isNullOrBlank()) View.GONE else View.VISIBLE

            binding.tvPath.setTextColor(getColor(monitorData.responseCode))
            binding.tvCode.setTextColor(getColor(monitorData.responseCode))
        }

        private fun getColor(code: Int): Int {
            when (code) {
                200 -> return ContextCompat.getColor(
                    containerView.context,
                    R.color.monitor_status_success
                )
                300 -> return ContextCompat.getColor(
                    containerView.context,
                    R.color.monitor_status_300
                )
                400 -> return ContextCompat.getColor(
                    containerView.context,
                    R.color.monitor_status_400
                )
                500 -> return ContextCompat.getColor(
                    containerView.context,
                    R.color.monitor_status_500
                )
                else -> return ContextCompat.getColor(
                    containerView.context,
                    R.color.monitor_status_error
                )
            }
        }

    }

}