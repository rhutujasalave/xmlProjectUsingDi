package com.example.xmlprojectUsingDi.data.model.response

data class TripListResponse(
    val data: List<TripItem>?
)

data class TripItem(
    val id: Int?,
    val destinationImage: String?,
    val status: String?,
    val category: Category?,
    val receiver: Receiver?,
    val dropoffAddress: DropoffAddress?,
    val customer: Customer?,
    val createdAt: String?,
    val totalCost: Double?,
    val isUpcoming: Boolean? = null,
    val schedule: Schedule?

)

data class Factory(
    val id: Int,
    val name: String
)

data class Coordinate(
    val latitude: Double,
    val longitude: Double
)

data class Receiver(
    val name: String,
    val phone: String,
    val countryId: Int,
    val diaCode: String
)

data class Schedule (
   val type :String?,
   val fromDate :String?,
   val  toDate :String?
)

data class Category(
    val id: Int?,
    val price: Double?,
    val itemType: String?,
    val icon: String?,
    val name: String?,
    val metadata: Any?
)

data class DropoffAddress(
    val id: Int,
    val line1: String,
    val line2: String,
    val city: String,
    val state: String,
    val postal_code: String,
    val isFavorite: Boolean,
    val longitude: Double,
    val latitude: Double
)

data class Customer(
    val id: Int,
    val name: String,
    val phone: String,
    val profileImage: String?,
    val averageRating: Double,
    val role: String
)

data class Option(
    val id: Int,
    val optionId: Int,
    val optionParentId: Int,
    val value: Double,
    val valueType: String,
    val name: String,
    val metadata: Metadata?
)

data class Metadata(
    val unit: String?
)

data class Materials(
    val id: Int,
    val name: String,
    val value: String
)


