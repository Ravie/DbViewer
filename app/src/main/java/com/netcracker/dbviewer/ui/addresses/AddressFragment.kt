package com.netcracker.dbviewer.ui.addresses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.netcracker.dbviewer.R
import com.netcracker.dbviewer.model.Address
import com.netcracker.dbviewer.services.RestApiService
import com.netcracker.dbviewer.services.SearchRepositoryProvider
import kotlinx.android.synthetic.main.fragment_address.*
import kotlinx.android.synthetic.main.fragment_address.view.*
import kotlinx.android.synthetic.main.fragment_address_list.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressFragment : Fragment()  {
    private var totalCount = -1L
    private val className = AddressFragment::class.java.simpleName
    private val repository = SearchRepositoryProvider.provideRepository(RestApiService.create())

    companion object {
        fun newInstance(): AddressFragment {
            return AddressFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        if (arguments != null) {
            totalCount = requireArguments().getLong("totalCount")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_address, container, false)
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.title = "Customer Info"
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createNewAddress()
    }

    private fun createNewAddress() {
        address_info.et_address.setText("")
        address_info.button.setOnClickListener {
            val address = Address(
                totalCount + 1,
                address_info.et_address.text.toString()
            )
            repository.saveAddress(address).enqueue(object :
                Callback<Address> {
                override fun onFailure(call: Call<Address>?, t: Throwable?) {
                    Log.d(className, t?.localizedMessage.orEmpty())
                }

                override fun onResponse(call: Call<Address>?, response: Response<Address>?) {
                    Toast.makeText(
                        context, "New address with id=[${response?.body()?.id}] was created",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

}
