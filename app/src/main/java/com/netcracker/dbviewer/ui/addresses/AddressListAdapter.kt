package com.netcracker.dbviewer.ui.addresses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.netcracker.dbviewer.model.Address
import com.netcracker.dbviewer.model.Customer

class AddressListAdapter(private val list: List<Address>)
    : RecyclerView.Adapter<AddressViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AddressViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address: Address = list[position]
        holder.bind(address)
    }

    override fun getItemCount(): Int = list.count()
}
