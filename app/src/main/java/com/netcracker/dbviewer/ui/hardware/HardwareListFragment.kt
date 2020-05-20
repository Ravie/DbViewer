package com.netcracker.dbviewer.ui.hardware

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
import com.netcracker.dbviewer.model.Hardware
import com.netcracker.dbviewer.model.HardwareResult
import com.netcracker.dbviewer.services.RestApiService
import com.netcracker.dbviewer.services.SearchRepositoryProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HardwareListFragment : Fragment() {
    private var serviceId = -1L
    private var serviceName= ""
    private val className = HardwareListFragment::class.java.simpleName
    private var mHardwareList: List<Hardware>? = null

    companion object {
        fun newInstance(): HardwareListFragment {
            return HardwareListFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        if (arguments != null) {
            serviceId = requireArguments().getLong("serviceId")
            serviceName = requireArguments().getString("serviceName").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hardware_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.hardware_recycle_view)
        searchHardware(recyclerView)

        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        if (serviceId != -1L) {
            toolbar?.title = "Hardware List: $serviceName"
        } else {
            toolbar?.title = "Hardware List"
        }

        return view
    }

    private fun searchHardware(recyclerView : RecyclerView) {
        val apiService = RestApiService.create()
        val repository = SearchRepositoryProvider.provideRepository(apiService)

        if (serviceId == -1L) {
            val call = repository.searchHardware()
            execute(call, recyclerView)
        } else {
            val call = repository.searchHardwareByService(serviceId)
            execute(call, recyclerView)
        }
    }

    private fun execute(call: Call<HardwareResult>, recyclerView: RecyclerView) {
        call.enqueue(object : Callback<HardwareResult> {
            override fun onFailure(call: Call<HardwareResult>?, t: Throwable?) {
                Log.d(className, t?.localizedMessage.orEmpty())
            }

            override fun onResponse(call: Call<HardwareResult>?, response: Response<HardwareResult>?) {
                mHardwareList = response?.body()?.content
                recyclerView.adapter = HardwareListAdapter(mHardwareList!!) { hardware ->
                    hardwareItemClicked(hardware) }
                recyclerView.layoutManager = LinearLayoutManager(activity)
            }
        })
    }

    private fun hardwareItemClicked(hardware: Hardware) {
        var fragment: Fragment? = null
        try {
            fragment = HardwareFragment.newInstance(hardware.id)
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
