package com.netcracker.dbviewer.ui.hardware

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.netcracker.dbviewer.R
import com.netcracker.dbviewer.model.Hardware

class HardwareViewHolder(inflater: LayoutInflater, parent: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.recycle_view_element,parent,false)) {
    var mName : TextView? = null
    var mSerial: TextView? = null
    var mStatus: TextView? = null

    init {
        mName = itemView.findViewById(R.id.list_title)
        mSerial = itemView.findViewById(R.id.list_description)
        mStatus = itemView.findViewById(R.id.list_second_description)
    }

    fun bind(
        hardware: Hardware,
        clickListener: (Hardware) -> Unit
    ) {
        mName?.text = hardware.name
        mSerial?.text = hardware.serial
        mStatus?.text = hardware.hardwareStatus.name

        itemView.setOnClickListener { clickListener(hardware)}
    }
}
