package com.example.xmlprojectUsingDi.data.model.response


data class TripDetailsResponse(
    val id: Int?,
    val customerId: Int?,
    val captainId: Int?,
    val driverId: Int?,
    val paymentId: Int?,
    val distanceKm: Double?,
    val totalMaterialCost: Double?,
    val materialCost: Double?,
    val transportCost: Double?,
    val totalCost: Double?,
    val totalVat: Double?,
    val vatRate: Double?,
    val quantity: Int?,
    val totalLabourCost: Double?,
    val additionalCost: Double?,
    val isUpcoming: Boolean?,
    val status: String?,
    val destinationImage: String?,
    val createdAt: String?,
    val receiver: Receiver?,
    val dropoffAddress: DropoffAddress?,
    val customer: Customer?,
    val schedule: Schedule?,
    val materials: List<Materials>?,
    val category: Category?
)
