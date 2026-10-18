package com.example.smsforwarder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smsforwarder.databinding.ItemSmsBinding

class SmsAdapter(private val items: List<SmsData>) : RecyclerView.Adapter<SmsAdapter.VH>() {

    class VH(val b: ItemSmsBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemSmsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        with(holder.b) {
            tvSender.text = item.sender
            tvTime.text = item.time
            tvBody.text = item.body
        }
    }

    override fun getItemCount(): Int = items.size
}
