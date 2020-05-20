package com.netcracker.dbviewer.ui.services

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
import com.netcracker.dbviewer.model.Service
import com.netcracker.dbviewer.model.ServiceResult
import com.netcracker.dbviewer.services.RestApiService
import com.netcracker.dbviewer.services.SearchRepositoryProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ServiceListFragment : Fragment() {
    private val className = ServiceListFragment::class.java.simpleName
    private var customerId = -1L
    private var customerName = ""
    private var mServiceList: List<Service>? = null

    companion object {
        fun newInstance(): ServiceListFragment {
            return ServiceListFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        if (arguments != null) {
            customerId = requireArguments().getLong("customerId")
            customerName = requireArguments().getString("customerName").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_service_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.service_recycle_view)
        searchService(recyclerView)

        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        if (customerId != -1L) {
            toolbar?.title = "Services: $customerName"
        } else {
            toolbar?.title = "Services"
        }

        return view
    }

    private fun searchService(recyclerView : RecyclerView) {
        val apiService = RestApiService.create()
        val repository = SearchRepositoryProvider.provideRepository(apiService)
        if (customerId == -1L) {
            val call = repository.searchServices()
            execute(call, recyclerView)
        } else {
            val call = repository.searchServicesByCustomer(customerId)
            execute(call, recyclerView)
        }
    }

    private fun execute(
        call: Call<ServiceResult>,
        recyclerView: RecyclerView
    ) {
        call.enqueue(object : Callback<ServiceResult> {
            override fun onFailure(call: Call<ServiceResult>?, t: Throwable?) {
                Log.d(className, t?.localizedMessage.orEmpty())
            }

            override fun onResponse(call: Call<ServiceResult>?, response: Response<ServiceResult>?) {
                mServiceList = response?.body()?.content
                recyclerView.adapter = ServiceListAdapter(mServiceList!!) { service ->
                    serviceItemClicked(service) }
                recyclerView.layoutManager = LinearLayoutManager(activity)
            }
        })
    }

    private fun serviceItemClicked(service: Service) {
        var fragment: Fragment? = null
        try {
            fragment = ServiceFragment.newInstance(service.id)
        } catch (e: Exception) {
            e.printStackTrace()
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
