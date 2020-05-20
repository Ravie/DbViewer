package com.netcracker.dbviewer.ui.addresses

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.netcracker.dbviewer.R
import com.netcracker.dbviewer.model.Address
import com.netcracker.dbviewer.model.Customer

class AddressViewHolder(inflater: LayoutInflater, parent: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.recycle_view_single_element,parent,false)) {
    var mName : TextView? = null

    init {
        mName = itemView.findViewById(R.id.list_title)
    }

    fun bind(address: Address) {
        mName?.text = address.fullAddress
    }
}
