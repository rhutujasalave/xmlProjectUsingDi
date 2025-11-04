package com.example.xmlprojectUsingDi.repository

import com.example.xmlprojectUsingDi.data.api.AuthApiService
import com.example.xmlprojectUsingDi.data.model.request.LoginRequest
import com.example.xmlprojectUsingDi.data.model.request.SignUpRequest
import com.example.xmlprojectUsingDi.data.model.response.CardListResponse
import com.example.xmlprojectUsingDi.data.model.response.DialCodeResponse
import com.example.xmlprojectUsingDi.data.model.response.LoginResponse
import com.example.xmlprojectUsingDi.data.model.response.SignUpResponse
import com.example.xmlprojectUsingDi.data.model.response.TripListResponse
import okhttp3.ResponseBody

import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService
) {

    suspend fun loginUser(
        request: LoginRequest
    ): Response<LoginResponse> {
        return authApiService.loginUser(request)
    }

    suspend fun getDialCodes(): Response<DialCodeResponse> {
        return authApiService.getDialCodes(
            page = 1,
            take = 10,
            sortColumn = "id",
            sortOrder = "ASC"
        )
    }

    suspend fun signUpUser(
        request: SignUpRequest
    ): Response<SignUpResponse> {
        return authApiService.registerUser(request)
    }

    suspend fun getTripList(
        token: String,
        page: Int = 1,
        take: Int = 10,
        sortColumn: String = "id",
        sortOrder: String = "ASC",
        isUpcoming: Boolean = false
    ): Response<TripListResponse> {
        if (token.isEmpty()) {
            return Response.error(401, ResponseBody.create(null, "Token not found"))
        }

        return try {
            authApiService.getTripList(
                authorization = "Bearer $token",
                page = page,
                take = take,
                sortColumn = sortColumn,
                sortOrder = sortOrder,
                isUpcoming = isUpcoming
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Response.error(500, ResponseBody.create(null, e.message ?: "Unknown error"))
        }
    }


    suspend fun getTripDetails(token: String, tripId: Int) =
        authApiService.getTripDetails("Bearer $token", tripId)


    suspend fun getWalletBalance(token: String) =
        authApiService.getWalletBalance("Bearer $token")

    suspend fun getCardList(token: String) =
        authApiService.getCardList("Bearer $token")

//    suspend fun getCardList(token: String): Response<List<CardListResponse>> {
//        return authApiService.getCardList("Bearer $token")
//    }

}

