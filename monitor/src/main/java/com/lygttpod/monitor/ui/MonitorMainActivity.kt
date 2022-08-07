package com.lygttpod.monitor.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationBarView
import com.lygttpod.monitor.MonitorHelper
import com.lygttpod.monitor.R
import com.lygttpod.monitor.databinding.ActivityMonitorMainBinding
import com.lygttpod.monitor.ui.request.MonitorMainFragment
import com.lygttpod.monitor.ui.sp.SPFileListFragment
import com.lygttpod.monitor.utils.getPhoneWifiIpAddress
import kotlin.concurrent.thread

class MonitorMainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    private var handle: Handler = Handler(Looper.getMainLooper())

    private lateinit var binding: ActivityMonitorMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitorMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initPage()
    }

    override fun onDestroy() {
        super.onDestroy()
        handle.removeCallbacksAndMessages(null)
    }

    private fun initView() {
        binding.tvTitle.text = getString(R.string.monitor_app_name)
        binding.tvClean.setOnClickListener {
            thread { MonitorHelper.deleteAll() }
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

        binding.bottomNavigationView.setOnItemSelectedListener(this)
    }

    private fun initPage() {
        val fragments = listOf(MonitorMainFragment(), SPFileListFragment())
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = fragments.size

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }
        }
        binding.viewPager.offscreenPageLimit = fragments.size
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> binding.bottomNavigationView.selectedItemId = R.id.navigation_monitor
                    1 -> binding.bottomNavigationView.selectedItemId = R.id.navigation_sharedPrefs
                }
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_monitor -> {
                binding.viewPager.currentItem = 0
                return true
            }
            R.id.navigation_sharedPrefs -> {
                binding.viewPager.currentItem = 1
                return true
            }
        }
        return false
    }

}