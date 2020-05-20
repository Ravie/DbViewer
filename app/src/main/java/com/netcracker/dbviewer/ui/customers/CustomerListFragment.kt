package com.netcracker.dbviewer.ui.customers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.netcracker.dbviewer.R
import com.netcracker.dbviewer.model.Customer
import com.netcracker.dbviewer.model.CustomerResult
import com.netcracker.dbviewer.services.RestApiService
import com.netcracker.dbviewer.services.SearchRepositoryProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CustomerListFragment : Fragment() {

    private val className = CustomerListFragment::class.java.simpleName
    private var mCustomerList: List<Customer>? = null

    companion object {
        fun newInstance(): CustomerListFragment = CustomerListFragment()
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
        val view = inflater.inflate(R.layout.fragment_customer_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.customer_recycle_view)
        searchCustomers(recyclerView)
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.title = "Customers"
        return view
    }

    private fun searchCustomers(recyclerView : RecyclerView) {
        val apiService = RestApiService.create()
        val repository = SearchRepositoryProvider.provideRepository(apiService)
        repository.searchCustomers().enqueue(object : Callback<CustomerResult> {
            override fun onFailure(call: Call<CustomerResult>?, t: Throwable?) {
                Log.d(className, t?.localizedMessage.orEmpty())
            }

            override fun onResponse(call: Call<CustomerResult>?, response: Response<CustomerResult>?) {
                mCustomerList = response?.body()?.content
                recyclerView.adapter = CustomerListAdapter(mCustomerList!!) { customer ->
                    customerItemClicked(customer) }
                recyclerView.layoutManager = LinearLayoutManager(activity)
            }
        })
    }

    private fun customerItemClicked(customer: Customer) {
        //Toast.makeText(this.context, "Clicked: ${customer.firstName} ${customer.lastName}", Toast.LENGTH_SHORT).show()

        // Создадим новый фрагмент
        var fragment: Fragment? = null

        // Handle navigation view item clicks here.
        try {
            fragment = CustomerFragment.newInstance(customer.id)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Вставляем фрагмент, заменяя текущий фрагмент
        val fragmentManager: FragmentManager = this.parentFragmentManager
        if (fragment != null) {
            fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.nav_host_fragment, fragment)
                .commit()
        }
    }
}
