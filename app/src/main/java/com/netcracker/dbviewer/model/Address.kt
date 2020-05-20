package com.netcracker.dbviewer.model

data class Address(
    val id: Long,
    val fullAddress: String
) {
    override fun toString(): String = fullAddress
}

data class AddressResult (val links: List<Link>, val content: List<Address>)