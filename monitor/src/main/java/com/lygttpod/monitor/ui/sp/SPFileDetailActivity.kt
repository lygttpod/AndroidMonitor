package com.lygttpod.monitor.ui.sp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lygttpod.monitor.MonitorHelper
import com.lygttpod.monitor.data.SpSubData
import com.lygttpod.monitor.databinding.ActivitySpFileDetailBinding
import com.lygttpod.monitor.ui.dialog.SpModifyDialog

/**
 * <pre>
 *      author  : Allen
 *      date    : 2022/8/6
 *      desc    :
 * </pre>
 */
class SPFileDetailActivity : AppCompatActivity() {

    companion object {
        private const val FILE_NAME = "file_name"

        fun buildIntent(context: Context, fileName: String?): Intent {
            return Intent(context, SPFileDetailActivity::class.java).apply {
                val bundle = Bundle()
                bundle.putString(FILE_NAME, fileName)
                this.putExtras(bundle)
            }
        }
    }

    private lateinit var binding: ActivitySpFileDetailBinding
    private var handle: Handler = Handler(Looper.getMainLooper())
    private var fileName: String? = null
    var list = listOf<SpSubData>()
    private var adapter: SpFileDetailAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpFileDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fileName = intent.getStringExtra(FILE_NAME)
        initView()
        initData()
    }

    private fun initData() {
        list = MonitorHelper.getSpFile(fileName ?: "").map { SpSubData(it.key, it.value) }.toList()
        adapter?.setData(list)
    }

    private fun initView() {
        binding.ivBack.setOnClickListener { finish() }
        binding.swipeRefresh.setOnRefreshListener {
            handle.postDelayed({
                binding.swipeRefresh.isRefreshing = false
                initData()
            }, 1000)
        }
        binding.tvTitle.text = fileName ?: "详情"
        adapter = SpFileDetailAdapter()
        adapter?.onItemClick = {
            SpModifyDialog.show(this, fileName, it.keyName, it.keyValue) { key, spValueInfo ->
                val position = list.indexOfFirst { it.keyName == key }
                if (position > -1) {
                    list[position].keyValue = spValueInfo
                    adapter?.notifyItemChanged(position)
                }
            }
        }
        binding.recyclerVew.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }
    }
}