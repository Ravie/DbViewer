package com.netcracker.dbviewer.ui.services

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.netcracker.dbviewer.model.Hardware
import com.netcracker.dbviewer.model.Service

class ServiceListAdapter(private val list: List<Service>,
                         private val clickListener: (Service) -> Unit)
    : RecyclerView.Adapter<ServiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ServiceViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service: Service = list[position]
        holder.bind(service, clickListener)
    }

    override fun getItemCount(): Int = list.count()
}
