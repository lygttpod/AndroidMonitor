package com.lygttpod.monitor.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lygttpod.monitor.adapter.MonitorPagerAdapter
import com.lygttpod.monitor.data.MonitorData
import com.lygttpod.monitor.databinding.ActivityMonitorDetailBinding
import com.lygttpod.monitor.utils.formatBody

class MonitorDetailActivity : AppCompatActivity() {

    companion object {
        const val KEY_MONITOR_DATA = "monitor_data"
        fun buildIntent(context: Context, monitorData: MonitorData): Intent {
            return Intent(context, MonitorDetailActivity::class.java).apply {
                val bundle = Bundle()
                bundle.putSerializable(KEY_MONITOR_DATA, monitorData)
                this.putExtras(bundle)
            }
        }
    }

    private var monitorData: MonitorData? = null

    private lateinit var binding: ActivityMonitorDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitorDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        monitorData = intent.getSerializableExtra(KEY_MONITOR_DATA) as? MonitorData?
        initView()
    }

    private fun initView() {
        binding.ivBack.setOnClickListener { finish() }
        binding.ivShare.setOnClickListener { share() }
        binding.tvTitle.text = monitorData?.path
        val fragmentPagerAdapter = MonitorPagerAdapter(supportFragmentManager)
        fragmentPagerAdapter.addFragment(MonitorResponseFragment.newInstance(monitorData), "响应")
        fragmentPagerAdapter.addFragment(MonitorRequestFragment.newInstance(monitorData), "请求")
        binding.viewPager.adapter = fragmentPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    private fun share() {
        val shareString =
            "url = ${monitorData?.url} \n method = ${monitorData?.method} \n header = ${monitorData?.requestHeaders} \n requestBody = ${
                formatBody(
                    monitorData?.requestBody
                        ?: "", monitorData?.requestContentType
                )
            } \n responseBody = ${
                formatBody(
                    monitorData?.responseBody
                        ?: "", monitorData?.responseContentType
                )
            }"
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareString)
        startActivity(Intent.createChooser(shareIntent, "分享抓包数据"))
    }
}