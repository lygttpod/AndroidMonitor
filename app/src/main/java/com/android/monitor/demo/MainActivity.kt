package com.android.monitor.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.monitor.demo.databinding.ActivityMainBinding
import com.lygttpod.monitor.MonitorHelper
import com.lygttpod.monitor.utils.getPhoneWifiIpAddress
import okhttp3.*
import okio.IOException

class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getServiceAddress()
        binding.btnSendRequest.setOnClickListener {
            sendRequest("https://www.wanandroid.com/article/list/0/json")
        }
    }

    private fun sendRequest(url: String) {
        val request = Request.Builder().url(url).build();
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val result = response.body?.string()
                runOnUiThread {
                    Toast.makeText(this@MainActivity, result, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun getServiceAddress() {
        getPhoneWifiIpAddress()?.let {
            val monitorUrl = "$it:${MonitorHelper.port}/index"
            binding.tvAddress.text = monitorUrl
        }
    }
}