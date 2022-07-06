package com.lygttpod.monitor.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lygttpod.monitor.MonitorHelper
import com.lygttpod.monitor.R
import com.lygttpod.monitor.databinding.ActivityMonitorConfigBinding
import com.lygttpod.monitor.enum.WeakNetworkType
import com.lygttpod.monitor.mock.MockHelper
import com.lygttpod.monitor.weaknetwork.WeakNetworkHelper

class MonitorConfigActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMonitorConfigBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitorConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initMonitorView()
        initWeakNetView()
        initMockView()
        initListener()
    }

    private fun initMonitorView() {
        binding.monitorSwitchBtn.isChecked = MonitorHelper.isOpenMonitor
    }

    private fun initMockView() {
        binding.mockSwitchBtn.isChecked = MockHelper.isOpen
        setMockSwitchUI(MockHelper.isOpen)
        binding.editBaseUrl.setText(MockHelper.mockBaseUrl)
        binding.editPath.setText(MockHelper.mockPaths)
        binding.editResponse.setText(MockHelper.mockResponse)
    }

    private fun initWeakNetView() {
        binding.switchBtn.isChecked = WeakNetworkHelper.isOpen
        setWeakSwitchUI(WeakNetworkHelper.isOpen)
        binding.editSpeed.setText(WeakNetworkHelper.responseSpeedByte.toString())
        setCurrentNetDes(WeakNetworkHelper.weakNetType())
        setSpeedContainerVisible(WeakNetworkHelper.weakNetType())

        when (WeakNetworkHelper.weakNetType()) {
            WeakNetworkType.TIME_OUT -> binding.radioBtnTimeOut.isChecked = true
            WeakNetworkType.NO_NETWORK -> binding.radioBtnNoNet.isChecked = true
            WeakNetworkType.SPEED_LIMIT -> binding.radioBtnSpeedLimit.isChecked = true
        }
    }

    private fun initListener() {
        binding.ivBack.setOnClickListener { finish() }

        binding.monitorSwitchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            MonitorHelper.isOpenMonitor = isChecked
            Toast.makeText(this, if (isChecked) "抓包功能已开启" else "抓包功能已关闭", Toast.LENGTH_SHORT).show()
        }

        binding.switchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            WeakNetworkHelper.isOpen = isChecked
            setWeakSwitchUI(isChecked)
        }

        binding.mockSwitchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            MockHelper.isOpen = isChecked
            setMockSwitchUI(isChecked)
        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_btn_time_out -> {
                    WeakNetworkHelper.setWeakType(WeakNetworkType.TIME_OUT)
                }
                R.id.radio_btn_no_net -> {
                    WeakNetworkHelper.setWeakType(WeakNetworkType.NO_NETWORK)
                }
                R.id.radio_btn_speed_limit -> {
                    WeakNetworkHelper.setWeakType(WeakNetworkType.SPEED_LIMIT)
                }
            }

            setSpeedContainerVisible(WeakNetworkHelper.weakNetType())
            setCurrentNetDes(WeakNetworkHelper.weakNetType())
        }

        binding.editSpeed.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val speed = s?.toString()?.toLongOrNull() ?: 1024L
                WeakNetworkHelper.responseSpeedByte = speed
            }

        })

        binding.editBaseUrl.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val url = s?.toString() ?: ""
                MockHelper.mockBaseUrl = url
            }

        })

        binding.editPath.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val url = s?.toString() ?: ""
                MockHelper.mockPaths = url
            }

        })

        binding.editResponse.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                MockHelper.mockResponse = s?.toString() ?: ""
            }

        })

        binding.editResponseCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                MockHelper.mockSuccessResponseCode = (s?.toString() ?: "200").toIntOrNull() ?: 200
            }

        })
    }

    private fun setSpeedContainerVisible(type: WeakNetworkType) {
        binding.groupSpeed.visibility =
            if (type == WeakNetworkType.SPEED_LIMIT) View.VISIBLE else View.GONE
    }

    private fun setCurrentNetDes(type: WeakNetworkType) {
        val des = when (type) {
            WeakNetworkType.TIME_OUT -> "当前配置为：模拟网络<<请求超时>>"
            WeakNetworkType.NO_NETWORK -> "当前配置为：模拟网络<<请求断网>>"
            WeakNetworkType.SPEED_LIMIT -> "当前配置为：模拟网络<<请求限速>>"
        }
        binding.tvDes.text = des
    }

    private fun setWeakSwitchUI(checked: Boolean) {
        val visible = if (checked) View.VISIBLE else View.GONE
        binding.clWeakConfigContainer.visibility = visible
    }

    private fun setMockSwitchUI(checked: Boolean) {
        val visible = if (checked) View.VISIBLE else View.GONE
        binding.clMockConfigContainer.visibility = visible
    }
}