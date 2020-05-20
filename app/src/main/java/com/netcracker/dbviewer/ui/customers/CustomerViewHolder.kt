package com.netcracker.dbviewer.ui.customers

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.netcracker.dbviewer.R
import com.netcracker.dbviewer.model.Customer

class CustomerViewHolder(inflater: LayoutInflater, parent: ViewGroup):
RecyclerView.ViewHolder(inflater.inflate(R.layout.recycle_view_element,parent,false)) {
    var mName : TextView? = null
    var mPhoneNumber: TextView? = null
    var mStatus: TextView? = null

    init {
        mName = itemView.findViewById(R.id.list_title)
        mPhoneNumber = itemView.findViewById(R.id.list_description)
        mStatus = itemView.findViewById(R.id.list_second_description)
    }

    fun bind(customer: Customer, clickListener: (Customer) -> Unit) {
        mName?.text = "${customer.firstName} ${customer.lastName}"
        mPhoneNumber?.text = customer.phoneNumber.toString()
        mStatus?.text = customer.customerStatus.name

        itemView.setOnClickListener { clickListener(customer)}
    }
}
