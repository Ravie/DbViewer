package com.netcracker.dbviewer.ui.services

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
import com.netcracker.dbviewer.model.Service
import com.netcracker.dbviewer.services.RestApiService
import com.netcracker.dbviewer.services.SearchRepositoryProvider
import com.netcracker.dbviewer.ui.hardware.HardwareListFragment
import kotlinx.android.synthetic.main.fragment_service.*
import kotlinx.android.synthetic.main.fragment_service.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceFragment(id: Long) : Fragment() {

    private val className = ServiceFragment::class.java.simpleName
    private val serviceId = id
    private val repository = SearchRepositoryProvider.provideRepository(RestApiService.create())

    companion object {
        fun newInstance(id: Long): ServiceFragment {
            return ServiceFragment(id)
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
        val view = inflater.inflate(R.layout.fragment_service, container, false)
        searchServiceById(serviceId)
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.title = "Service Info"
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter: ArrayAdapter<ServiceStatus> = ArrayAdapter<ServiceStatus>(
            this.requireContext(), android.R.layout.simple_spinner_item, ServiceStatus.values())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        service_info.sp_service_status.adapter = adapter

        applyChanges()
        showHardwareList()
    }

    private fun showHardwareList() {
        service_info.show_hardwares.setOnClickListener {
            var fragment: Fragment? = null
            val bundle = Bundle()
            bundle.putLong("serviceId", serviceId)
            bundle.putString("serviceName", service_info.et_service_name.text.toString())
            try {
                fragment = HardwareListFragment.newInstance()
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

    private fun searchServiceById(id: Long) {
        repository.searchServiceById(id).enqueue(object : Callback<Service> {
            override fun onFailure(call: Call<Service>?, t: Throwable?) {
                Log.d(className, t?.localizedMessage.orEmpty())
            }

            override fun onResponse(call: Call<Service>?, response: Response<Service>?) {
                fillServiceInfo(response?.body())
            }
        })
    }

    private fun fillServiceInfo(service: Service?) {
        service_info.et_service_name.setText(service?.name)
        service_info.sp_service_status.setSelection(service?.serviceStatus?.id!!.toInt() - 1)
    }

    private fun applyChanges() {
        service_info.button.setOnClickListener {
            val curStatus = service_info.sp_service_status.selectedItem.toString()
            val serviceStatus = Service.Status(
                ServiceStatus.valueOf(curStatus).id.toLong(),
                ServiceStatus.valueOf(curStatus).name
            )
            val newService = Service(
                serviceId,
                service_info.et_service_name.text.toString(),
                serviceStatus
            )
            repository.saveService(newService, serviceId).enqueue(object : Callback<Service> {
                override fun onFailure(call: Call<Service>?, t: Throwable?) {
                    Log.d(className, t?.localizedMessage.orEmpty())
                }

                override fun onResponse(call: Call<Service>?, response: Response<Service>?) {
                    Toast.makeText(
                        context, "Data for service with id=[${serviceId}] was updated",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}