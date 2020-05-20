package com.netcracker.dbviewer.ui.customers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.netcracker.dbviewer.R
import com.netcracker.dbviewer.model.Customer
import com.netcracker.dbviewer.services.RestApiService
import com.netcracker.dbviewer.services.SearchRepositoryProvider
import com.netcracker.dbviewer.ui.services.ServiceListFragment
import kotlinx.android.synthetic.main.fragment_customer.*
import kotlinx.android.synthetic.main.fragment_customer.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CustomerFragment(id: Long) : Fragment() {

    private val className = CustomerFragment::class.java.simpleName
    private val customerId = id
    private val repository = SearchRepositoryProvider.provideRepository(RestApiService.create())

    companion object {
        fun newInstance(id: Long): CustomerFragment {
            return CustomerFragment(id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_customer, container, false)
        searchCustomerById(customerId)
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.title = "Customer Info"
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter: ArrayAdapter<CustomerStatus> = ArrayAdapter<CustomerStatus>(
            this.requireContext(), android.R.layout.simple_spinner_item, CustomerStatus.values())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        customer_info.sp_customer_status.adapter = adapter

        applyChanges()
        showServices()
    }

    private fun showServices() {
        customer_info.showServices.setOnClickListener {
            var fragment: Fragment? = null
            val bundle = Bundle()
            bundle.putLong("customerId", customerId)
            bundle.putString("customerName", customer_info.et_first_name.text.toString() + " " +
                    customer_info.et_last_name.text.toString())
            try {
                fragment = ServiceListFragment.newInstance()
                fragment.arguments = bundle
            } catch (e: Exception) {
                Log.d(className, e.localizedMessage.orEmpty())
            }
            val fragmentManager: FragmentManager = this.parentFragmentManager
            if (fragment != null) {
                fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit()
            }
        }
    }

    private fun searchCustomerById(id: Long) {
        repository.searchCustomerById(id).enqueue(object : Callback<Customer> {
            override fun onFailure(call: Call<Customer>?, t: Throwable?) {
                Log.d(className, t?.localizedMessage.orEmpty())
            }

            override fun onResponse(call: Call<Customer>?, response: Response<Customer>?) {
                fillCustomerInfo(response?.body())
            }
        })
    }

    private fun fillCustomerInfo(customer: Customer?) {
        customer_info.et_first_name.setText(customer?.firstName)
        customer_info.et_last_name.setText(customer?.lastName)
        customer_info.et_phone_number.setText(customer?.phoneNumber.toString())
        customer_info.sp_customer_status.setSelection(customer?.customerStatus?.id!!.toInt() - 1)
    }

    private fun applyChanges() {
        customer_info.button.setOnClickListener {
            val curStatus = customer_info.sp_customer_status.selectedItem.toString()
            val customerStatus = Customer.Status(
                CustomerStatus.valueOf(curStatus).id.toLong(),
                CustomerStatus.valueOf(curStatus).name
            )
            val newCustomer = Customer(
                customerId,
                customer_info.et_first_name.text.toString(),
                customer_info.et_last_name.text.toString(),
                customer_info.et_phone_number.text.toString().toLong(),
                customerStatus
            )
            repository.saveCustomer(newCustomer, customerId).enqueue(object : Callback<Customer> {
                override fun onFailure(call: Call<Customer>?, t: Throwable?) {
                    Log.d(className, t?.localizedMessage.orEmpty())
                }

                override fun onResponse(call: Call<Customer>?, response: Response<Customer>?) {
                    Toast.makeText(
                        context, "Data for customer with id=[${customerId}] was updated",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}