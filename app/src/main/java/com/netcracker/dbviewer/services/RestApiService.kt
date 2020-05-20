package com.netcracker.dbviewer.services

import com.netcracker.dbviewer.model.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

interface RestApiService {

    @GET("customers")
    fun searchCustomers(): Call<CustomerResult>

    @GET("customers/search/getCustomerByPhoneNumber")
    fun searchCustomerByPhoneNumber(@Query("phoneNumber") phoneNumber: Long): Call<Customer>

    @GET("hardwares")
    fun searchHardware(): Call<HardwareResult>

    @GET("services")
    fun searchServices(): Call<ServiceResult>

    @GET("addresses")
    fun searchAddresses(): Call<AddressResult>

    @GET("customers/{id}/services")
    fun searchServicesByCustomer(@Path("id") id: Long): Call<ServiceResult>

    @GET("customers/{id}")
    fun searchCustomerById(@Path("id") id: Long): Call<Customer>

    @Headers("Content-Type: application/json")
    @PUT("customers/{id}")
    fun saveCustomer(@Body customer: Customer, @Path("id") id: Long): Call<Customer>

    @GET("services/{id}")
    fun searchServiceById(@Path("id") id: Long): Call<Service>

    @Headers("Content-Type: application/json")
    @PUT("services/{id}")
    fun saveService(@Body service: Service, @Path("id") id: Long): Call<Service>

    @GET("services/{id}/hardwares")
    fun searchHardwareByService(@Path("id") id: Long): Call<HardwareResult>

    @GET("hardwares/{id}")
    fun searchHardwareById(@Path("id") id: Long): Call<Hardware>

    @Headers("Content-Type: application/json")
    @PUT("hardwares/{id}")
    fun saveHardware(@Body hardware: Hardware, @Path("id") id: Long): Call<Hardware>

    @GET("hardwares/{id}/address")
    fun searchAddressByHardware(@Path("id") hardwareId: Long): Call<Address>

    @Headers("Content-Type: text/uri-list")
    @PUT("hardwares/{id}/address")
    fun saveAddress(@Body link: String, @Path("id") hardwareId: Long): Call<Void>

    companion object Factory {
        fun create(): RestApiService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://mk-nc.herokuapp.com/")
                .build()

            return retrofit.create(RestApiService::class.java);
        }
    }
}