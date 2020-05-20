package com.netcracker.dbviewer.model

data class Service(
    val id: Long,
    val name: String,
    val serviceStatus: Status
) {
    data class Status(
        val id: Long,
        val name: String
    )
}

data class ServiceResult (val links: List<Link>, val content: List<Service>) {
    data class Link (
        val rel: String,
        val self: String
    )
}