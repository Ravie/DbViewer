package com.netcracker.dbviewer.model

data class Hardware(
    val id: Long,
    val name: String,
    val serial: String,
    val hardwareStatus: Status
) {
    data class Status(
        val id: Long,
        val name: String
    )
}

data class HardwareResult (val links: List<Link>, val content: List<Hardware>) {
    data class Link (
        val rel: String,
        val self: String
    )
}