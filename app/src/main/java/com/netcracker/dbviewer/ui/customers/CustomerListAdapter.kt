package com.netcracker.dbviewer.ui.customers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.netcracker.dbviewer.model.Customer

class CustomerListAdapter(private val list: List<Customer>,
                          private val clickListener: (Customer) -> Unit)
    : RecyclerView.Adapter<CustomerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CustomerViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val customer: Customer = list[position]
        holder.bind(customer, clickListener)
    }

    override fun getItemCount(): Int = list.count()
}
