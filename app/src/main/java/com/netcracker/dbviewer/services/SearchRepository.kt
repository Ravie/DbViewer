package com.netcracker.dbviewer.services

import com.netcracker.dbviewer.model.*
import retrofit2.Call
import retrofit2.http.Url

object SearchRepositoryProvider {
    fun provideRepository(apiService: RestApiService): SearchRepository {
        return SearchRepository(apiService)
    }
}

class SearchRepository(private val apiService: RestApiService) {
    fun searchCustomers(): Call<CustomerResult> {
        return apiService.searchCustomers()
    }

    fun searchCustomerByPhoneNumber(phoneNumber: Long): Call<Customer> {
        return apiService.searchCustomerByPhoneNumber(phoneNumber)
    }

    fun saveCustomer(customer: Customer, id: Long): Call<Customer> {
        return apiService.saveCustomer(customer, id)
    }

    fun searchCustomerById(id: Long): Call<Customer> {
        return apiService.searchCustomerById(id)
    }

    fun searchHardware(): Call<HardwareResult> {
        return apiService.searchHardware()
    }

    fun searchServices(): Call<ServiceResult> {
        return apiService.searchServices()
    }

    fun searchServicesByCustomer(id: Long): Call<ServiceResult> {
        return apiService.searchServicesByCustomer(id)
    }

    fun searchServiceById(id: Long): Call<Service> {
        return apiService.searchServiceById(id)
    }

    fun saveService(service: Service, id: Long): Call<Service> {
        return apiService.saveService(service, id)
    }

    fun searchHardwareByService(id: Long): Call<HardwareResult> {
        return apiService.searchHardwareByService(id)
    }

    fun searchHardwareById(id: Long): Call<Hardware> {
        return apiService.searchHardwareById(id)
    }

    fun saveHardware(hardware: Hardware, id: Long): Call<Hardware> {
        return apiService.saveHardware(hardware, id)
    }

    fun searchAddresses(): Call<AddressResult> {
        return apiService.searchAddresses()
    }

    fun searchAddressByHardware(hardwareId: Long): Call<Address> {
        return apiService.searchAddressByHardware(hardwareId)
    }

    fun saveAddress(link: String, hardwareId: Long): Call<Void> {
        return apiService.saveAddress(link, hardwareId)
    }

}