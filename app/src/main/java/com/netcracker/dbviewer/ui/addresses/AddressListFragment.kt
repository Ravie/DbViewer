package com.netcracker.dbviewer.ui.addresses

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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.netcracker.dbviewer.R
import com.netcracker.dbviewer.model.Address
import com.netcracker.dbviewer.model.AddressResult
import com.netcracker.dbviewer.services.RestApiService
import com.netcracker.dbviewer.services.SearchRepositoryProvider
import retrofit2.Call
import retrofit2.Response

class AddressListFragment : Fragment() {

    private val className = AddressListFragment::class.java.simpleName
    private var mAddressList: List<Address>? = null

    companion object {
        fun newInstance(): AddressListFragment = AddressListFragment()
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
        val view = inflater.inflate(R.layout.fragment_address_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.address_recycle_view)
        searchAddresses(recyclerView)
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.title = "Address List"
        addNewAddressListener(view, recyclerView)
        return view
    }

    private fun addNewAddressListener(
        view: View,
        recyclerView: RecyclerView
    ) {
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            var fragment: Fragment? = null
            val bundle = Bundle()
            bundle.putLong("totalCount", recyclerView.adapter!!.itemCount.toLong())
            try {
                fragment = AddressFragment.newInstance()
                fragment.arguments = bundle
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

    private fun searchAddresses(recyclerView : RecyclerView) {
        val apiService = RestApiService.create()
        val repository = SearchRepositoryProvider.provideRepository(apiService)
        val call = repository.searchAddresses()
        call.enqueue(object : retrofit2.Callback<AddressResult> {
            override fun onFailure(call: Call<AddressResult>?, t: Throwable?) {
                Log.d(className, t?.localizedMessage.orEmpty())
            }

            override fun onResponse(call: Call<AddressResult>?, response: Response<AddressResult>?) {
                mAddressList = response?.body()?.content
                recyclerView.adapter = AddressListAdapter(mAddressList!!)
                recyclerView.layoutManager = LinearLayoutManager(activity)
            }
        })
    }
}
