package com.netcracker.dbviewer.ui.services

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.netcracker.dbviewer.R
import com.netcracker.dbviewer.model.Hardware
import com.netcracker.dbviewer.model.Service

class ServiceViewHolder(inflater: LayoutInflater, parent: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.recycle_view_simple_element,parent,false)) {
    var mName : TextView? = null
    var mStatus: TextView? = null

    init {
        mName = itemView.findViewById(R.id.list_title)
        mStatus = itemView.findViewById(R.id.list_description)
    }

    fun bind(service: Service, clickListener: (Service) -> Unit) {
        mName?.text = service.name
        mStatus?.text = service.serviceStatus.name

        itemView.setOnClickListener { clickListener(service)}
    }
}
