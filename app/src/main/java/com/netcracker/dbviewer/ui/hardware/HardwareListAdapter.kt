package com.netcracker.dbviewer.ui.hardware

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.netcracker.dbviewer.model.Hardware

class HardwareListAdapter(private val list: List<Hardware>,
                          private val clickListener: (Hardware) -> Unit)
    : RecyclerView.Adapter<HardwareViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HardwareViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HardwareViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: HardwareViewHolder, position: Int) {
        val hardware: Hardware = list[position]
        holder.bind(hardware, clickListener)
    }

    override fun getItemCount(): Int = list.count()
}
