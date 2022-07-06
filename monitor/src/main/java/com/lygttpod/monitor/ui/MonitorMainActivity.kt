package com.lygttpod.monitor.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lygttpod.monitor.MonitorHelper
import com.lygttpod.monitor.R
import com.lygttpod.monitor.adapter.MonitorListAdapter
import com.lygttpod.monitor.data.MonitorData
import com.lygttpod.monitor.databinding.ActivityMonitorMainBinding
import com.lygttpod.monitor.utils.getPhoneWifiIpAddress
import kotlin.concurrent.thread

class MonitorMainActivity : AppCompatActivity() {

    private var adapter: MonitorListAdapter? = null

    private var handle: Handler = Handler(Looper.getMainLooper())

    private lateinit var binding: ActivityMonitorMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitorMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initRv()
        setData()
    }

    override fun onDestroy() {
        super.onDestroy()
        handle.removeCallbacksAndMessages(null)
    }

    private fun initView() {
        binding.tvTitle.text = getString(R.string.monitor_app_name)
        binding.swipeRefresh.setOnRefreshListener {
            handle.postDelayed({
                binding.swipeRefresh.isRefreshing = false
                setData()
            }, 1000)
        }

        binding.tvClean.setOnClickListener {
            thread { MonitorHelper.deleteAll() }
            adapter?.setData(null)
        }
        getPhoneWifiIpAddress()?.let {
            binding.tvWifiAddress.visibility = View.VISIBLE
            val monitorUrl = "局域网内可访问：$it:${MonitorHelper.port}/index"
            binding.tvWifiAddress.text = monitorUrl
            Log.d("MonitorHelper", monitorUrl)
        }

        binding.ivSetting.setOnClickListener {
            startActivity(Intent(this, MonitorConfigActivity::class.java))
        }
    }

    private fun setData() {
        MonitorHelper.getMonitorDataListForAndroid(limit = 100)?.observe(this, Observer {
            adapter?.setData(it)
        })
    }

    private fun initRv() {
        adapter = MonitorListAdapter()
        adapter?.itemClick = { gotoMonitorDetail(it) }
        binding.rvMonitor.layoutManager = LinearLayoutManager(this)
        binding.rvMonitor.adapter = adapter
    }

    private fun gotoMonitorDetail(monitorData: MonitorData) {
        startActivity(MonitorDetailActivity.buildIntent(this, monitorData))
    }
}