package com.example.xmlprojectUsingDi.data.model.response

data class CardListResponse(
    val id: Int,
    val type: String,
    val holderName: String?,
    val cardBrand: String,
    val cardLast4: String,
    val expMonth: String?,
    val expYear: String?
)
