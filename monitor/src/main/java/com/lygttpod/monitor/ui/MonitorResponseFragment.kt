package com.lygttpod.monitor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lygttpod.monitor.ui.MonitorDetailActivity.Companion.KEY_MONITOR_DATA
import com.lygttpod.monitor.R
import com.lygttpod.monitor.data.MonitorData
import com.lygttpod.monitor.databinding.FragmentMonitorResponseBinding
import com.lygttpod.monitor.utils.formatBody

class MonitorResponseFragment : Fragment() {

    companion object {
        fun newInstance(monitorData: MonitorData?): MonitorResponseFragment {
            return MonitorResponseFragment().apply {
                arguments = Bundle().apply { putSerializable(KEY_MONITOR_DATA, monitorData) }
            }
        }
    }

    private var monitorData: MonitorData? = null

    private lateinit var binding: FragmentMonitorResponseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_monitor_response, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMonitorResponseBinding.bind(view)
        monitorData = arguments?.getSerializable(KEY_MONITOR_DATA) as? MonitorData?
        initData()
    }

    private fun initData() {
        binding.tvUrl.text = monitorData?.url
        binding.tvMethod.text = monitorData?.method
        binding.tvCode.text = monitorData?.responseCode.toString()
        binding.tvResponseDate.text = monitorData?.responseTime

        val responseBody = if (monitorData?.source == "Flutter") formatBody(
            monitorData?.responseBody ?: "",
            "json"
        ) else monitorData?.responseBody
        val responseType = monitorData?.responseContentType

        binding.tvResponseBody.text = if (responseBody.isNullOrBlank()) (monitorData?.errorMsg
            ?: monitorData?.responseMessage) else formatBody(responseBody, responseType)
    }
}