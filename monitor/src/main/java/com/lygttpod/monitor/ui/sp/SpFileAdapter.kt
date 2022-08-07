package com.lygttpod.monitor.ui.sp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lygttpod.monitor.R
import com.lygttpod.monitor.data.SpData
import com.lygttpod.monitor.databinding.ItemSpFileBinding

/**
 * <pre>
 *      author  : Allen
 *      date    : 2022/8/6
 *      desc    :
 * </pre>
 */
class SpFileAdapter : RecyclerView.Adapter<SpFileAdapter.SpFileViewHolder>() {

    private var list: List<SpData> = listOf()

    var onItemClick: ((SpData) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpFileViewHolder {
        return SpFileViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_sp_file, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SpFileViewHolder, position: Int) {
        holder.bindData(list[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(list[position])
        }
    }

    override fun getItemCount() = list.size

    fun setData(list: List<SpData>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class SpFileViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemSpFileBinding.bind(view)

        fun bindData(data: SpData) {
            binding.tvSpFileName.text = "${data.fileName}.xml"
        }
    }
}