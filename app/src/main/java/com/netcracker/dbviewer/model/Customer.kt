package com.netcracker.dbviewer.model

data class Customer(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val phoneNumber: Long,
    val customerStatus: Status
) {
    data class Status(
        val id: Long,
        val name: String
    )
}

data class Link (
    val rel: String,
    val self: String
)

data class CustomerResult (val links: List<Link>, val content: List<Customer>)