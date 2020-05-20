package com.netcracker.dbviewer.ui.hardware

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.netcracker.dbviewer.R
import com.netcracker.dbviewer.model.Address
import com.netcracker.dbviewer.model.AddressResult
import com.netcracker.dbviewer.model.Hardware
import com.netcracker.dbviewer.services.RestApiService
import com.netcracker.dbviewer.services.SearchRepositoryProvider
import kotlinx.android.synthetic.main.fragment_hardware.*
import kotlinx.android.synthetic.main.fragment_hardware.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HardwareFragment(id: Long) : Fragment() {

    private lateinit var addresses: List<Address>
    private val className = HardwareFragment::class.java.simpleName
    private val hardwareId = id
    private val repository = SearchRepositoryProvider.provideRepository(RestApiService.create())

    companion object {
        fun newInstance(id: Long): HardwareFragment {
            return HardwareFragment(id)
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
        val view = inflater.inflate(R.layout.fragment_hardware, container, false)
        searchServiceById(hardwareId)
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.title = "Hardware Info"
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter: ArrayAdapter<HardwareStatus> = ArrayAdapter<HardwareStatus>(
            this.requireContext(), android.R.layout.simple_spinner_item, HardwareStatus.values())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        hardware_info.sp_hardware_status.adapter = adapter

        loadAddresses()

        applyChanges()
    }

    private fun loadAddresses() {
        repository.searchAddresses().enqueue(object : Callback<AddressResult> {
            override fun onFailure(call: Call<AddressResult>?, t: Throwable?) {
                Log.d(className, t?.localizedMessage.orEmpty())
            }

            override fun onResponse(call: Call<AddressResult>?, response: Response<AddressResult>?) {
                addresses = response?.body()?.content!!
                val adapter: ArrayAdapter<Address> = ArrayAdapter<Address>(
                    requireContext(), android.R.layout.simple_spinner_item, addresses
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                hardware_info.sp_hardware_address.adapter = adapter
                loadHardwareAddress(adapter)
            }
        })
    }

    private fun loadHardwareAddress(adapter: ArrayAdapter<Address>) {
        repository.searchAddressByHardware(hardwareId).enqueue(object : Callback<Address> {
            override fun onFailure(call: Call<Address>?, t: Throwable?) {
                Log.d(className, t?.localizedMessage.orEmpty())
            }

            override fun onResponse(call: Call<Address>?, response: Response<Address>?) {
                val position = adapter.getPosition(response?.body())
                hardware_info.sp_hardware_address.setSelection(position)
            }
        })
    }

    private fun searchServiceById(id: Long) {
        repository.searchHardwareById(id).enqueue(object : Callback<Hardware> {
            override fun onFailure(call: Call<Hardware>?, t: Throwable?) {
                Log.d(className, t?.localizedMessage.orEmpty())
            }

            override fun onResponse(call: Call<Hardware>?, response: Response<Hardware>?) {
                fillHardwareInfo(response?.body())
            }
        })
    }

    private fun fillHardwareInfo(hardware: Hardware?) {
        hardware_info.et_hardware_name.setText(hardware?.name)
        hardware_info.et_hardware_serial.setText(hardware?.serial)
        hardware_info.sp_hardware_status.setSelection(hardware?.hardwareStatus?.id!!.toInt() - 1)
    }

    private fun applyChanges() {
        hardware_info.button.setOnClickListener {
            val curStatus = hardware_info.sp_hardware_status.selectedItem.toString()
            val hardwareStatus = Hardware.Status(
                HardwareStatus.valueOf(curStatus).id.toLong(),
                HardwareStatus.valueOf(curStatus).name
            )
            val newHardware = Hardware(
                hardwareId,
                hardware_info.et_hardware_name.text.toString(),
                hardware_info.et_hardware_serial.text.toString(),
                hardwareStatus
            )
            repository.saveHardware(newHardware, hardwareId).enqueue(object : Callback<Hardware> {
                override fun onFailure(call: Call<Hardware>?, t: Throwable?) {
                    Log.d(className, t?.localizedMessage.orEmpty())
                }

                override fun onResponse(call: Call<Hardware>?, response: Response<Hardware>?) {
                    Toast.makeText(
                        context, "Data for hardware with id=[${hardwareId}] was updated",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
            saveAddress()
        }
    }

    private fun saveAddress() {
        val curAddress = hardware_info.sp_hardware_address.selectedItem.toString()
        for (address in addresses) {
            if (address.fullAddress == curAddress) {
                repository.saveHardwareAddress("addresses/${address.id}", hardwareId).enqueue(object : Callback<Void> {
                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        Log.d(className, t?.localizedMessage.orEmpty())
                    }

                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        Toast.makeText(
                            context, "Address id=[${address.id}] for hardware with id=[${hardwareId}] was updated",
                            Toast.LENGTH_SHORT
                        ).show()
                        //val jObjError = JSONObject(response?.errorBody()!!.string())
                    }
                })
            }
        }
    }
}